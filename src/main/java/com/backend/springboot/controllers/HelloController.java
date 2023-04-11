package com.backend.springboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.database.User;
import com.backend.database.UserRepository;

@RestController
public class HelloController {
	@Autowired
	private UserRepository userRepository;
	
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World")String name) {
        return String.format("Hello %s!", name);
    }
    
    @GetMapping("/authenticateUser")
    public String authenticateUser(@RequestParam(value = "name")String name, @RequestParam(value = "password")String password) {
    	Iterable<User> allUsers = userRepository.findAll();
    	
    	for (User u : allUsers) {
    		if (u.getEmail().equals(name) && u.getPasswordHash().equals(password)) {
    			System.out.println("Authenticated user " + name);
    			return "Authenticated";
    		}
    	}
    	
    	return null;
    }
    
    @GetMapping("/createUser")
    public String createUser(@RequestParam(value = "name")String name, @RequestParam(value = "password")String password) {
    	User u = new User();
    	u.setEmail(name);
    	u.setPasswordHash(password);
    	userRepository.save(u);
    	
    	System.out.println("Created user " + name);
    	
    	return "Created user " + name;
    }
}
