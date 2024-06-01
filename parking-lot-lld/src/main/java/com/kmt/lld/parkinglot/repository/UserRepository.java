package com.kmt.lld.parkinglot.repository;

import com.kmt.lld.parkinglot.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    Map<String, User> users;

    public UserRepository(){
        users = new HashMap<>();
    }

    public User insert(User user) throws Exception {
        String username = user.getUsername();
        if(!users.containsKey(username)){
            users.put(username, user);
            return user;
        } else {
            throw new Exception("User already exists.");
        }
    }

    public User update(User user) throws Exception {
        String username = user.getUsername();
        if(users.containsKey(username)){
            users.put(username, user);
            return user;
        }else{
            throw new Exception("User doesn't exist.");
        }
    }
}
