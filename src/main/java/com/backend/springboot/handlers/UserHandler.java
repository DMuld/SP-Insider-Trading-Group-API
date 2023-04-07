package com.backend.springboot.handlers;

public class UserHandler {

    public String getUser(String input) {
        //Make a function call to the DB to get the list of users.
        //Possibly return as a comma delimited list.
        return "<Need to return users>";
    }

    public String createUser(String userName, String hashedPassword) {
        //Need to create user.
        return "User Created";
    }

    public String updateUser(String userName, String newHashedPassword) {
        //Need to make it take user input.
        return "User Password Changed";
    }

}
