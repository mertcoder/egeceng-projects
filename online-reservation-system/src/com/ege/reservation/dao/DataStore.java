package com.ege.reservation.dao;

import com.ege.reservation.base.UserDao;
import com.ege.reservation.data.Transport;
import com.ege.reservation.data.User;
import com.ege.reservation.data.Voyage;
import com.ege.reservation.reservation.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore implements UserDao {
    //Singleton. Data store for applciation com.ege.reservation.data, which is Pattern: Singleton

    private static DataStore instance;

    // Data, com.ege.reservation.data, com.ege.reservation.data
    private final Map<String, User> users;
    private final Map<String, Transport> transports;
    private final Map<String, Voyage> voyages;
    private final Map<String, Reservation> reservations;


    private User currentUser;

    private DataStore(){
        users = new HashMap<>();
        transports = new HashMap<>();
        voyages = new HashMap<>();
        reservations = new HashMap<>();

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

    // Transport methods
    public void addTransport(Transport transport) {
        transports.put(transport.getTransportId(), transport);
    }

    public Transport getTransportById(String transportId) {
        return transports.get(transportId);
    }

    public List<Transport> getAllTransports() {
        return new ArrayList<>(transports.values());
    }



    // Voyage methods
    public void addVoyage(Voyage voyage) {
        voyages.put(voyage.getId(), voyage);
    }

    public void removeVoyage(String voyageId) {
        voyages.remove(voyageId);
    }

    public Voyage getVoyageById(String voyageId) {
        return voyages.get(voyageId);
    }

    public List<Voyage> getAllVoyages() {
        return new ArrayList<>(voyages.values());
    }



    // Reservation methods
    public void addReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }

    public void removeReservation(String reservationId) {
        reservations.remove(reservationId);
    }

    public Reservation getReservationById(String reservationId) {
        return reservations.get(reservationId);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> getReservationsForUser(String userId) {
        return reservations.values().stream()
                .filter(r -> r.getUser().getUserId().equals(userId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Reservation> getReservationsForVoyage(String voyageId) {
        return reservations.values().stream()
                .filter(r -> r.getVoyage().getId().equals(voyageId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

}
