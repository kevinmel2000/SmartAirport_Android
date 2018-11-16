package com.laurensius_dede_suhardiman.smartairport.model;

public class BaggageTracking {
    String id;
    String baggage_code;
    String datetime;
    String status;

    public BaggageTracking(String id,
                           String baggage_code,
                           String datetime,
                           String status
    ){
        this.id = id;
        this.baggage_code = baggage_code;
        this.datetime = datetime;
        this.status = status;

    }

    public String getId() {
        return id;
    }

    public String getBaggageCode() {
        return baggage_code;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getStatus() {
        return status;
    }


}
