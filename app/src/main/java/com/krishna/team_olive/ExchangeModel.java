package com.krishna.team_olive;

public class ExchangeModel {

    private String user_name, client_name, user_uid, client_uid, item_name, postid;

    public ExchangeModel(String user_name, String client_name, String user_uid, String client_uid, String item_name, String postid) {
        this.user_name = user_name;
        this.client_name = client_name;
        this.user_uid = user_uid;
        this.client_uid = client_uid;
        this.item_name = item_name;
        this.postid = postid;
    }

    public ExchangeModel() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getClient_uid() {
        return client_uid;
    }

    public void setClient_uid(String client_uid) {
        this.client_uid = client_uid;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
