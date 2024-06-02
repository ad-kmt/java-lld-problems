package org.kmt.lld.meetingscheduler.service;

import org.kmt.lld.meetingscheduler.models.Notification;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.utils.Logger;

import java.util.List;

/**
 * Service class for sending notifications to users.
 */
public class NotificationService {

    Logger log = Logger.getInstance();

    /**
     * Sends a notification to a single user with the specified message.
     */
    public void sendNotification(User user, String message) {
        Notification notification = new Notification(user, message);
        log.info("Sent notification: " + notification + " to user: " + user.getEmail());
    }

    /**
     * Sends notifications to a list of users with the specified message.
     */
    public void sendNotification(List<User> users, String message) {
        for (User user : users) {
            sendNotification(user, message);
        }
    }
}
