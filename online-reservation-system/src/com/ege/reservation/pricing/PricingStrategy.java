package com.ege.reservation.pricing;

import com.ege.reservation.data.Seat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Pricing strategy interface (Strategy Pattern)
 */
public interface PricingStrategy {
    double calculatePrice(double basePrice, List<Seat.SeatType> seatTypes, LocalDateTime departureDate);
} 