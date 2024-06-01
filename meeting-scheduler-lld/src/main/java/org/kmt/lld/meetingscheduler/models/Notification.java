package org.kmt.lld.meetingscheduler.models;

import java.time.LocalDateTime;

/**
 * Represents a notification sent to a user.
 */
public class Notification {
    private final User recipient;
    private final String message;
    private final LocalDateTime timestamp;

    public Notification(User recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public User getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Notification { " +
                "recipient: " + recipient.getName() +
                ", message: '" + message + '\'' +
                ", timestamp: " + timestamp +
                " }";
    }
}
