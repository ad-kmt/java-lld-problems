package org.kmt.lld.meetingscheduler.service.meeting;

import org.kmt.lld.meetingscheduler.exceptions.service.MeetingOverlapException;
import org.kmt.lld.meetingscheduler.exceptions.service.MeetingRoomCapacityLimitException;
import org.kmt.lld.meetingscheduler.exceptions.service.ParticipantNotFoundException;
import org.kmt.lld.meetingscheduler.models.*;
import org.kmt.lld.meetingscheduler.models.enums.InviteResponse;
import org.kmt.lld.meetingscheduler.repository.MeetingRepository;
import org.kmt.lld.meetingscheduler.repository.RoomRepository;
import org.kmt.lld.meetingscheduler.service.meeting.observer.MeetingEventsSubscriber;
import org.kmt.lld.meetingscheduler.utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing meeting scheduling.
 */
public class MeetingSchedulerService {

    public Logger log = Logger.getInstance();
    public MeetingRepository meetingRepository;
    public RoomRepository roomRepository;
    public List<MeetingEventsSubscriber> meetingEventsSubscribers;
    private final Map<Integer, Lock> roomLocks = new ConcurrentHashMap<>(); // Maintain locks for each room.
    private final Map<Integer, Lock> meetingLocks = new ConcurrentHashMap<>(); // Maintain locks for each meeting.

    public MeetingSchedulerService(MeetingRepository meetingRepository, RoomRepository roomRepository, MeetingEventsSubscriber... meetingEventsSubscribers) {
        this.meetingRepository = meetingRepository;
        this.roomRepository = roomRepository;
        this.meetingEventsSubscribers = new ArrayList<>(Arrays.asList(meetingEventsSubscribers));
    }

    /**
     * Schedules a new meeting if the room capacity and availability checks pass.
     *
     * CONCURRENCY HANDLING:
     * - Multiple threads might try to schedule meetings in the same room simultaneously, leading to race conditions.
     * - Instead of locking the entire method, synchronize only the critical sections that need atomic access.
     * - Use a fine-grained lock on the room to aid performance, ensuring that all threads use the same lock for operations on the same room.
     * - Move event notification outside the lock to reduce the scope of the critical section.
     *
     * @param meetingRequest The meeting request containing details of the meeting to be scheduled.
     * @return The scheduled meeting.
     * @throws MeetingRoomCapacityLimitException If the meeting room capacity is less than the total participants.
     * @throws MeetingOverlapException If the new meeting overlaps with an existing meeting.
     */
    public Meeting scheduleMeeting(Meeting meetingRequest) {
        Room room = meetingRequest.getRoom();
        Lock roomLock = roomLocks.computeIfAbsent(room.getId(), k -> new ReentrantLock());

        // Room capacity validation
        if (room.getCapacity() < meetingRequest.getInvites().size() + 1) {
            throw new MeetingRoomCapacityLimitException("Failed to schedule meeting. Meeting room capacity is less than total participants.");
        }

        Meeting meeting;
        roomLock.lock();
        try {
            if (!checkAvailability(meetingRequest)) {
                throw new MeetingOverlapException("Failed to schedule meeting. New meeting is overlapping with existing meeting.");
            }

            meeting = meetingRepository.create(meetingRequest);
            log.info("Meeting created: " + meetingRequest);
        } finally {
            roomLock.unlock();
        }

        // Send creation event to all subscribers outside of the lock to reduce scope of the critical section
        for (MeetingEventsSubscriber meetingEventsSubscriber : meetingEventsSubscribers) {
            meetingEventsSubscriber.creationEvent(meeting);
        }
        log.info("Meeting creation event sent to all subscribers.");
        return meeting;
    }

    /**
     * Checks if the requested meeting time is available.
     *
     * This method does not require synchronization as it involves only read operations.
     *
     * @param meetingRequest The meeting request containing the desired meeting time.
     * @return True if the meeting time is available, false otherwise.
     */
    public boolean checkAvailability(Meeting meetingRequest) {
        return meetingRepository.getAllMeetings().stream()
                .filter(existingMeeting -> existingMeeting.getRoom().getId() == meetingRequest.getRoom().getId())
                .noneMatch(existingMeeting -> isOverlapping(existingMeeting.getInterval(), meetingRequest.getInterval()));
    }

    /**
     * Returns a list of rooms available during the specified interval.
     *
     * This method does not require synchronization as it involves only read operations.
     *
     * @param interval The interval for which to check room availability.
     * @return A list of available rooms.
     */
    public List<Room> getAvailableRooms(Interval interval) {
        Set<Integer> overlappingRooms = meetingRepository.getAllMeetings().stream()
                .filter(existingMeeting -> isOverlapping(existingMeeting.getInterval(), interval))
                .map(overlappingMeeting -> overlappingMeeting.getRoom().getId())
                .collect(Collectors.toSet());

        return roomRepository.getAllRooms().stream()
                .filter(room -> !overlappingRooms.contains(room.getId()))
                .toList();
    }

    /**
     * Checks if two intervals overlap.
     *
     * This method does not require synchronization as it involves only read operations.
     *
     * @param A The first interval.
     * @param B The second interval.
     * @return True if the intervals overlap, false otherwise.
     */
    public boolean isOverlapping(Interval A, Interval B) {
        return !(A.getEndTime().isBefore(B.getStartTime()) || B.getEndTime().isBefore(A.getStartTime()));
    }

    /**
     * Responds to a meeting invitation.
     *
     * CONCURRENCY HANDLING:
     * - Instead of locking the entire method, synchronize only the critical sections that need atomic access.
     * - Use a fine-grained lock on the meeting to aid performance, ensuring that all threads use the same lock for operations on the same meeting.
     * - Move event notification outside the lock to reduce the scope of the critical section.
     *
     * @param participant The participant responding to the invitation.
     * @param inviteResponse The response to the invitation.
     * @param meetingId The ID of the meeting.
     * @throws ParticipantNotFoundException If the participant is not part of the meeting.
     */
    public void respondToInvitation(User participant, InviteResponse inviteResponse, int meetingId) {
        Meeting meeting = meetingRepository.getMeetingById(meetingId);
        Lock meetingLock = meetingLocks.computeIfAbsent(meetingId, k -> new ReentrantLock());

        // Synchronize on the meeting lock to ensure atomicity while handling invitation response
        meetingLock.lock();
        try {
            Stream<User> participantStream = meeting.getInvites().stream().map(Invite::getParticipant);

            if (participantStream.noneMatch(p -> p.getEmail().equals(participant.getEmail()))) {
                throw new ParticipantNotFoundException("Participant not part of the meeting");
            }

            for (Invite invite : meeting.getInvites()) {
                if (invite.getParticipant().getEmail().equals(participant.getEmail())) {
                    invite.setInviteStatus(inviteResponse);
                }
            }

            meetingRepository.update(meeting);
            log.info(String.format("Response: %s set for participant: %s for meeting: %s", inviteResponse, participant, meeting));
        } finally {
            meetingLock.unlock();
        }

        // Send participant response event to all subscribers outside of the lock to reduce scope of the critical section
        for (MeetingEventsSubscriber meetingEventsSubscriber : meetingEventsSubscribers) {
            meetingEventsSubscriber.userResponseEvent(meeting, participant, inviteResponse);
        }
        log.info("Participant response event sent to all subscribers.");
    }
}
