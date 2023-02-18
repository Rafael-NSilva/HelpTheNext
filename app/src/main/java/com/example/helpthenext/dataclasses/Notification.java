package com.example.helpthenext.dataclasses;

public class Notification {
    private String id;
    private String senderName;
    private String senderID;
    private String receiverName;
    private String receiverID;
    private String type;
    private String messageContent;

    public Notification() {}

    public Notification(String id, String senderName, String senderID, String receiverName, String receiverID, String type, String messageContent) {
        this.id = id;
        this.senderName = senderName;
        this.senderID = senderID;
        this.receiverName = receiverName;
        this.receiverID = receiverID;
        this.type = type;
        this.messageContent = messageContent;
    }

    public String getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getType() {
        return type;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
