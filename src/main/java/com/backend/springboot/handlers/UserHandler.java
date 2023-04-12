package com.backend.springboot.handlers;

import com.backend.springboot.database.User;
import com.backend.springboot.database.UserRepository;

public class UserHandler {

    public String getUser(String input, UserRepository userRepository) {
        //Make a function call to the DB to get the list of users.
        //Possibly return as a comma delimited list.
        return "<Need to return users>";
    }
    
    public String authenticateUser(String userName, String hashedPassword, UserRepository userRepository) {
    	Iterable<User> allUsers = userRepository.findAll();
    	
    	for (User u : allUsers) {
    		if (u.getEmail().equals(userName) && u.getPasswordHash().equals(hashedPassword)) {
    			System.out.println("Authenticated user " + userName);
    			return "Authenticated";
    		}
    	}
    	System.out.println("User not found");
    	return "";
    }

    public String createUser(String userName, String hashedPassword, UserRepository userRepository) {
    	// TODO: Check to make sure user doesn't already exist
    	User u = new User();
    	u.setEmail(userName);
    	u.setPasswordHash(hashedPassword);
    	userRepository.save(u);
    	
    	System.out.println("Created user " + userName);
    	
    	return "Created user";
    }

    public String updateUser(String userName, String newHashedPassword, UserRepository userRepository) {
        //Need to make it take user input.
        return "User Password Changed";
    }

}
