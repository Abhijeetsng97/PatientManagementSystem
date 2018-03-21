package com.abhijeet.patientbillingsoftware.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhij on 19-03-2018.
 */

public class Ward {
    String num, pos;

    public Ward() {
    }

    public Ward(String num, String pos) {
        this.num = num;
        this.pos = pos;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("num", num);
        result.put("pos", pos);
        return result;
    }
}
