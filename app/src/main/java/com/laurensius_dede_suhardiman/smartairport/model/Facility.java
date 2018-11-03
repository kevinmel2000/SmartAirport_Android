package com.laurensius_dede_suhardiman.smartairport.model;

public class Facility {

    String id;
    String name;
    String latitude;
    String longitude;
    String location;
    String description;
    String image;
    String marker;

    public Facility(
            String id,
            String name,
            String latitude,
            String longitude,
            String location,
            String description,
            String image,
            String marker
    ){
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.description = description;
        this.image = image;
        this.marker = marker;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getMarker() {
        return marker;
    }
}
