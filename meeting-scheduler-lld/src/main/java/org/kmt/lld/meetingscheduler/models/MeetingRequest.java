package org.kmt.lld.meetingscheduler.models;

import java.time.LocalDate;
import java.util.List;

public class MeetingRequest {
    String title;
    User organizer;
    List<User> participants;
    double duration;
    LocalDate date;

    public MeetingRequest(String title, User organizer, List<User> participants, double duration, LocalDate date) {
        this.title = title;
        this.organizer = organizer;
        this.participants = participants;
        this.duration = duration;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public User getOrganizer() {
        return organizer;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public double getDuration() {
        return duration;
    }

    public LocalDate getDate() {
        return date;
    }
}
