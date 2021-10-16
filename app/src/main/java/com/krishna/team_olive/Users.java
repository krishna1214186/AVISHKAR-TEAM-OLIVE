package com.krishna.team_olive;

import java.util.HashMap;

public class Users {
    private String email,name,location,phone,uid,profileimg,isNGO;
   // HashMap<String,Object> map;

    public Users(String email,String isNGO, String location, String name,  String phone,  String profileimg,String uid ) {
//        HashMap<String,Object>

        this.email = email;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.uid = uid;
        this.profileimg = profileimg;
        this.isNGO = isNGO;
    }

    public Users() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getIsNGO() {
        return isNGO;
    }

    public void setIsNGO(String isNGO) {
        this.isNGO = isNGO;
    }
}