package com.backend.springboot.controllers;

import org.springframework.web.bind.annotation.*;
import com.backend.springboot.handlers.UserHandler;

@RestController
public class UserController {

    @GetMapping("/getUser")
    public String getUser(@RequestParam(value = "name", defaultValue = "*")String name) {
        UserHandler response = new UserHandler();
        return String.format(response.getUser(name));
    }

    @GetMapping("/createUser")
    public String createUser(@RequestParam(value = "name") String userName, @RequestParam(value = "password") String hashedPassword) {
        UserHandler response = new UserHandler();
        return String.format(response.createUser(userName, hashedPassword));
    }

    @GetMapping("/updateUserPassword")
    public String updateUserPassword(@RequestParam(value = "name")String userName, @RequestParam(value = "password")String newHashedPassword) {
        UserHandler response = new UserHandler();
        return String.format(response.createUser(userName, newHashedPassword));
    }

}
