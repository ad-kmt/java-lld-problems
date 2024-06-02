package org.kmt.lld.meetingscheduler.repository;

import org.kmt.lld.meetingscheduler.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository class for managing users.
 */
public class UserRepository {
    // ConcurrentHashMap ensures thread-safe access to the user data.
    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public User create(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }
}
