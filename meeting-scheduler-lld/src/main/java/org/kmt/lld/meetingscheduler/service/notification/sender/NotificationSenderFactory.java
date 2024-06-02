package org.kmt.lld.meetingscheduler.service.notification.sender;

import org.kmt.lld.meetingscheduler.models.enums.NotificationType;

/**
 * Factory class for creating instances of NotificationSender.
 */
public class NotificationSenderFactory {

    // Instances of notification sender implementations
    SmsNotificationSenderImpl smsNotificationSender;
    EmailNotificationSenderImpl emailNotificationSender;

    /**
     * Constructor to initialize the factory with specific sender instances.
     */
    public NotificationSenderFactory(SmsNotificationSenderImpl smsNotificationSender, EmailNotificationSenderImpl emailNotificationSender) {
        this.smsNotificationSender = smsNotificationSender;
        this.emailNotificationSender = emailNotificationSender;
    }

    /**
     * Returns the appropriate NotificationSender based on the notification type.
     */
    public NotificationSender getNotificationSender(NotificationType notificationType) {
        switch (notificationType) {
            case EMAIL:
                return emailNotificationSender;
            case SMS:
                return smsNotificationSender;
            default:
                throw new IllegalArgumentException("Unknown notification type");
        }
    }
}
