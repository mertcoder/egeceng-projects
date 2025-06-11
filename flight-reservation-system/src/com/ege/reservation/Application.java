package com.ege.reservation;

import com.ege.reservation.dao.DataInitializer;
import com.ege.reservation.ui.LoginScreen;
import com.ege.reservation.ui.UIFactory;

public class Application {
    private final UIFactory uiFactory;

    public Application(){
        DataInitializer.getInstance().initializeData();
        this.uiFactory = new UIFactory();
    }

    public void run(){
        LoginScreen loginScreen = uiFactory.createLoginScreen();
        loginScreen.display();

    }

}
