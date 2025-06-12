package com.ege.reservation.reservation;

import com.ege.reservation.data.User;
import com.ege.reservation.data.Voyage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a reservation made by a user for a voyage
 */
public class Reservation {
    private final String id;
    private final User user;
    private final Voyage voyage;
    private final List<String> seatIds;
    private final LocalDateTime reservationTime;
    private final double totalPrice;
    private ReservationStatus status;
    
    // Builders will manage the construction (Builder Pattern)
    private Reservation(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.user = builder.user;
        this.voyage = builder.voyage;
        this.seatIds = new ArrayList<>(builder.seatIds);
        this.reservationTime = builder.reservationTime != null ? 
                builder.reservationTime : LocalDateTime.now();
        this.totalPrice = builder.totalPrice;
        this.status = builder.status != null ? builder.status : ReservationStatus.CONFIRMED;
    }
    
    public String getId() {
        return id;
    }
    
    public User getUser() {
        return user;
    }
    
    public Voyage getVoyage() {
        return voyage;
    }
    
    public List<String> getSeatIds() {
        return seatIds;
    }
    
    public LocalDateTime getReservationTime() {
        return reservationTime;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    // Builder Pattern
    public static class Builder {
        private String id;
        private User user;
        private Voyage voyage;
        private List<String> seatIds = new ArrayList<>();
        private LocalDateTime reservationTime;
        private double totalPrice;
        private ReservationStatus status;
        
        public Builder(User user, Voyage voyage) {
            this.user = user;
            this.voyage = voyage;
        }
        
        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        
        public Builder withSeatId(String seatId) {
            this.seatIds.add(seatId);
            return this;
        }
        
        public Builder withSeatIds(List<String> seatIds) {
            this.seatIds.addAll(seatIds);
            return this;
        }
        
        public Builder withReservationTime(LocalDateTime reservationTime) {
            this.reservationTime = reservationTime;
            return this;
        }
        
        public Builder withTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }
        
        public Builder withStatus(ReservationStatus status) {
            this.status = status;
            return this;
        }
        
        public Reservation build() {
            return new Reservation(this);
        }
    }
} 