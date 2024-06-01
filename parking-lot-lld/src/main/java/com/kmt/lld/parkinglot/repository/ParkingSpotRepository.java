package com.kmt.lld.parkinglot.repository;

import com.kmt.lld.parkinglot.model.ParkingSpot;

import java.util.HashMap;
import java.util.Map;

public class ParkingSpotRepository {

    Map<Integer, ParkingSpot> parkingSpots;
    int counter;

    public ParkingSpotRepository(){
        this.parkingSpots = new HashMap<>();
        this.counter = 1;
    }

    public ParkingSpot insert(ParkingSpot parkingSpot){
        int id = counter++;
        parkingSpot.setId(id);
        parkingSpots.put(id, parkingSpot);
        return parkingSpot;
    }

}
