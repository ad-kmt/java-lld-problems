package org.kmt.lld.meetingscheduler;

import org.kmt.lld.meetingscheduler.exceptions.service.MeetingSchedulerException;
import org.kmt.lld.meetingscheduler.models.*;
import org.kmt.lld.meetingscheduler.models.enums.InviteResponse;
import org.kmt.lld.meetingscheduler.repository.MeetingRepository;
import org.kmt.lld.meetingscheduler.repository.RoomRepository;
import org.kmt.lld.meetingscheduler.repository.UserRepository;
import org.kmt.lld.meetingscheduler.service.meeting.observer.MeetingNotificationService;
import org.kmt.lld.meetingscheduler.service.meeting.MeetingRoomService;
import org.kmt.lld.meetingscheduler.service.meeting.MeetingSchedulerService;
import org.kmt.lld.meetingscheduler.service.meeting.strategy.FindMeetingStrategy;
import org.kmt.lld.meetingscheduler.service.meeting.strategy.FirstAvailableSlotStrategy;
import org.kmt.lld.meetingscheduler.service.notification.NotificationService;
import org.kmt.lld.meetingscheduler.service.UserService;
import org.kmt.lld.meetingscheduler.service.notification.sender.EmailNotificationSenderImpl;
import org.kmt.lld.meetingscheduler.service.notification.sender.NotificationSenderFactory;
import org.kmt.lld.meetingscheduler.service.notification.sender.SmsNotificationSenderImpl;
import org.kmt.lld.meetingscheduler.utils.Logger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Main application class for the Meeting Scheduler.
 */
public class MeetingSchedulerApp {
    public static void main(String[] args) {

        Logger log = Logger.getInstance();

        // Initialize repositories
        RoomRepository roomRepository = new RoomRepository();
        MeetingRepository meetingRepository = new MeetingRepository();
        UserRepository userRepository = new UserRepository();

        // Initialize services
        NotificationSenderFactory notificationSenderFactory = new NotificationSenderFactory(new SmsNotificationSenderImpl(), new EmailNotificationSenderImpl());
        NotificationService notificationService = new NotificationService(notificationSenderFactory);
        MeetingRoomService meetingRoomService = new MeetingRoomService(roomRepository);
        FindMeetingStrategy findMeetingStrategy = new FirstAvailableSlotStrategy(meetingRepository, roomRepository);
        MeetingNotificationService meetingNotificationService = new MeetingNotificationService(notificationService);
        MeetingSchedulerService meetingSchedulerService = new MeetingSchedulerService(meetingRepository, roomRepository, meetingNotificationService);
        UserService userService = new UserService(userRepository);

        log.info("Meeting Scheduler App Started!");

        // Initialize rooms
        Room room1 = meetingRoomService.create(new Room("Room 1", 5));
        Room room2 = meetingRoomService.create(new Room("Room 2", 10));
        log.info("Initialized Rooms");

        // Initialize users
        User user1 = userService.create(new User("User 1", "user1@mail.com"));
        User user2 = userService.create(new User("User 2", "user2@mail.com"));
        User user3 = userService.create(new User("User 3", "user3@mail.com"));
        User user4 = userService.create(new User("User 4", "user4@mail.com"));
        User user5 = userService.create(new User("User 5", "user5@mail.com"));
        User user6 = userService.create(new User("User 6", "user6@mail.com"));
        User user7 = userService.create(new User("User 7", "user7@mail.com"));
        User user8 = userService.create(new User("User 8", "user8@mail.com"));
        log.info("Initialized Users");

        // Schedule a meeting
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 5, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 5, 12, 30);

        Meeting meeting1 = meetingSchedulerService.scheduleMeeting(new Meeting("Meeting 1", startTime, endTime, room1, user1, user2, user3, user4));

        // Respond to invitation
        meetingSchedulerService.respondToInvitation(user2, InviteResponse.ACCEPTED, meeting1.getId());
        log.info("Meeting invitation status: " + meeting1.getInvites());

        // Attempt to schedule another meeting at the same time in the same room
        try {
            Meeting meeting2 = meetingSchedulerService.scheduleMeeting(new Meeting("Meeting 2", startTime, endTime, room1, user1, user2, user3, user4));
        } catch (MeetingSchedulerException e) {
            log.error(e.getMessage());
        }

        // Find meeting slot using a findMeetingStrategy and Schedule meeting
        Meeting meeting3 = findMeetingStrategy.findMeeting(new MeetingRequest("Meeting 3", user8, List.of(user7, user6), 1, null));
        meetingSchedulerService.scheduleMeeting(meeting3);

        // Get available rooms for the specified interval
        List<Room> availableRooms = meetingSchedulerService.getAvailableRooms(new Interval(startTime, endTime));
        log.info("Available Rooms: " + availableRooms);



    }
}
