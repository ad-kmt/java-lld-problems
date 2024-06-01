package org.kmt.lld.meetingscheduler.repository;

import org.kmt.lld.meetingscheduler.exceptions.MeetingNotFoundException;
import org.kmt.lld.meetingscheduler.models.Meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository class for managing meetings.
 */
public class MeetingRepository {
    private final Map<Integer, Meeting> meetings = new HashMap<>();
    private int counter = 1;

    public Meeting create(Meeting meeting) {
        meeting.setId(counter++);
        meetings.put(meeting.getId(), meeting);
        return meeting;
    }

    public Meeting update(Meeting meeting) {
        if (!meetings.containsKey(meeting.getId())) {
            throw new MeetingNotFoundException("Meeting not found");
        }
        meetings.put(meeting.getId(), meeting);
        return meeting;
    }

    public List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetings.values());
    }

    public List<Meeting> getAllMeetingsByRoom(int roomId) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getRoom().getId() == roomId)
                .toList();
    }

    public Meeting getMeetingById(int meetingId) {
        return meetings.get(meetingId);
    }

    public void deleteMeeting(int meetingId) {
        meetings.remove(meetingId);
    }
}
