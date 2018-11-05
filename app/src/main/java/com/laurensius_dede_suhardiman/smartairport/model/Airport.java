package com.laurensius_dede_suhardiman.smartairport.model;

public class Airport {

    String id;
    String name;
    String address;
    String iata;
    String icao;

    public Airport(
            String id,
            String name,
            String address,
            String iata,
            String icao
    ){
        this.id = id ;
        this.name = name ;
        this.address = address ;
        this.iata = iata ;
        this.icao = icao ;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getIata() {
        return iata;
    }

    public String getIcao() {
        return icao;
    }
}
