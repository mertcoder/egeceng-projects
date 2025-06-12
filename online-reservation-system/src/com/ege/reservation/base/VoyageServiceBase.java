package com.ege.reservation.base;

import com.ege.reservation.data.TransportType;
import com.ege.reservation.data.Voyage;

import java.time.LocalDateTime;
import java.util.List;

public interface VoyageServiceBase {
    public Voyage addVoyage(TransportType transportType, String transportName, String companyName,
                            String transportNumber, String origin, String destination,
                            LocalDateTime transportStartTime, LocalDateTime transportArrivalTime,
                            double price);

    public boolean cancelVoyage(String voyageId);
    public boolean deleteVoyage(String voyageId);
    public List<Voyage> getAllVoyages();
    public Voyage getVoyageById(String voyageId);

}
