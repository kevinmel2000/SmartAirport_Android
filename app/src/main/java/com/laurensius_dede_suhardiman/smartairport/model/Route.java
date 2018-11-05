package com.laurensius_dede_suhardiman.smartairport.model;

public class Route {

    String route_id;
    String airlines_id;
    String flight_no;
    String airlines;
    String origin_id;
    String origin_name;
    String origin_iata;
    String origin_icao;
    String destination_id;
    String destination_name;
    String destination_iata;
    String destination_icao;
    String flight_schedule;
    String flight_duration_minutes;
    String transit;
    String transit_name;
    String transit_iata;
    String transit_icao;

    public Route (
            String route_id,
            String airlines_id,
            String flight_no,
            String airlines,
            String origin_id,
            String origin_name,
            String origin_iata,
            String origin_icao,
            String destination_id,
            String destination_name,
            String destination_iata,
            String destination_icao,
            String flight_schedule,
            String flight_duration_minutes,
            String transit,
            String transit_name,
            String transit_iata,
            String transit_icao
    ){
        this.route_id =  route_id ;
        this.airlines_id =  airlines_id ;
        this.flight_no =  flight_no ;
        this.airlines =  airlines ;
        this.origin_id =  origin_id ;
        this.origin_name =  origin_name ;
        this.origin_iata =  origin_iata ;
        this.origin_icao =  origin_icao ;
        this.destination_id = destination_id  ;
        this.destination_name =  destination_name ;
        this.destination_iata =  destination_iata ;
        this.destination_icao =  destination_icao ;
        this.flight_schedule =  flight_schedule ;
        this.flight_duration_minutes =  flight_duration_minutes ;
        this.transit =  transit ;
        this.transit_name =  transit_name ;
        this.transit_iata =  transit_iata ;
        this.transit_icao =  transit_icao ;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getAirlines_id() {
        return airlines_id;
    }

    public String getFlight_no() {
        return flight_no;
    }

    public String getAirlines() {
        return airlines;
    }

    public String getOrigin_id() {
        return origin_id;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public String getOrigin_iata() {
        return origin_iata;
    }

    public String getOrigin_icao() {
        return origin_icao;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public String getDestination_iata() {
        return destination_iata;
    }

    public String getDestination_icao() {
        return destination_icao;
    }

    public String getFlight_schedule() {
        return flight_schedule;
    }

    public String getFlight_duration_minutes() {
        return flight_duration_minutes;
    }

    public String getTransit() {
        return transit;
    }

    public String getTransit_name() {
        return transit_name;
    }

    public String getTransit_iata() {
        return transit_iata;
    }

    public String getTransit_icao() {
        return transit_icao;
    }
}
