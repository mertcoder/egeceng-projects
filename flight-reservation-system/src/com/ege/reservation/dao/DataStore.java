package com.ege.reservation.dao;

import com.ege.reservation.base.UserDao;
import com.ege.reservation.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore implements UserDao {
    //Singleton. Data store for applciation com.ege.reservation.data, which is Pattern: Singleton

    private static DataStore instance;

    // Data, com.ege.reservation.data, com.ege.reservation.data
    private final Map<String, User> users;
    // TODO: ADD TRANSPORTS VOYAGES RES'


    private User currentUser;

    private DataStore(){
        users = new HashMap<>();
        // TODO: ADD TRANSPORTS VOYAGES RES'

    }

    public static synchronized DataStore getInstance(){
        if(instance==null){
            instance = new DataStore();
        }
        return instance;
    }


    @Override
    public void addUser(User user) {
        users.put(user.getUserId(),user);
    }

    @Override
    public User getUserById(String userId) {
        return users.get(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return users.values().stream()
                .filter(user-> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User currentUser) {
        this.currentUser  = currentUser;
    }
}
