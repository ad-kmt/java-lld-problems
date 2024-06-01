package org.kmt.lld.meetingscheduler.models;

/**
 * Represents a meeting room with an ID, capacity, and name.
 */
public class Room {
    private int id;
    private final int capacity;
    private final String name;

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    /**
     * Sets the ID of the room.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Room {" +
                " id: " + id +
                ", name: '" + name + '\'' +
                ", capacity: " + capacity +
                " }";
    }
}
