package org.kmt.lld.meetingscheduler.service.meeting.strategy;

import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.MeetingRequest;
import org.kmt.lld.meetingscheduler.models.Room;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.repository.MeetingRepository;
import org.kmt.lld.meetingscheduler.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Strategy for finding the first available meeting slot across all rooms.
 * This strategy considers the availability of all rooms and finds the first available slot
 * for the requested meeting duration starting from the current time.
 */
public class FirstAvailableSlotStrategy implements FindMeetingStrategy {
    private final MeetingRepository meetingRepository;
    private final RoomRepository roomRepository;

    public FirstAvailableSlotStrategy(MeetingRepository meetingRepository, RoomRepository roomRepository) {
        this.meetingRepository = meetingRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Finds the first available meeting slot based on the given meeting request.
     * This method does not require synchronization because it primarily involves read operations.
     *
     * HANDLING CONCURRENCY
     * - Read operations on the repository methods are generally thread-safe as they do not modify shared data.
     * - The logic in this method involves reading the list of rooms and meetings, sorting them, and checking for available slots.
     * - If concurrent modifications (additions or deletions of meetings/rooms) occur, they will be handled gracefully by the
     *   underlying thread-safe collections (e.g., ConcurrentHashMap used in repositories).
     * - Ensuring thread safety for write operations (e.g., create, update, delete meetings) is the responsibility of the repository methods.
     */
    @Override
    public Meeting findMeeting(MeetingRequest meetingRequest) {
        List<Room> allRooms = roomRepository.getAllRooms(); // Retrieve all rooms from the repository
        LocalDateTime now = LocalDateTime.now(); // Current time
        double durationHours = meetingRequest.getDuration(); // Meeting duration in hours

        // Iterate through all rooms to find the first available slot
        for (Room room : allRooms) {
            List<Meeting> roomMeetings = meetingRepository.getAllMeetingsByRoom(room.getId()); // Get all meetings for the room

            // Sort meetings by start time
            List<Meeting> sortedMeetings = roomMeetings.stream()
                    .sorted(Comparator.comparing(m -> m.getInterval().getStartTime()))
                    .toList();

            LocalDateTime possibleStartTime = now;
            LocalDateTime possibleEndTime = possibleStartTime.plusHours((long) durationHours);

            // Check for gaps between meetings in the sorted list
            for (Meeting meeting : sortedMeetings) {
                if (possibleEndTime.isBefore(meeting.getInterval().getStartTime())) {
                    // Found a gap before the current meeting's start time
                    return new Meeting(meetingRequest.getTitle(), possibleStartTime, possibleEndTime, room, meetingRequest.getOrganizer(), meetingRequest.getParticipants().toArray(new User[0]));
                } else {
                    // Move to the end time of the current meeting and check the next slot
                    possibleStartTime = meeting.getInterval().getEndTime();
                    possibleEndTime = possibleStartTime.plusHours((long) durationHours);
                }
            }

            // If no gaps found, check after the last meeting in the room
            if (sortedMeetings.isEmpty() || possibleStartTime.isAfter(sortedMeetings.get(sortedMeetings.size() - 1).getInterval().getEndTime())) {
                return new Meeting(meetingRequest.getTitle(), possibleStartTime, possibleEndTime, room, meetingRequest.getOrganizer(), meetingRequest.getParticipants().toArray(new User[0]));
            }
        }

        // If no rooms are available, return null or handle as per application requirements
        return null;
    }
}

