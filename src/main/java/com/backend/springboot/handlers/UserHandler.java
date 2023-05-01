package com.backend.springboot.handlers;

import com.backend.springboot.database.User;
import com.backend.springboot.database.UserRepository;

public class UserHandler {
	
	private User getUserObj(String input, UserRepository userRepository) {
		Iterable<User> allUsers = userRepository.findAll();
		
		for (User u : allUsers) {
			if (u.getEmail().equals(input)) {
				return u;
			}
		}
		
		return null;
	}

    public String getUser(String input, UserRepository userRepository) {
    	String result = "";
    	Iterable<User> allUsers = userRepository.findAll();
        
        if (input.equals("*")) {
        	for (User u : allUsers) {
        		String favorites;
        		if (u.getFavorites() == null) {
        			favorites = "";
        		}
        		else {
        			favorites = "!" + u.getFavorites();
        		}
        		result += u.getEmail() + favorites + ",";
        	}
        	
        	return result;
        }
        
        User u = getUserObj(input, userRepository);
        String favorites;
		if (u.getFavorites() == null) {
			favorites = "";
		}
		else {
			favorites = "!" + u.getFavorites();
		}
        
        return u.getEmail() + favorites;
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
    
    public String updateFavorites(String userName, String favorites, UserRepository userRepository) {
    	User user = getUserObj(userName, userRepository);
    	
    	user.setFavorites(favorites);
    	
    	userRepository.save(user);
    	
    	return "Updated favorites";
    }

    public String updateUser(String userName, String newHashedPassword, UserRepository userRepository) {
    	User user = getUserObj(userName, userRepository);
    	
    	user.setPasswordHash(newHashedPassword);
    	
    	userRepository.save(user);
    	
        return "User Password Changed";
    }

}
