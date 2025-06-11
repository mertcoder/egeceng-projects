package com.ege.reservation.ui;

import com.ege.reservation.service.UserService;

public class UIFactory {

    public LoginScreen createLoginScreen(){
        return new LoginScreen(new UserService(),this);
    }

    public RegisterScreen createRegisterScreen(){
        return new RegisterScreen(new UserService(),this);
    }

    public AdminDasboardScreen createAdminDashboardScreen(){
        return new AdminDasboardScreen(new UserService(), this);
    }

    public MainMenuScreen createMainMenuScreen(){
        return new MainMenuScreen(new UserService(), this);
    }

}
