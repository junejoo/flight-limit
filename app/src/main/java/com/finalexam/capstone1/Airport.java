package com.finalexam.capstone1;

public class Airport {
    private String name_en, name_kr, city;

    Airport(String name_en, String name_kr, String city) {
        this.name_en = name_en;
        this.name_kr = name_kr;
        this.city = city;
    }

    Airport(String name_en, String name_kr) {
        this.name_en = name_en;
        this.name_kr = name_kr;
        this.city = name_kr.split(" ")[0] + " " + name_kr.split(" ")[1];
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_kr() {
        return name_kr;
    }

    public void setName_kr(String name_kr) {
        this.name_kr = name_kr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}