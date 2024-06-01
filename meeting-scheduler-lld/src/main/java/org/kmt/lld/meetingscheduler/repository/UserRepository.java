package org.kmt.lld.meetingscheduler.repository;

import org.kmt.lld.meetingscheduler.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository class for managing users.
 */
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public User create(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }
}
