package com.ege.reservation.pricing;


import com.ege.reservation.data.Seat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Dynamic pricing strategy that adjusts price based on time to departure (Strategy Pattern)
 */
public class DynamicPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, List<Seat.SeatType> seatTypes, LocalDateTime departureDate) {
        double totalPrice = basePrice;
        
        // Apply price adjustments based on seat types (same as StandardPricingStrategy)
        for (Seat.SeatType seatType : seatTypes) {
            if (seatType == Seat.SeatType.BUSINESS) {
                totalPrice += basePrice * 0.5;
            }
        }
        
        // Apply dynamic pricing based on days until departure
        LocalDateTime now = LocalDateTime.now();
        long daysUntilDeparture = ChronoUnit.DAYS.between(now, departureDate);
        
        if (daysUntilDeparture <= 1) {
            // Last minute bookings are expensive (50% premium)
            totalPrice *= 1.5;
        } else if (daysUntilDeparture <= 7) {
            // Less than a week before departure (20% premium)
            totalPrice *= 1.2;
        } else if (daysUntilDeparture >= 30) {
            // Early bookings get a discount (10% off)
            totalPrice *= 0.9;
        }
        
        return totalPrice;
    }
}