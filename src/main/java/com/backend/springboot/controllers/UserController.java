package com.backend.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.backend.springboot.database.UserRepository;
import com.backend.springboot.handlers.UserHandler;

@RestController
public class UserController {
	@Autowired
    private UserRepository userRepository;
	

    @GetMapping("/getUser")
    public String getUser(@RequestParam(value = "name", defaultValue = "*")String name) {
        UserHandler response = new UserHandler();
        return String.format(response.getUser(name, userRepository));
    }
    
    @GetMapping("/authenticateUser")
    public String authenticateUser(@RequestParam(value = "name")String name, @RequestParam(value = "password")String password) {
    	UserHandler response = new UserHandler();
    	return String.format(response.authenticateUser(name, password, userRepository));
    }

    @GetMapping("/createUser")
    public String createUser(@RequestParam(value = "name") String userName, @RequestParam(value = "password") String hashedPassword) {
    	UserHandler response = new UserHandler();
    	return String.format(response.createUser(userName, hashedPassword, userRepository));
    }

    @GetMapping("/updateUserPassword")
    public String updateUserPassword(@RequestParam(value = "name")String userName, @RequestParam(value = "password")String newHashedPassword) {
        UserHandler response = new UserHandler();
        return String.format(response.createUser(userName, newHashedPassword, userRepository));
    }

    @GetMapping("/updateFavorites")
    public String updateFavorites(@RequestParam(value = "name")String userName, @RequestParam(value = "favorites")String favorites) {
    	UserHandler response = new UserHandler();
    	return String.format(response.updateFavorites(userName, favorites, userRepository));
    }
}
