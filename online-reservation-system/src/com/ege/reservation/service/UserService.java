package com.ege.reservation.service;

import com.ege.reservation.base.UserServiceBase;
import com.ege.reservation.dao.DataStore;
import com.ege.reservation.data.User;

import java.util.UUID;

public class UserService implements UserServiceBase {
    //TODO: handle com.ege.reservation.data storage on singleton
    private final DataStore dataStore;

    public UserService(){
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public User login(String username, String password){
        User user = dataStore.getUserByUsername(username);
        if(user!=null && user.getPassword().equals(password)){
            dataStore.setCurrentUser(user);
            return user;
        }
        return null;
    }

    @Override
    public void logout() {
        dataStore.setCurrentUser(null);
    }

    @Override
    public User getCurrentUser() {
        return dataStore.getCurrentUser();
    }

    @Override
    public User registerUser(String username, String password, String fullName, String email) {
        if(dataStore.getUserByUsername(username)!=null){ // username already exists?
            return null;
        }
        User newUser = new User("user-" + UUID.randomUUID().toString().substring(0,10),
                username,password,fullName,email,false);

        dataStore.addUser(newUser);
        return newUser;

    }

    @Override
    public boolean isAdmin() {
        return dataStore.getCurrentUser().isAdmin() && getCurrentUser()!=null;
    }

}
