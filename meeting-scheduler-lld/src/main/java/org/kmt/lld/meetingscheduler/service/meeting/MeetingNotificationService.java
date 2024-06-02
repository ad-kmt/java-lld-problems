package org.kmt.lld.meetingscheduler.service.meeting;

import org.kmt.lld.meetingscheduler.models.Invite;
import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.models.enums.InviteResponse;
import org.kmt.lld.meetingscheduler.models.notification.Notification;
import org.kmt.lld.meetingscheduler.service.meeting.observer.MeetingEventsSubscriber;
import org.kmt.lld.meetingscheduler.service.notification.NotificationService;

public class MeetingNotificationService implements MeetingEventsSubscriber {

    NotificationService notificationService;

    public MeetingNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void creationEvent(Meeting meeting) {
        meeting.getInvites().stream()
                .map(Invite::getParticipant)
                .forEach(participant -> {
                    Notification notification = new Notification(participant, String.format("You're invited to %s by %s", meeting.getTitle(), meeting.getOrganizer().getName()));
                    notificationService.sendNotificationOverAllChannels(notification);
                });

        Notification organizerNotification = new Notification(meeting.getOrganizer(), String.format("You've created meeting: %s", meeting.getOrganizer().getName()));
        notificationService.sendNotificationOverAllChannels(organizerNotification);
    }

    @Override
    public void userResponseEvent(Meeting meeting, User participant, InviteResponse inviteResponse) {
        Notification notificationForOrganizer = new Notification(meeting.getOrganizer(), String.format("%S has %s invite to %s", participant.getName(), inviteResponse.toString(), meeting.getTitle()));
        notificationService.sendNotificationOverAllChannels(notificationForOrganizer);

        Notification notificationForParticipant = new Notification(participant, String.format("You've %s invite to %s", inviteResponse, meeting.getTitle()));
        notificationService.sendNotificationOverAllChannels(notificationForParticipant);
    }
}
