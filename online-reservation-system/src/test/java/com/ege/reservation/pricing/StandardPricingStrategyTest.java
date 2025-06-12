package test.java.com.ege.reservation.pricing;

import com.ege.reservation.data.Seat;
import com.ege.reservation.pricing.StandardPricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 Unit tests for StandardPricingStrategy
 Testing Strategy Pattern implementation
 */
class StandardPricingStrategyTest {

    private StandardPricingStrategy pricingStrategy;

    @BeforeEach
    void setUp() {
        pricingStrategy = new StandardPricingStrategy();
    }

    @Test
    void testCalculatePrice_EconomySeats() {
        // Arrange
        double basePrice = 100.0;
        List<Seat.SeatType> seatTypes = Arrays.asList(
            Seat.SeatType.ECONOMY, 
            Seat.SeatType.ECONOMY
        );
        LocalDateTime departureDate = LocalDateTime.now().plusDays(5);

        // Act
        double result = pricingStrategy.calculatePrice(basePrice, seatTypes, departureDate);

        // Assert
        assertEquals(100.0, result, 0.01, "Economy seats should not have additional cost");
    }

    @Test
    void testCalculatePrice_BusinessSeats() {
        // Arrange
        double basePrice = 100.0;
        List<Seat.SeatType> seatTypes = Arrays.asList(
            Seat.SeatType.BUSINESS, 
            Seat.SeatType.BUSINESS
        );
        LocalDateTime departureDate = LocalDateTime.now().plusDays(5);

        // Act
        double result = pricingStrategy.calculatePrice(basePrice, seatTypes, departureDate);

        // Assert
        assertEquals(200.0, result, 0.01, "Business seats should add 50% premium for each seat");
    }

    @Test
    void testCalculatePrice_MixedSeats() {
        // Arrange
        double basePrice = 100.0;
        List<Seat.SeatType> seatTypes = Arrays.asList(
            Seat.SeatType.ECONOMY, 
            Seat.SeatType.BUSINESS
        );
        LocalDateTime departureDate = LocalDateTime.now().plusDays(5);

        // Act
        double result = pricingStrategy.calculatePrice(basePrice, seatTypes, departureDate);

        // Assert
        assertEquals(150.0, result, 0.01, "Mixed seats should add premium only for business seats");
    }

    @Test
    void testCalculatePrice_EmptySeats() {
        // Arrange
        double basePrice = 100.0;
        List<Seat.SeatType> seatTypes = List.of();
        LocalDateTime departureDate = LocalDateTime.now().plusDays(5);

        // Act
        double result = pricingStrategy.calculatePrice(basePrice, seatTypes, departureDate);

        // Assert
        assertEquals(100.0, result, 0.01, "Empty seat list should return base price");
    }
} 