package com.laurensius_dede_suhardiman.smartairport.model;

import java.io.Serializable;

public class ParkingArea implements Serializable {

    String id;
    String name;
    String description;
    String status_car;
    String status_motorcycle;
    String latitude;
    String longitude;

    public ParkingArea(String id,
            String name,
            String description,
            String status_car,
            String status_motorcycle,
            String latitude,
            String longitude){

        this.id = id ;
        this.name = name;
        this.description = description;
        this.status_car = status_car;
        this.status_motorcycle = status_motorcycle;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus_car() {
        return status_car;
    }

    public String getStatus_motorcycle() {
        return status_motorcycle;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
