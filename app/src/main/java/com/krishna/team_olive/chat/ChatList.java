package com.krishna.team_olive.chat;

public class ChatList {
    String id,message;
    Long timeStamp;
    String isSeen;

    public ChatList(String id, String message, Long timeStamp,String isSeen) {
        this.id = id;
        this.message = message;
        this.timeStamp = timeStamp;
        this.isSeen=isSeen;
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

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }
}