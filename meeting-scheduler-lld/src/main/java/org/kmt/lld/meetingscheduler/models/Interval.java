package org.kmt.lld.meetingscheduler.models;

import java.time.LocalDateTime;

/**
 * Represents a time interval with a start time and an end time.
 */
public class Interval {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructs an Interval with the specified start time and end time.
     *
     * @param startTime the start time of the interval
     * @param endTime the end time of the interval
     * @throws IllegalArgumentException if the end time is before the start time
     */
    public Interval(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
