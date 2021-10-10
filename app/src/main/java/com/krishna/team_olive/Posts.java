package com.krishna.team_olive;

public class Posts {

    private String name;
    private String imageurl;
    private String postid;
    private String exchange01;
    private String exchange02;

    public Posts() {
    }

    public Posts(String name, String imageurl, String postid, String exchange01, String exchange02) {
        this.name = name;
        this.imageurl = imageurl;
        this.postid = postid;
        this.exchange01 = exchange01;
        this.exchange02 = exchange02;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange01() {
        return exchange01;
    }

    public void setExchange01(String exchange01) {
        this.exchange01 = exchange01;
    }

    public String getExchange02() {
        return exchange02;
    }

    public void setExchange02(String exchange02) {
        this.exchange02 = exchange02;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }



}
