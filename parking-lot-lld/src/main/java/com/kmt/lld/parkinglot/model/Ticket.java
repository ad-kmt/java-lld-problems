package com.kmt.lld.parkinglot.model;


import java.time.LocalDateTime;

public class Ticket {

    int id;
    ParkingSpot parkingSpot;
    LocalDateTime entryTime;
    LocalDateTime exitTime;
    String vehicleNumber;

}
