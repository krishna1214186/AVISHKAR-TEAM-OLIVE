package com.krishna.team_olive;

public class users {
    private String email,name,phone,uid,profileimg,isNGO;

    public users(String email, String name, String phone, String uid, String profileimg, String isNGO) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.profileimg = profileimg;
        this.isNGO = isNGO;
    }

    public users() {
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

    public String isNGO() {
        return isNGO;
    }

    public void setNGO(String NGO) {
        isNGO = NGO;
    }
}