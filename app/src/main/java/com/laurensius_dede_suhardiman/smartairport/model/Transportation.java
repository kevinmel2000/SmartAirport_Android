package com.laurensius_dede_suhardiman.smartairport.model;

import java.io.Serializable;

public class Transportation implements Serializable {

    String id;
    String name;
    String moda;
    String origin;
    String destination;
    String schedule;
    String ticket_price;
    String origin_lat;
    String origin_lon;
    String destination_lat;
    String destination_lon;

    public Transportation(
            String id,
            String name,
            String moda,
            String origin,
            String destination,
            String schedule,
            String ticket_price,
            String origin_lat,
            String origin_lon,
            String destination_lat,
            String destination_lon ){
        this.id = id;
        this.name = name;
        this.moda = moda;
        this.origin = origin;
        this.destination = destination;
        this.schedule = schedule;
        this.ticket_price = ticket_price;
        this.origin_lat = origin_lat;
        this.origin_lon = origin_lon;
        this.destination_lat = destination_lat;
        this.destination_lon = destination_lon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModa() {
        return moda;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getTicket_price() {
        return ticket_price;
    }

    public String getOrigin_lat() {
        return origin_lat;
    }

    public String getOrigin_lon() {
        return origin_lon;
    }

    public String getDestination_lat() {
        return destination_lat;
    }

    public String getDestination_lon() {
        return destination_lon;
    }


}
