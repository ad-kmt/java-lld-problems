package org.kmt.lld.meetingscheduler.exceptions;

public class MeetingNotFoundException extends RuntimeException{

    public MeetingNotFoundException(String message){
        super(message);
    }

    public MeetingNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
