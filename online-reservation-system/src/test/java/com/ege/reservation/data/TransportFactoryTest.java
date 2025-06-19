package test.java.com.ege.reservation.data;

import com.ege.reservation.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
Unit tests for TransportFactory
 Testing Factory Pattern implementation
 */
class TransportFactoryTest {
    private TransportFactory transportFactory;
    @BeforeEach
    void setUp() {
        transportFactory = new TransportFactory();
    }

    @Test
    void testCreateTransport_Bus() {
        // Arrange
        TransportType type = TransportType.BUS;
        String id = "test-bus-1";
        String name = "Test Bus";
        String company = "Test Company";
        String number = "B123";

        // Act
        Transport result = transportFactory.createTransport(type, id, name, company, number);

        // Assert
        assertNotNull(result, "Created transport should not be null");
        assertInstanceOf(Bus.class, result, "Should create Bus instance");
        assertEquals(id, result.getTransportId(), "Transport ID should match");
        assertEquals(name, result.getTransportName(), "Transport name should match");
        assertEquals(TransportType.BUS, result.getTransportType(), "Transport type should be BUS");
    }

    @Test
    void testCreateTransport_Flight() {
        // Arrange
        TransportType type = TransportType.FLIGHT;
        String id = "test-flight-1";
        String name = "Test Flight";
        String company = "Test Airlines";
        String number = "TF456";

        // Act
        Transport result = transportFactory.createTransport(type, id, name, company, number);

        // Assert
        assertNotNull(result, "Created transport should not be null");
        assertInstanceOf(Flight.class, result, "Should create Flight instance");
        assertEquals(id, result.getTransportId(), "Transport ID should match");
        assertEquals(name, result.getTransportName(), "Transport name should match");
        assertEquals(TransportType.FLIGHT, result.getTransportType(), "Transport type should be FLIGHT");
    }

    @Test
    void testCreateBus_DirectMethod() {
        // Arrange
        String id = "bus-direct-1";
        String name = "Direct Bus";
        String company = "Direct Company";

        // Act
        Bus result = transportFactory.createBus(id, name, company);

        // Assert
        assertNotNull(result, "Created bus should not be null");
        assertEquals(id, result.getTransportId(), "Bus ID should match");
        assertEquals(name, result.getTransportName(), "Bus name should match");
        assertEquals(company, result.getBusCompany(), "Bus company should match");
        assertEquals(TransportType.BUS, result.getTransportType(), "Transport type should be BUS");
    }

    @Test
    void testCreateFlight_DirectMethod() {
        // Arrange
        String id = "flight-direct-1";
        String name = "Direct Flight";
        String airline = "Direct Airlines";
        String flightNumber = "DF789";

        // Act
        Flight result = transportFactory.createFlight(id, name, airline, flightNumber);

        // Assert
        assertNotNull(result, "Created flight should not be null");
        assertEquals(id, result.getTransportId(), "Flight ID should match");
        assertEquals(name, result.getTransportName(), "Flight name should match");
        assertEquals(airline, result.getAirline(), "Airline should match");
        assertEquals(flightNumber, result.getFlightNumber(), "Flight number should match");
        assertEquals(TransportType.FLIGHT, result.getTransportType(), "Transport type should be FLIGHT");
    }

}