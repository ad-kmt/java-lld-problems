package org.kmt.lld.meetingscheduler.service.notification.sender;

import org.kmt.lld.meetingscheduler.models.notification.Notification;

/**
 * Interface for sending notifications.
 */
public interface NotificationSender {

    /**
     * Sends a notification.
     */
    void send(Notification notification);
}