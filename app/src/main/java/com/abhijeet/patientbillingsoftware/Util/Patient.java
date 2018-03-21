package com.abhijeet.patientbillingsoftware.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhij on 19-03-2018.
 */

public class Patient {
    String name,id,time,ward,wardNum,dismissTime,billTotal,paid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getWardNum() {
        return wardNum;
    }

    public void setWardNum(String wardNum) {
        this.wardNum = wardNum;
    }

    public String getDismissTime() {
        return dismissTime;
    }

    public void setDismissTime(String dismissTime) {
        this.dismissTime = dismissTime;
    }

    public String getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(String billTotal) {
        this.billTotal = billTotal;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public Patient(String name, String id, String time, String ward, String wardNum,
                    String dismissTime, String billTotal, String paid) {
        this.name = name;
        this.id = id;
        this.time = time;
        this.ward = ward;
        this.wardNum = wardNum;

        this.dismissTime = dismissTime;
        this.billTotal = billTotal;
        this.paid = paid;
    }

    public Patient() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("id", id);
        result.put("time", time);
        result.put("ward", ward);
        result.put("wardNum", wardNum);
        result.put("dismissTime", dismissTime);
        result.put("billTotal", billTotal);
        result.put("paid", paid);
        return result;
    }

}
