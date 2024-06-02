package org.kmt.lld.meetingscheduler.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a meeting with a title, interval, room, organizer, and a list of invites.
 */
public class Meeting {
    private int id;
    private String title;
    private Interval interval;
    private Room room;
    private User organizer;
    private List<Invite> invites;

    public Meeting(String title, LocalDateTime startTime, LocalDateTime endTime, Room room, User organizer, User... participants) {
        this.title = title;
        this.interval = new Interval(startTime, endTime);
        this.room = room;
        this.organizer = organizer;
        this.invites = Arrays.stream(participants)
                .map(Invite::new)
                .toList();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Interval getInterval() {
        return interval;
    }

    public Room getRoom() {
        return room;
    }

    public User getOrganizer() {
        return organizer;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    @Override
    public String toString() {
        return "Meeting {" +
                " id: " + id +
                ", title: " + title +
                ", room id: " + room.getId() +
                ", start time: " + interval.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                ", end time: " + interval.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                " }";
    }




}

