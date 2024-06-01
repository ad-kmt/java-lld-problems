package org.kmt.lld.meetingscheduler.exceptions.service;

public class MeetingSchedulerException extends RuntimeException{

    public MeetingSchedulerException(String message){
        super(message);
    }

    public MeetingSchedulerException(String message, Throwable cause){
        super(message, cause);
    }
}
