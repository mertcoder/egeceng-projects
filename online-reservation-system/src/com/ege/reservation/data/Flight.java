package com.ege.reservation.data;

public class Flight extends Transport{

    private final String airline;
    private final String flightNumber;

    public Flight(String id, String name, String airline, String flightNumber){
        super(id,name,180);
        this.airline = airline;
        this.flightNumber = flightNumber;
    }


    @Override
    protected void initializeSeats() {
        // 30 rows of 6 seats 3+3 config
        String[] columns = {"A","B","C","D","E","F"};

        for(int row=1; row<=30; row++){
            for(String column: columns){
                Seat.SeatType type = Seat.SeatType.ECONOMY;
                //business seats
                if(row<=3){
                    type = Seat.SeatType.BUSINESS;
                }
                String seatId = row + column; // seatId based on row and column
                seats.add(new Seat(seatId, row, column, type));
            }
        }


    }

    @Override
    public TransportType getTransportType() {
        return TransportType.FLIGHT;
    }
    public String getAirline(){return airline;}
    public String getFlightNumber(){return flightNumber;}
}
