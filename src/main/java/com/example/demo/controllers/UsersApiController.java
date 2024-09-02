package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.UsersApi;
import com.example.demo.model.User;
import com.example.demo.services.UsersService;

@RestController
public class UsersApiController implements UsersApi {

    private final UsersService usersService;

    @Autowired
    public UsersApiController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<Void> createUsers(User user) {
        this.usersService.create(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<User>> listUsers(Integer limit) {
        List<User> users = this.usersService.getAllUsers(limit);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> showUserById(String userId) {
        User user = this.usersService.getUser(Long.parseLong(userId));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
