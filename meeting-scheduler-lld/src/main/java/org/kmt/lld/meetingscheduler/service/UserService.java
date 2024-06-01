package org.kmt.lld.meetingscheduler.service;

import org.kmt.lld.meetingscheduler.models.User;
import org.kmt.lld.meetingscheduler.repository.UserRepository;

/**
 * Service class for managing users.
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.create(user);
    }
}
