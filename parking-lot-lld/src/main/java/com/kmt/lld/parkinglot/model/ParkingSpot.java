package com.kmt.lld.parkinglot.model;

import com.kmt.lld.parkinglot.enums.ParkingSpotStatus;
import com.kmt.lld.parkinglot.enums.VehicleType;


public class ParkingSpot {

    int id;
    VehicleType vehicleType;
    ParkingSpotStatus parkingSpotStatus;

    public void setId(int id) {
        this.id = id;
    }
}
