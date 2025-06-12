package com.ege.reservation.data;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Voyage {
    private final String id;
    private final Transport transport;
    private final String origin;
    private final String destination;
    private final LocalDateTime transportStartTime;
    private final LocalDateTime transportArrivalTime;
    private final double price;
    private VoyageStatus status;


    // Pattern: Builder here.
    private Voyage(Builder builder){
        this.id = builder.id != null ? builder.id: UUID.randomUUID().toString();
        this.transport = builder.transport;
        this.origin = builder.origin;
        this.destination = builder.destination;
        this.transportStartTime = builder.transportStartTime;
        this.transportArrivalTime = builder.transportArrivalTime;
        this.price = builder.price;
        this.status = builder.status;
    }


    public String getId() {
        return id;
    }

    public Transport getTransport() {
        return transport;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return transportStartTime;
    }

    public LocalDateTime getArrivalTime() {
        return transportArrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public VoyageStatus getStatus() {
        return status;
    }

    public void setStatus(VoyageStatus status) {
        this.status = status;
    }

    // shows formatted information.
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String transportType = transport.getTransportType().toString().toLowerCase();
        return String.format("%s (%s): %s -> %s (%s - %s) [%s] $%.2f",
                transport.getTransportName(),
                transportType,
                origin,
                destination,
                transportStartTime.format(formatter),
                transportArrivalTime.format(formatter),
                status,
                price);
    }

    public static class Builder {
        private String id;
        private Transport transport;
        private String origin;
        private String destination;
        private LocalDateTime transportStartTime;
        private LocalDateTime transportArrivalTime;
        private double price;
        private VoyageStatus status;

        public Builder(Transport transport, String origin, String destination) {
            this.transport = transport;
            this.origin = origin;
            this.destination = destination;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTransportStartTime(LocalDateTime transportStartTime) {
            this.transportStartTime = transportStartTime;
            return this;
        }

        public Builder withTransportArrivalTime(LocalDateTime transportArrivalTime) {
            this.transportArrivalTime = transportArrivalTime;
            return this;
        }

        public Builder withPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder withStatus(VoyageStatus status) {
            this.status = status;
            return this;
        }

        public Voyage build() {
            return new Voyage(this);
        }
    }
}
