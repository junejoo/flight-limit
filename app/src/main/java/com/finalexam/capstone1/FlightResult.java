package com.finalexam.capstone1;

import java.io.Serializable;

public class FlightResult implements Serializable {
    private String[] dep_code, arr_code, dep_time, arr_time, carrierCode, number, duration, carrier_eng, carrier_kor;
    private String totalTime;
    private int price;

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setDep_code(String[] dep_code) {
        this.dep_code = dep_code;
    }

    public void setDep_code(int i) {
        this.dep_code = new String[i];
    }

    public void setDep_code(String dep_code, int i){
        this.dep_code[i] = dep_code;
    }

    public void setDep_time(String[] dep_time) {
        this.dep_time = dep_time;
    }

    public void setDep_time(int i) {
        this.dep_time = new String[i];
    }

    public void setDep_time(String dep_time, int i){
        this.dep_time[i] = dep_time;
    }

    public void setArr_code(String[] arr_code) {
        this.arr_code = arr_code;
    }

    public void setArr_code(int i) {
        this.arr_code = new String[i];
    }

    public void setArr_code(String arr_code, int i){
        this.arr_code[i] = arr_code;
    }

    public void setArr_time(String[] arr_time) {
        this.arr_time = arr_time;
    }

    public void setArr_time(int i) {
        this.arr_time = new String[i];
    }

    public void setArr_time(String arr_time, int i){
        this.arr_time[i] = arr_time;
    }

    public void setCarrierCode(String[] carrierCode) {
        this.carrierCode = carrierCode;
    }

    public void setCarrierCode(int i) {
        this.carrierCode = new String[i];
    }

    public void setCarrierCode(String carrierCode, int i){
        this.carrierCode[i] = carrierCode;
    }

    public void setNumber(String[] number) {
        this.number = number;
    }

    public void setNumber(String number, int i){
        this.number[i] = number;
    }

    public void setNumber(int i){ this.number = new String[i]; }

    public void setDuration(String[] duration) {
        this.duration = duration;
    }

    public void setDuration(String duration, int i){
        this.duration[i] = duration;
    }

    public void setDuration(int i){ this.duration = new String[i]; }

    public void setCarrier_eng(String[] carrier_eng) {
        this.carrier_eng = carrier_eng;
    }

    public void setCarrier_eng(String carrier_eng, int i){this.carrier_eng[i]= carrier_eng; }

    public void setCarrier_kor(String[] carrier_kor) {
        this.carrier_kor = carrier_kor;
    }

    public void setCarrier_kor(String carrier_kor, int i){this.carrier_kor[i]= carrier_kor; }

    public String[] getCarrier_eng() {
        return carrier_eng;
    }

    public String[] getCarrier_kor() {
        return carrier_kor;
    }

    public String getCarrier_eng(int i){
        return carrier_eng[i];
    }

    public String getCarrier_kor(int i){
        return carrier_kor[i];
    }

    public int getPrice() {
        return price;
    }

    public String[] getDep_code() {
        return dep_code;
    }

    public String getDep_code(int i){
        return dep_code[i];
    }

    public String[] getDep_time() {
        return dep_time;
    }

    public String getDep_time(int i){
        return dep_time[i];
    }

    public String[] getArr_code() {
        return arr_code;
    }

    public String getArr_code(int i){
        return arr_code[i];
    }

    public String[] getArr_time() {
        return arr_time;
    }

    public String getArr_time(int i){
        return arr_time[i];
    }

    public String[] getCarrierCode() {
        return carrierCode;
    }

    public String getCarrierCode(int i){
        return carrierCode[i];
    }

    public int getDepCodeSize(){
        return dep_code.length;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String[] getNumber() {
        return number;
    }
    public String getNumber(int i){
        return number[i];
    }

    public String[] getDuration() {
        return duration;
    }

    public String getDuration(int i) {
        return duration[i];
    }

    public FlightResult(int i){
        this.dep_code = new String[i];
        this.dep_time = new String[i];
        this.arr_code = new String[i];
        this.arr_time = new String[i];
        this.carrierCode = new String[i];
        this.number = new String[i];
        this.duration = new String[i];
        this.carrier_eng = new String[i];
        this.carrier_kor = new String[i];
    }

    public FlightResult(String[] dep_code, String[] dep_time, String[] arr_code, String[] arr_time, String[] carrierCode, String[] carrier_eng, String[] carrier_kor, String[] number, String totalTime, String[] duration, int price){
        this.dep_code = dep_code;
        this.dep_time = dep_time;
        this.arr_code = arr_code;
        this.arr_time = arr_time;
        this.carrierCode = carrierCode;
        this.number = number;
        this.totalTime = totalTime;
        this.price = price;
        this.duration = duration;
        this.carrier_eng = carrier_eng;
        this.carrier_kor = carrier_kor;
    }

    public FlightResult(FlightResult fl){
        this.dep_code = fl.getDep_code();
        this.dep_time = fl.getDep_time();
        this.arr_code = fl.getArr_code();
        this.arr_time = fl.getArr_time();
        this.carrierCode = fl.getCarrierCode();
        this.number = fl.getNumber();
        this.totalTime = fl.getTotalTime();
        this.price = fl.getPrice();
        this.duration = fl.getDuration();
        this.carrier_eng = fl.getCarrier_eng();
        this.carrier_kor = fl.getCarrier_kor();
    }
}
