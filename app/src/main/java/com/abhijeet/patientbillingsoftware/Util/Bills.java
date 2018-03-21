package com.abhijeet.patientbillingsoftware.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhij on 19-03-2018.
 */

public class Bills {
    String name, amount, paid;

    public String getName() {
        return name;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Bills(String name, String amount, String paid) {
        this.name = name;
        this.paid = paid;
        this.amount = amount;
    }

    public Bills() {}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("amount", amount);
        result.put("paid", paid);
        return result;
    }
}
