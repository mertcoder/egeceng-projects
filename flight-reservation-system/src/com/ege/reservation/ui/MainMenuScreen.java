package com.ege.reservation.ui;

import com.ege.reservation.service.UserService;

public class MainMenuScreen extends Screen{
    private final UserService userService;

    public MainMenuScreen(UserService userService, UIFactory uiFactory){
        super(uiFactory);
        this.userService = userService;
    }

    @Override
    protected void renderContent() {

    }

    @Override
    protected void handleInput() {

    }
}
