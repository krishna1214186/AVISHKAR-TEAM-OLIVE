package com.krishna.team_olive.chat.messages;

public class MessagesList {
    private String name, uid, lastMessage;
    private int unseenMessages;

    public MessagesList(String name, String uid, String lastMessage,  int unseenMessages) {
        this.name = name;
        this.uid = uid;
        this.lastMessage = lastMessage;

        this.unseenMessages = unseenMessages;

    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getLastMessage() {
        return lastMessage;
    }



    public int getUnseenMessages() {
        return unseenMessages;
    }


}
