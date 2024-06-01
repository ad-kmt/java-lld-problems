package org.kmt.lld.meetingscheduler.exceptions.service;

public class ParticipantNotFoundException extends RuntimeException{

    public ParticipantNotFoundException(String message){
        super(message);
    }

    public ParticipantNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
