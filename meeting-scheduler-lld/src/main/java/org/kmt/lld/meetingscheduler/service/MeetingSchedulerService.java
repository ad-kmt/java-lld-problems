package org.kmt.lld.meetingscheduler.service;

import org.kmt.lld.meetingscheduler.exceptions.ParticipantNotFoundException;
import org.kmt.lld.meetingscheduler.exceptions.meetingscheduler.MeetingOverlapException;
import org.kmt.lld.meetingscheduler.exceptions.meetingscheduler.MeetingRoomCapacityLimitException;
import org.kmt.lld.meetingscheduler.models.Interval;
import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.Room;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.repository.MeetingRepository;
import org.kmt.lld.meetingscheduler.repository.RoomRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing meeting scheduling.
 */
public class MeetingSchedulerService {

    public MeetingRepository meetingRepository;
    public RoomRepository roomRepository;

    public NotificationService notificationService;

    public MeetingSchedulerService(MeetingRepository meetingRepository, RoomRepository roomRepository, NotificationService notificationService){
        this.meetingRepository = meetingRepository;
        this.roomRepository = roomRepository;
        this.notificationService = notificationService;
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
        System.out.println("Meeting created: " + meetingRequest);

        notificationService.sendNotification(
                meeting.getInvites().stream().map(Meeting.Invite::getParticipant).toList(),
                String.format("You're invited to %s by %s", meeting.getTitle(), meeting.getOrganizer().getName())
        );

        System.out.println("Notification sent to all participants.");
        return meeting;
    }

    /**
     * Checks if the requested meeting time is available.
     */
    public boolean checkAvailability(Meeting meetingRequest){
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
    public boolean isOverlapping(Interval A, Interval B){
        return !(A.getEndTime().isBefore(B.getStartTime()) || B.getEndTime().isBefore(A.getStartTime()));
    }

    /**
     * Responds to a meeting invitation.
     */
    public void respondToInvitation(User participant, Meeting.InviteResponse inviteResponse, Meeting meeting) {

        Stream<User> participantStream = meeting.getInvites().stream().map(Meeting.Invite::getParticipant);

        if(participantStream.noneMatch(p -> p.getEmail().equals(participant.getEmail()))) {
            throw new ParticipantNotFoundException("Participant not part of the meeting");
        }

        for(Meeting.Invite invite : meeting.getInvites()){
            if(invite.getParticipant().getEmail().equals(participant.getEmail())){
                invite.setInviteStatus(inviteResponse);
            }
        }
        meetingRepository.update(meeting);
        System.out.printf("Response: %s set for participant: %s for meeting: %s%n", inviteResponse, participant, meeting);
    }


}