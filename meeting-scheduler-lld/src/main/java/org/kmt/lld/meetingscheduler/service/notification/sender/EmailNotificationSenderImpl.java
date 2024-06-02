package org.kmt.lld.meetingscheduler.service.notification.sender;

import org.kmt.lld.meetingscheduler.models.Notification;
import org.kmt.lld.meetingscheduler.utils.Logger;

/**
 * Implementation of NotificationSender for sending email notifications.
 */
public class EmailNotificationSenderImpl implements NotificationSender {

    // Singleton instance of Logger for logging information
    Logger log = Logger.getInstance();

    /**
     * Sends an email notification.
     */
    @Override
    public void send(Notification notification) {
        // Log the message indicating that an email has been sent
        log.info(String.format("Sent Email to user - %s: [%s]", notification.getRecipient().getEmail(), notification.getMessage()));
    }
}
