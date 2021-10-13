package com.krishna.team_olive.chat;

public class ChatList {
String id,message;
Long timeStamp;

    public ChatList(String id, String message, Long timeStamp) {
        this.id = id;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public ChatList(String id, String message) {
        this.id = id;
        this.message = message;
    }
    public ChatList()
    {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
