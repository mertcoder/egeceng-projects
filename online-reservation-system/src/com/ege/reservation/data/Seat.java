package com.ege.reservation.data;

public class Seat {
    private final String id;
    private final int row;
    private final String column;
    private final SeatType type;
    private boolean isReserved;

    public Seat(String id, int row, String column, SeatType type) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.type = type;
        this.isReserved = false;
    }


    public String getSeatId() {
        return id;
    }

    public int getSeatRow() {
        return row;
    }

    public String getSeatColumn() {
        return column;
    }

    public SeatType getSeatType() {
        return type;
    }

    public boolean isSeatReserved() {
        return isReserved;
    }

    public void setSeatReserved(boolean reserved) {
        isReserved = reserved;
    }


    public String toString() {
        return id + (isReserved ? " [X]" : " [ ]");
    }

    public enum SeatType {
        ECONOMY,
        BUSINESS
    }
}
