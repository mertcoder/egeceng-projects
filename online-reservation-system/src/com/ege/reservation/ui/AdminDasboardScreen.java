package com.ege.reservation.ui;

import com.ege.reservation.service.UserService;

public class AdminDasboardScreen  extends Screen{
    private final UserService userService;

    public AdminDasboardScreen(UserService userService, UIFactory uiFactory){
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
