package com.kmt.lld.parkinglot.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
@Data
public class Ticket {

    int id;
    ParkingSpot parkingSpot;
    LocalDateTime entryTime;
    LocalDateTime exitTime;
    String vehicleNumber;

}
