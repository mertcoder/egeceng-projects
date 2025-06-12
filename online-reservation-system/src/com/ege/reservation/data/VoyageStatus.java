package com.ege.reservation.data;

public enum VoyageStatus {
    SCHEDULED, // the voyage has planned but not started yet
    BOARDING, // passengers are getting in
    DEPARTED, // voyage has started
    FINISHED, // finished.
    CANCELLED // voyage cancelled.
}
