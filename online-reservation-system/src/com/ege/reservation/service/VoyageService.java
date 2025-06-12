package com.ege.reservation.service;

import com.ege.reservation.base.VoyageServiceBase;
import com.ege.reservation.dao.DataStore;
import com.ege.reservation.data.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VoyageService implements VoyageServiceBase {
    private final DataStore dataStore;
    private final TransportFactory transportFactory;

    public VoyageService() {
        this.dataStore = DataStore.getInstance();
        this.transportFactory = new TransportFactory();
    }

    @Override
    public Voyage addVoyage(TransportType transportType, String transportName, String companyName, String transportNumber, String origin, String destination, LocalDateTime transportStartTime, LocalDateTime transportArrivalTime, double price) {
        // Create transport
        String transportId = (transportType == TransportType.BUS ? "bus-" : "flight-") +
                UUID.randomUUID().toString().substring(0, 8);

        Transport transport = transportFactory.createTransport(
                transportType, transportId, transportName, companyName, transportNumber);
        dataStore.addTransport(transport);

        // Create voyage
        Voyage voyage = new Voyage.Builder(transport, origin, destination)
                .withId("voyage-" + UUID.randomUUID().toString().substring(0, 8))
                .withTransportStartTime(transportStartTime)
                .withTransportArrivalTime(transportArrivalTime)
                .withPrice(price)
                .withStatus(VoyageStatus.SCHEDULED)
                .build();

        dataStore.addVoyage(voyage);
        return voyage;
    }

    @Override
    public boolean cancelVoyage(String voyageId) {
        Voyage voyage = dataStore.getVoyageById(voyageId);
        if (voyage == null) {
            return false;
        }

        voyage.setStatus(VoyageStatus.CANCELLED);
        return true;
    }

    @Override
    public boolean deleteVoyage(String voyageId) {
        Voyage voyage = dataStore.getVoyageById(voyageId);
        if (voyage == null) {
            return false;
        }

        dataStore.removeVoyage(voyageId);
        return true;
    }

    @Override
    public List<Voyage> getAllVoyages() {
        return dataStore.getAllVoyages();
    }


    @Override
    public Voyage getVoyageById(String voyageId) {
        return dataStore.getVoyageById(voyageId);
    }

}
