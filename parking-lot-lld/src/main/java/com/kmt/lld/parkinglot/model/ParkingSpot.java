package com.kmt.lld.parkinglot.model;

import com.kmt.lld.parkinglot.enums.ParkingSpotStatus;
import com.kmt.lld.parkinglot.enums.VehicleType;
import lombok.Data;

@Data
public class ParkingSpot {

    int id;
    VehicleType vehicleType;
    ParkingSpotStatus parkingSpotStatus;

}
