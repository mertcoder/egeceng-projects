package com.ege.reservation.base;

import com.ege.reservation.data.User;

public interface UserServiceBase {
    User login(String username, String password);
    void logout();
    User getCurrentUser();
    User registerUser(String username, String password, String fullName, String email);
    boolean isAdmin();
}
