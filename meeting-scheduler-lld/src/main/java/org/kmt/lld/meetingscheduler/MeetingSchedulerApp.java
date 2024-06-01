package org.kmt.lld.meetingscheduler;

import org.kmt.lld.meetingscheduler.exceptions.meetingscheduler.MeetingSchedulerException;
import org.kmt.lld.meetingscheduler.models.Interval;
import org.kmt.lld.meetingscheduler.models.Meeting;
import org.kmt.lld.meetingscheduler.models.Room;
import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.repository.MeetingRepository;
import org.kmt.lld.meetingscheduler.repository.RoomRepository;
import org.kmt.lld.meetingscheduler.repository.UserRepository;
import org.kmt.lld.meetingscheduler.service.MeetingRoomService;
import org.kmt.lld.meetingscheduler.service.MeetingSchedulerService;
import org.kmt.lld.meetingscheduler.service.NotificationService;
import org.kmt.lld.meetingscheduler.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Main application class for the Meeting Scheduler.
 */
public class MeetingSchedulerApp {
    public static void main(String[] args) {
        // Initialize repositories
        RoomRepository roomRepository = new RoomRepository();
        MeetingRepository meetingRepository = new MeetingRepository();
        UserRepository userRepository = new UserRepository();

        // Initialize services
        NotificationService notificationService = new NotificationService();
        MeetingRoomService meetingRoomService = new MeetingRoomService(roomRepository);
        MeetingSchedulerService meetingSchedulerService = new MeetingSchedulerService(meetingRepository, roomRepository, notificationService);
        UserService userService = new UserService(userRepository);

        System.out.println("Meeting Scheduler App Started!");

        // Initialize rooms
        Room room1 = meetingRoomService.create(new Room("Room 1", 5));
        Room room2 = meetingRoomService.create(new Room("Room 2", 10));
        System.out.println("Initialized Rooms");

        // Initialize users
        User user1 = userService.create(new User("User 1", "user1@mail.com"));
        User user2 = userService.create(new User("User 2", "user2@mail.com"));
        User user3 = userService.create(new User("User 3", "user3@mail.com"));
        User user4 = userService.create(new User("User 4", "user4@mail.com"));
        User user5 = userService.create(new User("User 5", "user5@mail.com"));
        User user6 = userService.create(new User("User 6", "user6@mail.com"));
        User user7 = userService.create(new User("User 7", "user7@mail.com"));
        User user8 = userService.create(new User("User 8", "user8@mail.com"));
        System.out.println("Initialized Users");

        // Schedule a meeting
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 5, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 5, 12, 30);
        Meeting meeting = meetingSchedulerService.scheduleMeeting(new Meeting("Meeting 1", startTime, endTime, room1, user1, user2, user3, user4));

        // Respond to invitation
        meetingSchedulerService.respondToInvitation(user2, Meeting.InviteResponse.ACCEPTED, meeting);
        System.out.println("Meeting invitation status: " + meeting.getInvites());

        // Attempt to schedule another meeting at the same time in the same room
        try {
            Meeting meeting2 = meetingSchedulerService.scheduleMeeting(new Meeting("Meeting 2", startTime, endTime, room1, user1, user2, user3, user4));
        } catch (MeetingSchedulerException e) {
            System.err.println(e.getMessage());
        }

        // Get available rooms for the specified interval
        List<Room> availableRooms = meetingSchedulerService.getAvailableRooms(new Interval(startTime, endTime));
        System.out.println("Available Rooms: " + availableRooms);
    }
}
