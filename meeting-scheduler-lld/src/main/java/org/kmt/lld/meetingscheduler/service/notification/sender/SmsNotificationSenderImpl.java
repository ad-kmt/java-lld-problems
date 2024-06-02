package org.kmt.lld.meetingscheduler.service.notification.sender;

import org.kmt.lld.meetingscheduler.models.notification.Notification;
import org.kmt.lld.meetingscheduler.utils.Logger;

public class SmsNotificationSenderImpl implements NotificationSender {
    Logger log = Logger.getInstance();

    @Override
    public void send(Notification notification) {
        log.info("Sent sms: " + notification.getMessage() + " to user: " + notification.getRecipient().getName());
    }
}
