package org.kmt.lld.meetingscheduler.exceptions.meetingscheduler;

public class MeetingSchedulerException extends RuntimeException{

    public MeetingSchedulerException(String message){
        super(message);
    }

    public MeetingSchedulerException(String message, Throwable cause){
        super(message, cause);
    }
}
