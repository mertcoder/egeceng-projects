package com.ege.reservation.data;

/**
 * Concrete implementation of Transport for Bus (Factory Method pattern)
 */
public class Bus extends Transport {
    private final String busCompany;

    public Bus(String id, String name, String busCompany) {
        super(id, name, 40); // Standard bus with 40 seats
        this.busCompany = busCompany;
    }

    @Override
    protected void initializeSeats() {
        // Initialize bus seats with 13 rows of 3 seats (2+1 configuration) plus front row
        // Row 1: 2 seats (driver row)
        // Rows 2-14: 3 seats each (2+1 configuration)
        String[] leftColumns = {"A", "B"}; // Left side (2 seats)
        String[] rightColumns = {"C"}; // Right side (1 seat)

        // Row 1 (front row - only 2 seats)
        for (String column : leftColumns) {
            Seat.SeatType type = Seat.SeatType.BUSINESS; // Front row seats are business class

            String seatId = "1" + column;
            seats.add(new Seat(seatId, 1, column, type));
        }

        // Rows 2-14 (regular rows with 3 seats each)
        for (int row = 2; row <= 14; row++) {
            // Left side seats (A, B)
            for (String column : leftColumns) {
                Seat.SeatType type = Seat.SeatType.ECONOMY; // All regular seats are economy

                String seatId = row + column;
                seats.add(new Seat(seatId, row, column, type));
            }

            // Right side seat (C)
            for (String column : rightColumns) {
                Seat.SeatType type = Seat.SeatType.ECONOMY; // All regular seats are economy

                String seatId = row + column;
                seats.add(new Seat(seatId, row, column, type));
            }
        }
    }

    @Override
    public TransportType getTransportType() {
        return TransportType.BUS;
    }

    public String getBusCompany() {
        return busCompany;
    }
}