package com.finalexam.capstone1;

public class FlightDetail {
    private String dep_code, arr_code, dep_time, arr_time, carrierCode, number, duration, carrier_kor;

    public String getArr_time() {
        return arr_time;
    }

    public String getArr_code() {
        return arr_code;
    }

    public String getDep_code() {
        return dep_code;
    }

    public String getDep_time() {
        return dep_time;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public String getNumber() {
        return number;
    }

    public String getDuration() {
        return duration;
    }

    public String getCarrier_kor() {
        return carrier_kor;
    }

    public void setCarrier_kor(String carrier_kor) {
        this.carrier_kor = carrier_kor;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public void setArr_time(String arr_time) {
        this.arr_time = arr_time;
    }

    public void setArr_code(String arr_code) {
        this.arr_code = arr_code;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public void setDep_code(String dep_code) {
        this.dep_code = dep_code;
    }

    public FlightDetail(String carrierCode, String carrier_kor, String number, String dep_code, String dep_time, String arr_code, String arr_time, String duration){
        this.carrierCode = carrierCode;
        this.dep_code = dep_code;
        this.dep_time = dep_time;
        this.arr_code = arr_code;
        this.arr_time = arr_time;
        this.number = number;
        this.duration = duration;
        this.carrier_kor = carrier_kor;
    }

}
