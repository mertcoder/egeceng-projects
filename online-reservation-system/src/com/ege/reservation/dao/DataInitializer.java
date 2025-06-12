package com.ege.reservation.dao;


import com.ege.reservation.data.*;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.UUID;

public class DataInitializer {
    private static DataInitializer instance;
    private final DataStore dataStore;
    private final TransportFactory transportFactory;

    private DataInitializer(){
        dataStore = DataStore.getInstance();
        transportFactory = new TransportFactory();
    }

    public static synchronized DataInitializer getInstance(){
        if(instance == null){
            instance = new DataInitializer();
        }
        return instance;
    }


    public void initializeData() {
        createUsers();
        createTransports();
        createVoyages();
    }

    private void createUsers() {
        // Create admin user
        User admin = new User(
                "admin-" + UUID.randomUUID().toString().substring(0, 8),
                "mert",
                "123456",
                "System Administrator",
                "admin@reservationsystem.com",
                true
        );
        dataStore.addUser(admin);

        // Create regular users
        User user1 = new User(
                "user-" + UUID.randomUUID().toString().substring(0, 8),
                "hedera35",
                "123456",
                "Hedera A",
                "hedera@gmail.com",
                false
        );
        dataStore.addUser(user1);

        User user2 = new User(
                "user-" + UUID.randomUUID().toString().substring(0, 8),
                "zeynepalnt",
                "123456",
                "Zeynep A",
                "zeynep@gmail.com",
                false
        );

        User user3 = new User(
                "user-" + UUID.randomUUID().toString().substring(0, 8),
                "sevvall",
                "123456",
                "Sevval M",
                "sevval@gmail.com",
                false
        );


        dataStore.addUser(user2);
    }

    private void createTransports() {
        // Create buses
        Bus bus1 = transportFactory.createBus(
                "bus-" + UUID.randomUUID().toString().substring(0, 8),
                "City Express",
                "Metro Bus Lines"
        );
        dataStore.addTransport(bus1);

        Bus bus2 = transportFactory.createBus(
                "bus-" + UUID.randomUUID().toString().substring(0, 8),
                "Mountain Voyager",
                "Alpine Transport"
        );
        dataStore.addTransport(bus2);

        // Create flights
        Flight flight1 = transportFactory.createFlight(
                "flight-" + UUID.randomUUID().toString().substring(0, 8),
                "Sky Express",
                "Blue Sky Airlines",
                "BS124"
        );
        dataStore.addTransport(flight1);

        Flight flight2 = transportFactory.createFlight(
                "flight-" + UUID.randomUUID().toString().substring(0, 8),
                "Ocean Flyer",
                "Coastal Airways",
                "CA789"
        );
        dataStore.addTransport(flight2);
    }

    private void createVoyages() {
        // Get the created transports
        var transports = dataStore.getAllTransports();

        if (transports.size() >= 4) {
            // Create bus voyages
            Voyage busVoyage1 = new Voyage.Builder(transports.get(0), "Izmir", "Ankara")
                    .withId("voyage-" + UUID.randomUUID().toString().substring(0, 8))
                    .withTransportStartTime(LocalDateTime.now().plusDays(1))
                    .withTransportArrivalTime(LocalDateTime.now().plusDays(1).plusHours(6))
                    .withPrice(250.0)
                    .withStatus(VoyageStatus.SCHEDULED)
                    .build();
            dataStore.addVoyage(busVoyage1);

            Voyage busVoyage2 = new Voyage.Builder(transports.get(1), "Izmir", "Antalya")
                    .withId("voyage-" + UUID.randomUUID().toString().substring(0, 8))
                    .withTransportStartTime(LocalDateTime.now().plusDays(2))
                    .withTransportArrivalTime(LocalDateTime.now().plusDays(2).plusHours(8))
                    .withPrice(300.0)
                    .withStatus(VoyageStatus.SCHEDULED)
                    .build();
            dataStore.addVoyage(busVoyage2);

            // Create flight voyages
            Voyage flightVoyage1 = new Voyage.Builder(transports.get(2), "Istanbul", "London")
                    .withId("voyage-" + UUID.randomUUID().toString().substring(0, 8))
                    .withTransportStartTime(LocalDateTime.now().plusDays(3))
                    .withTransportArrivalTime(LocalDateTime.now().plusDays(3).plusHours(4))
                    .withPrice(1200.0)
                    .withStatus(VoyageStatus.SCHEDULED)
                    .build();
            dataStore.addVoyage(flightVoyage1);

            Voyage flightVoyage2 = new Voyage.Builder(transports.get(3), "Isparta", "Paris")
                    .withId("voyage-" + UUID.randomUUID().toString().substring(0, 8))
                    .withTransportStartTime(LocalDateTime.now().plusDays(5))
                    .withTransportArrivalTime(LocalDateTime.now().plusDays(5).plusHours(5))
                    .withPrice(1500.0)
                    .withStatus(VoyageStatus.SCHEDULED)
                    .build();
            dataStore.addVoyage(flightVoyage2);
        }
    }

}
