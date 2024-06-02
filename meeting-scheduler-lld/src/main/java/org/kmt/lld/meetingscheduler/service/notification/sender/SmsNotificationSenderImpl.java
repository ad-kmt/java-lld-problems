package org.kmt.lld.meetingscheduler.service.notification.sender;

import org.kmt.lld.meetingscheduler.models.Notification;
import org.kmt.lld.meetingscheduler.utils.Logger;

/**
 * Implementation of NotificationSender for sending SMS notifications.
 */
public class SmsNotificationSenderImpl implements NotificationSender {
    // Singleton instance of Logger for logging information
    Logger log = Logger.getInstance();

    /**
     * Sends an SMS notification.
     */
    @Override
    public void send(Notification notification) {
        log.info(String.format("Sent SMS to user - %s: [%s]", notification.getRecipient().getName(), notification.getMessage()));
    }
}
