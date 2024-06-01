package org.kmt.lld.meetingscheduler.exceptions.meetingscheduler;

public class MeetingOverlapException extends MeetingSchedulerException {

    public MeetingOverlapException(String message){
        super(message);
    }

    public MeetingOverlapException(String message, Throwable cause){
        super(message, cause);
    }
}
