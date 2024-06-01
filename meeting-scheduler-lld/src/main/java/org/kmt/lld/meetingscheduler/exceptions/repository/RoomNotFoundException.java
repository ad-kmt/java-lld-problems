package org.kmt.lld.meetingscheduler.exceptions.repository;

public class RoomNotFoundException extends RuntimeException{

    public RoomNotFoundException(String message){
        super(message);
    }

    public RoomNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
