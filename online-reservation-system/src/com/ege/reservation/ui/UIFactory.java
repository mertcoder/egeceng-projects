package com.ege.reservation.ui;

import com.ege.reservation.service.ReservationService;
import com.ege.reservation.service.UserService;
import com.ege.reservation.service.VoyageService;

public class UIFactory {

    public LoginScreen createLoginScreen(){
        return new LoginScreen(new UserService(),this);
    }

    public RegisterScreen createRegisterScreen(){
        return new RegisterScreen(new UserService(),this);
    }


    public UserDashboardScreen createUserDashboardScreen() {
        return new UserDashboardScreen(new UserService(), new ReservationService(), this);
    }

    public AdminDashboardScreen createAdminDashboardScreen() {
        return new AdminDashboardScreen(new UserService(), new VoyageService(), this);
    }

    public VoyageSearchScreen createVoyageSearchScreen() {
        return new VoyageSearchScreen(new ReservationService(), this);
    }


    public MainMenuScreen createMainMenuScreen(){
        return new MainMenuScreen(new UserService(), this);
    }

    public SeatSelectionScreen createSeatSelectionScreen(String voyageId) {
        return new SeatSelectionScreen(voyageId, new ReservationService(), this);
    }

    public ReservationConfirmationScreen createReservationConfirmationScreen(String voyageId, String[] selectedSeats) {
        return new ReservationConfirmationScreen(voyageId, selectedSeats, new ReservationService(), new UserService(), this);
    }

    public UserAccountScreen createUserAccountScreen() {
        return new UserAccountScreen(new UserService(), this);
    }


}
