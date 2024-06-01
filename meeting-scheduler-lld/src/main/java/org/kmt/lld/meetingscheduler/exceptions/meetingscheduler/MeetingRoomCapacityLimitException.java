package org.kmt.lld.meetingscheduler.exceptions.meetingscheduler;

public class MeetingRoomCapacityLimitException extends MeetingSchedulerException {

    public MeetingRoomCapacityLimitException(String message){
        super(message);
    }

    public MeetingRoomCapacityLimitException(String message, Throwable cause){
        super(message, cause);
    }
}
