package org.kmt.lld.meetingscheduler.service.notification;

import org.kmt.lld.meetingscheduler.models.enums.NotificationType;
import org.kmt.lld.meetingscheduler.models.notification.Notification;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.service.notification.sender.NotificationSender;
import org.kmt.lld.meetingscheduler.service.notification.sender.NotificationSenderFactory;
import org.kmt.lld.meetingscheduler.utils.Logger;

import java.util.List;

/**
 * Service class for sending notifications to users.
 */
public class NotificationService {

    // Singleton instance of Logger for logging information
    Logger log = Logger.getInstance();

    // Factory for creating NotificationSender instances
    NotificationSenderFactory notificationSenderFactory;


    public NotificationService(NotificationSenderFactory notificationSenderFactory){
        this.notificationSenderFactory = notificationSenderFactory;
    }

    /**
     * Sends a notification to a single user with the specified message.
     */
    public void sendNotification(User user, String message, NotificationType notificationType) {
        Notification notification = new Notification(user, message);
        NotificationSender notificationSender = notificationSenderFactory.getNotificationSender(notificationType);
        notificationSender.send(notification);
    }

    /**
     * Sends notifications to a single user across all channels.
     */
    public void sendNotificationOverAllChannels(User user, String message) {
        Notification notification = new Notification(user, message);
        for(NotificationType notificationType : NotificationType.values()){
            NotificationSender notificationSender = notificationSenderFactory.getNotificationSender(notificationType);
            notificationSender.send(notification);
        }
    }
}
