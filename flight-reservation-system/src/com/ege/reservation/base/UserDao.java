package com.ege.reservation.base;

import com.ege.reservation.data.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);
    User getUserById(String userId);
    User getUserByUsername(String username);
    public List<User> getAllUsers();
    //Current user
    public User getCurrentUser();
    public void setCurrentUser(User currentUser);


}
