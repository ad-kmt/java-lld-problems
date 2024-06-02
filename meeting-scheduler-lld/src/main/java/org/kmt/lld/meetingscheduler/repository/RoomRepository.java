package org.kmt.lld.meetingscheduler.repository;

import org.kmt.lld.meetingscheduler.exceptions.repository.MeetingNotFoundException;
import org.kmt.lld.meetingscheduler.exceptions.repository.RoomNotFoundException;
import org.kmt.lld.meetingscheduler.models.Room;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Repository class for managing rooms with thread-safe operations.
 */
public class RoomRepository {
    // ConcurrentHashMap to ensure thread-safe access to rooms.
    private final ConcurrentHashMap<Integer, Room> rooms = new ConcurrentHashMap<>();
    // AtomicInteger to ensure atomic increments for room IDs.
    private final AtomicInteger counter = new AtomicInteger(1);

    /**
     * Creates a new room with a unique ID and stores it in the repository.
     */
    public Room create(Room room) {
        // Generate a unique ID for the room in a thread-safe manner.
        int id = counter.getAndIncrement();
        room.setId(id);
        rooms.put(id, room);
        return room;
    }

    /**
     * Retrieves all rooms from the repository.
     */
    public List<Room> getAllRooms() {
        return rooms.values().stream().toList();
    }

    /**
     * Retrieves a room by its ID.
     */
    public Room getRoomById(int roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new RoomNotFoundException("Room not found");
        }
        return room;
    }
}
