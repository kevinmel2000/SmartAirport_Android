package com.laurensius_dede_suhardiman.smartairport.model;

public class Tourism {

    String id;
    String name;
    String city_district;
    String latitude;
    String longitude;
    String description;
    String category;
    String address;
    String phone;
    String image;

    public Tourism(
            String id,
            String name,
            String city_district,
            String latitude,
            String longitude,
            String description,
            String category,
            String address,
            String phone,
            String image){


        this.id= id;
        this.name= name;
        this.city_district= city_district;
        this.latitude= latitude;
        this.longitude= longitude;
        this.description= description;
        this.category= category;
        this.address= address;
        this.phone= phone;
        this.image= image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity_district() {
        return city_district;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
