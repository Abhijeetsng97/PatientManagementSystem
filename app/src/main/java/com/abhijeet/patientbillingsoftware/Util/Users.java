package com.abhijeet.patientbillingsoftware.Util;

/**
 * Created by abhij on 17-03-2018.
 */

public class Users {

    String auth,name,type,uid;

    public Users(){
    }

    public Users(String auth,String name,String type,String uid){
        this.auth = auth;
        this.name = name;
        this.type = type;
        this.uid = uid;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
