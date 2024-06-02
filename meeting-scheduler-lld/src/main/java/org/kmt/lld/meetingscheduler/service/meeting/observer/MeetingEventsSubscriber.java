package org.kmt.lld.meetingscheduler.service.meeting.observer;

import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.models.enums.InviteResponse;

public interface MeetingEventsSubscriber {
    void creationEvent(Meeting meeting);
    void userResponseEvent(Meeting meeting, User participant, InviteResponse inviteResponse);
}
