package com.ege.reservation.data;

import java.util.ArrayList;
import java.util.List;

public abstract class Transport {
    private final String id;
    private String name;
    private int totalSeats;
    protected List<Seat> seats;



    public Transport(String id, String name, int totalSeats){
        this.id = id;
        this.name = name;
        this.totalSeats = totalSeats;
        this.seats = new ArrayList<Seat>();
        initializeSeats();
    }


    protected abstract void initializeSeats();
    public abstract TransportType getTransportType();
    public String getTransportId(){return id;}
    public String getTransportName(){return name;}
    public void setTransportName(String name){this.name = name;}
    public List<Seat> getSeats(){return seats;}

    public boolean isSeatAvailable(String seatId){
        return seats.stream()
                .filter(seat->seat.getSeatId().equals(seatId))
                .findFirst()
                .map(seat-> !seat.isSeatReserved())
                .orElse(false);
    }

    public void reserveSeat(String seatId){
        seats.stream()
                .filter(seat->seat.getSeatId().equals(seatId))
                .findFirst()
                .ifPresent(seat -> seat.setSeatReserved(true));
    }

    public void cancelSeatReservation(String seatId) {
        seats.stream()
                .filter(seat -> seat.getSeatId().equals(seatId))
                .findFirst()
                .ifPresent(seat -> seat.setSeatReserved(false));
    }

}
