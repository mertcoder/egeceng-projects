package com.ege.reservation.dao;


import com.ege.reservation.data.User;

import javax.xml.crypto.Data;
import java.util.UUID;

public class DataInitializer {
    private static DataInitializer instance;
    private final DataStore dataStore;

    // Transport factory here TODO

    private DataInitializer(){
        dataStore = DataStore.getInstance();
        // transportfactory here also TODO
    }

    public static synchronized DataInitializer getInstance(){
        if(instance == null){
            instance = new DataInitializer();
        }
        return instance;
    }

    public void initializeData() {
        createUsers();
        //createTransports();
        //createVoyages();
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



}
