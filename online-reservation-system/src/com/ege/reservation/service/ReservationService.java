package com.ege.reservation.service;

import com.ege.reservation.dao.DataStore;
import com.ege.reservation.data.Seat;
import com.ege.reservation.data.User;
import com.ege.reservation.data.Voyage;
import com.ege.reservation.pricing.PricingStrategy;
import com.ege.reservation.pricing.StandardPricingStrategy;
import com.ege.reservation.reservation.Reservation;
import com.ege.reservation.reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service facade for reservation operations (Facade Pattern)
 */
public class ReservationService {
    private final DataStore dataStore;
    private PricingStrategy pricingStrategy;
    
    public ReservationService() {
        this.dataStore = DataStore.getInstance();
        this.pricingStrategy = new StandardPricingStrategy(); // Default strategy
    }
    
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
    
    public List<Voyage> searchVoyages(String origin, String destination, LocalDateTime date) {
        return dataStore.getAllVoyages().stream()
                .filter(v -> v.getOrigin().equalsIgnoreCase(origin) && 
                             v.getDestination().equalsIgnoreCase(destination) && 
                             v.getDepartureTime().toLocalDate().equals(date.toLocalDate()))
                .collect(Collectors.toList());
    }
    
    public Voyage getVoyageById(String voyageId) {
        return dataStore.getVoyageById(voyageId);
    }
    
    public List<String> getAvailableSeats(String voyageId) {
        Voyage voyage = dataStore.getVoyageById(voyageId);
        if (voyage == null) {
            return new ArrayList<>();
        }
        
        List<String> reservedSeats = dataStore.getReservationsForVoyage(voyageId).stream()
                .flatMap(r -> r.getSeatIds().stream())
                .collect(Collectors.toList());
        
        return voyage.getTransport().getSeats().stream()
                .filter(seat -> !reservedSeats.contains(seat.getSeatId()))
                .map(Seat::getSeatId)
                .collect(Collectors.toList());
    }
    
    public boolean isSeatAvailable(String voyageId, String seatId) {
        Voyage voyage = dataStore.getVoyageById(voyageId);
        if (voyage == null) {
            return false;
        }
        
        // Check if seat is already reserved in another reservation
        return dataStore.getReservationsForVoyage(voyageId).stream()
                .flatMap(r -> r.getSeatIds().stream())
                .noneMatch(s -> s.equals(seatId));
    }
    
    public double calculatePrice(String voyageId, List<String> seatIds) {
        Voyage voyage = dataStore.getVoyageById(voyageId);
        if (voyage == null) {
            return 0.0;
        }
        
        List<Seat.SeatType> seatTypes = seatIds.stream()
                .map(seatId -> voyage.getTransport().getSeats().stream()
                        .filter(s -> s.getSeatId().equals(seatId))
                        .findFirst()
                        .map(Seat::getSeatType)
                        .orElse(Seat.SeatType.ECONOMY))
                .collect(Collectors.toList());
        
        return pricingStrategy.calculatePrice(voyage.getPrice(), seatTypes, voyage.getDepartureTime());
    }
    
    public Reservation createReservation(User user, String voyageId, List<String> seatIds) {
        Voyage voyage = dataStore.getVoyageById(voyageId);
        if (voyage == null) {
            return null;
        }
        
        // Check if seats are available
        for (String seatId : seatIds) {
            if (!isSeatAvailable(voyageId, seatId)) {
                return null; // Seat not available
            }
        }
        
        // Calculate price
        double totalPrice = calculatePrice(voyageId, seatIds);
        
        // Create reservation
        Reservation reservation = new Reservation.Builder(user, voyage)
                .withId("res-" + UUID.randomUUID().toString().substring(0, 8))
                .withSeatIds(seatIds)
                .withTotalPrice(totalPrice)
                .withReservationTime(LocalDateTime.now())
                .withStatus(ReservationStatus.CONFIRMED)
                .build();
        
        // Save reservation
        dataStore.addReservation(reservation);
        
        return reservation;
    }
    
    public boolean cancelReservation(String reservationId) {
        Reservation reservation = dataStore.getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        return true;
    }
    public List<Voyage> getAllVoyages() {
        return dataStore.getAllVoyages();
    }

    public List<Reservation> getUserReservations(String userId) {
        return dataStore.getReservationsForUser(userId);
    }
} 