package com.krishna.team_olive.messages;

public class MessagesList {
    private String name, uid, lastMessage, chatKey;
    private int unseenMessages;

    public MessagesList(String name, String uid, String lastMessage, String profilePic, int unseenMessages, String chatKey) {
        this.name = name;
        this.uid = uid;
        this.lastMessage = lastMessage;

        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
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

    public String getChatKey() {
        return chatKey;
    }
}
