package com.finalexam.capstone1;

public class Flight {
    String carrierCode;
    String dep_code;
    String dep_time;
    String arr_code;
    String arr_time;

    public String getCarrierCode() {
        return carrierCode;
    }

    public String getDep_code() {
        return dep_code;
    }

    public String getDep_time() {
        return dep_time;
    }

    public String getArr_code() {
        return arr_code;
    }

    public String getArr_time() {
        return arr_time;
    }

    public Flight(String carrierCode, String dep_code, String dep_time, String arr_code, String arr_time){
        this.carrierCode = carrierCode;
        this.dep_code = dep_code;
        this.dep_time = dep_time;
        this.arr_code = arr_code;
        this.arr_time = arr_time;
    }
}
