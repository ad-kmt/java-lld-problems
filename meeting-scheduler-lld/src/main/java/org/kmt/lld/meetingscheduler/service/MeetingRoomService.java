package org.kmt.lld.meetingscheduler.service;

import org.kmt.lld.meetingscheduler.models.Room;
import org.kmt.lld.meetingscheduler.repository.RoomRepository;

/**
 * Service class for managing meeting rooms.
 */
public class MeetingRoomService {
    private final RoomRepository roomRepository;

    public MeetingRoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room create(Room room) {
        return roomRepository.create(room);
    }
}
