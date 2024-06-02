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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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


    public MeetingSchedulerService(MeetingRepository meetingRepository, RoomRepository roomRepository, MeetingEventsSubscriber... meetingEventsSubscribers) {
        this.meetingRepository = meetingRepository;
        this.roomRepository = roomRepository;
        this.meetingEventsSubscribers = new ArrayList<>();
        this.meetingEventsSubscribers.addAll(Arrays.asList(meetingEventsSubscribers));
    }



    /**
     * Schedules a new meeting if the room capacity and availability checks pass.
     */
    public Meeting scheduleMeeting(Meeting meetingRequest) {
        Room room = meetingRequest.getRoom();

        if (room.getCapacity() < meetingRequest.getInvites().size() + 1) {
            throw new MeetingRoomCapacityLimitException("Failed to schedule meeting. Meeting room capacity is less than total participants.");
        }

        if (!checkAvailability(meetingRequest)) {
            throw new MeetingOverlapException("Failed to schedule meeting. New meeting is overlapping with existing meeting.");
        }

        Meeting meeting = meetingRepository.create(meetingRequest);
        log.info("Meeting created: " + meetingRequest);

        // Send creation event to all subscribers
        for(MeetingEventsSubscriber meetingEventsSubscriber: meetingEventsSubscribers){
            meetingEventsSubscriber.creationEvent(meeting);
        }
        log.info("Meeting creation event sent to all subscribers.");
        return meeting;
    }

    /**
     * Checks if the requested meeting time is available.
     */
    public boolean checkAvailability(Meeting meetingRequest) {
        return meetingRepository.getAllMeetings().stream()
                .filter(existingMeeting -> existingMeeting.getRoom().getId() == meetingRequest.getRoom().getId())
                .noneMatch(existingMeeting -> isOverlapping(existingMeeting.getInterval(), meetingRequest.getInterval()));
    }

    /**
     * Returns a list of rooms available during the specified interval.
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
     */
    public boolean isOverlapping(Interval A, Interval B) {
        return !(A.getEndTime().isBefore(B.getStartTime()) || B.getEndTime().isBefore(A.getStartTime()));
    }

    /**
     * Responds to a meeting invitation.
     */
    public void respondToInvitation(User participant, InviteResponse inviteResponse, int meetingId) {
        Meeting meeting = meetingRepository.getMeetingById(meetingId);

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

        // Send creation event to all subscribers
        for(MeetingEventsSubscriber meetingEventsSubscriber: meetingEventsSubscribers){
            meetingEventsSubscriber.userResponseEvent(meeting, participant, inviteResponse);
        }
        log.info("Participant response event sent to all subscribers.");
    }




}
