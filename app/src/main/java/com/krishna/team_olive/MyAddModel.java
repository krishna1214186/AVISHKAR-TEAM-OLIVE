package com.krishna.team_olive;

import android.net.Uri;

public class MyAddModel {
    private String name;
    private String cateogary;
    private String post_img;

    public MyAddModel(String name, String cateogary, String post_img) {
        this.name = name;
        this.cateogary = cateogary;
        this.post_img = post_img;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCateogary() {
        return cateogary;
    }

    public void setCateogary(String cateogary) {
        this.cateogary = cateogary;
    }

}