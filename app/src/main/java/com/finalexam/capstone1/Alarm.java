package com.finalexam.capstone1;

import java.util.Date;

public class Alarm {

    String dept, arrv, airl;   // 출발지, 도착지, 항공사
    int adlt, chld; // 성인, 아동 인원
//    private Date date;      // 출발날짜
    String arrdate, depdate, price;
    Boolean stop;   // 경유여부 (경유True, 직항False)
    String round;

    public Alarm(String dept, String arrv, int adlt, int chld, String depdate, String arrdate,String price, String round) {
        this.dept = dept;
        this.arrv = arrv;
//        this.airl = airl;
        this.adlt = adlt;
        this.chld = chld;
        this.depdate = depdate;
        this.arrdate = arrdate;
        this.price = price;
        this.round=round;
    }
}
