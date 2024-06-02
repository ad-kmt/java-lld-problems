package org.kmt.lld.meetingscheduler.repository;

import org.kmt.lld.meetingscheduler.exceptions.repository.MeetingNotFoundException;
import org.kmt.lld.meetingscheduler.models.Meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Repository class for managing meetings.
 * Uses ConcurrentHashMap for thread-safe access to meetings and AtomicInteger for unique ID generation.
 */
public class MeetingRepository {
    private final ConcurrentHashMap<Integer, Meeting> meetings = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(1);

    /**
     * Creates a new meeting and stores it in the repository.
     *
     * Synchronization not required since there is only one transaction map.put()
     */
    public Meeting create(Meeting meeting) {
        meeting.setId(counter.getAndIncrement());
        meetings.put(meeting.getId(), meeting);
        return meeting;
    }

    /**
     * Updates an existing meeting in the repository.
     *
     * Synchronization required since there are 2 transactions map.containsKey() and map.put()
     */
    public synchronized Meeting update(Meeting meeting) {
        if (!meetings.containsKey(meeting.getId())) {
            throw new MeetingNotFoundException("Meeting not found");
        }
        meetings.put(meeting.getId(), meeting);
        return meeting;
    }

    /**
     * Retrieves all meetings from the repository.
     */
    public List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetings.values());
    }

    /**
     * Retrieves all meetings for a specific room.
     */
    public List<Meeting> getAllMeetingsByRoom(int roomId) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getRoom().getId() == roomId)
                .toList();
    }

    /**
     * Retrieves a meeting by its ID.
     */
    public Meeting getMeetingById(int meetingId) {
        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            throw new MeetingNotFoundException("Meeting not found");
        }
        return meeting;
    }

    /**
     * Deletes a meeting by its ID.
     *
     * Synchronization required since there are 2 transactions map.containsKey() and map.remove()
     */
    public synchronized void deleteMeeting(int meetingId) {
        if (!meetings.containsKey(meetingId)) {
            throw new MeetingNotFoundException("Meeting not found");
        }
        meetings.remove(meetingId);
    }
}
