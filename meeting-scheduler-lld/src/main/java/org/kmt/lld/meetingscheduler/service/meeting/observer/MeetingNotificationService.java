package org.kmt.lld.meetingscheduler.service.meeting.observer;

import org.kmt.lld.meetingscheduler.models.Invite;
import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.models.enums.InviteResponse;
import org.kmt.lld.meetingscheduler.models.Notification;
import org.kmt.lld.meetingscheduler.service.notification.NotificationService;

public class MeetingNotificationService implements MeetingEventsSubscriber {

    NotificationService notificationService;

    public MeetingNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Handles the creation event by sending notifications to participants and the organizer.
     */
    @Override
    public void creationEvent(Meeting meeting) {
        // Send notification to all participants
        meeting.getInvites().stream()
                .map(Invite::getParticipant)
                .forEach(participant -> {
                    Notification notification = new Notification(participant, String.format("You're invited to %s by %s", meeting.getTitle(), meeting.getOrganizer().getName()));
                    notificationService.sendNotificationOverAllChannels(notification);
                });

        // Send notification to the organizer
        Notification organizerNotification = new Notification(meeting.getOrganizer(), String.format("You've created meeting: %s", meeting.getTitle()));
        notificationService.sendNotificationOverAllChannels(organizerNotification);
    }

    /**
     * Handles the user response event by sending notifications to the organizer and the participant who responded.
     */
    @Override
    public void userResponseEvent(Meeting meeting, User participant, InviteResponse inviteResponse) {
        Notification notificationForOrganizer = new Notification(meeting.getOrganizer(), String.format("%S has %s invite to %s", participant.getName(), inviteResponse.toString(), meeting.getTitle()));
        notificationService.sendNotificationOverAllChannels(notificationForOrganizer);

        Notification notificationForParticipant = new Notification(participant, String.format("You've %s invite to %s", inviteResponse, meeting.getTitle()));
        notificationService.sendNotificationOverAllChannels(notificationForParticipant);
    }
}
