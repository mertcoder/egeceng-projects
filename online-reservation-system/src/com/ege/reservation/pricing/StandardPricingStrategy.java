package com.ege.reservation.pricing;


import com.ege.reservation.data.Seat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard pricing strategy implementation (Strategy Pattern)
 */
public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, List<Seat.SeatType> seatTypes, LocalDateTime departureDate) {
        double totalPrice = basePrice;
        
        // Apply price adjustments based on seat types
        for (Seat.SeatType seatType : seatTypes) {
            // No adjustment for regular seats
            if (seatType == Seat.SeatType.BUSINESS) {
                totalPrice += basePrice * 0.5; // 50% premium for business class
            }
        }
        
        return totalPrice;
    }
} 