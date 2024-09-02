package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;

@Service
public class UsersService {
    private final Map<Long, User> users = new java.util.HashMap<>();

    public void create(User user) {
        long id = users.size() + 1;
        user.setId(id);
        users.put(id, user);
    }

    public List<User> getAllUsers(int limit) {
        return users.values().stream().limit(limit).toList();
    }

    public User getUser(Long id) {
        return users.get(id);
    }
}
