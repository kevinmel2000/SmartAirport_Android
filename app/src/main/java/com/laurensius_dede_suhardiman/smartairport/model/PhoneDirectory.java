package com.laurensius_dede_suhardiman.smartairport.model;

public class PhoneDirectory {

    String id;
    String name;
    String phone;
    String image;

    public PhoneDirectory(String id,String name,String phone,String image){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
