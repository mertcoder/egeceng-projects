package com.ege.reservation.data;

// Pattern: Factory pattern.
public class TransportFactory {

    public Transport createTransport(TransportType type, String id, String name, String company, String number) {
        switch (type){
            case BUS -> {
                return new Bus(id,name, company);
            }
            case FLIGHT -> {
                return new Flight(id,name,company,number);
            }
            default -> throw new IllegalArgumentException("Unsupported transport type: ? " + type);
        }
    }
    public Bus createBus(String id, String name, String busCompany) {
        return new Bus(id, name, busCompany);
    }

    public Flight createFlight(String id, String name, String airline, String flightNumber) {
        return new Flight(id, name, airline, flightNumber);
    }
}
