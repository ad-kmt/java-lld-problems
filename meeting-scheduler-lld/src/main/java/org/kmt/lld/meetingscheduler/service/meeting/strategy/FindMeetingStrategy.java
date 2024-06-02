package org.kmt.lld.meetingscheduler.service.meeting.strategy;

import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.MeetingRequest;

/**
 * Interface for defining strategies to find available meeting slots.
 * Implementations of this interface will provide different strategies for finding
 * available meeting slots based on the meeting request.
 */
public interface FindMeetingStrategy {

    /**
     * Finds an available meeting slot based on the given meeting request.
     */
    Meeting findMeeting(MeetingRequest meetingRequest);
}

