package org.kmt.lld.meetingscheduler.service.notification;

import org.kmt.lld.meetingscheduler.models.enums.NotificationType;
import org.kmt.lld.meetingscheduler.models.Notification;
import org.kmt.lld.meetingscheduler.service.notification.sender.NotificationSender;
import org.kmt.lld.meetingscheduler.service.notification.sender.NotificationSenderFactory;
import org.kmt.lld.meetingscheduler.utils.Logger;

/**
 * Service class for sending notifications to users.
 */
public class NotificationService {

    // Singleton instance of Logger for logging information
    Logger log = Logger.getInstance();

    // Factory for creating NotificationSender instances
    NotificationSenderFactory notificationSenderFactory;


    public NotificationService(NotificationSenderFactory notificationSenderFactory) {
        this.notificationSenderFactory = notificationSenderFactory;
    }

    /**
     * Sends a notification to a single user with the specified message.
     */
    public void sendNotification(Notification notification, NotificationType notificationType) {
        NotificationSender notificationSender = notificationSenderFactory.getNotificationSender(notificationType);
        notificationSender.send(notification);
    }

    /**
     * Sends notifications to a single user across all channels.
     */
    public void sendNotificationOverAllChannels(Notification notification) {
        for (NotificationType notificationType : NotificationType.values()) {
            NotificationSender notificationSender = notificationSenderFactory.getNotificationSender(notificationType);
            notificationSender.send(notification);
        }
    }
}
