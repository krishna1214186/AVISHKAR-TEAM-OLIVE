package com.krishna.team_olive.ModelClasses;

public class NotificationsModel {
    private String user_id;
    private String text;
    private String post_id;
    private int Type;

    public NotificationsModel() {
    }

    public NotificationsModel(String user_id, String text, String post_id, int type) {
        this.user_id = user_id;
        this.text = text;
        this.post_id = post_id;
        Type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
