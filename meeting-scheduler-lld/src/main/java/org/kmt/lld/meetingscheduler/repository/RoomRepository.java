package org.kmt.lld.meetingscheduler.repository;

import org.kmt.lld.meetingscheduler.exceptions.repository.MeetingNotFoundException;
import org.kmt.lld.meetingscheduler.exceptions.repository.RoomNotFoundException;
import org.kmt.lld.meetingscheduler.models.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository class for managing rooms.
 */
public class RoomRepository {
    private final Map<Integer, Room> rooms = new HashMap<>();
    private int counter = 1;

    public Room create(Room room) {
        room.setId(counter++);
        rooms.put(room.getId(), room);
        return room;
    }

    public List<Room> getAllRooms() {
        return rooms.values().stream().toList();
    }

    public Room getRoomById(int roomId) {
        if (!rooms.containsKey(roomId)) {
            throw new RoomNotFoundException("Room not found");
        }
        return rooms.get(roomId);
    }
}
