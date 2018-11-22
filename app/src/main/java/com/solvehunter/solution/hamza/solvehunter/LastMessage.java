package com.solvehunter.solution.hamza.solvehunter;

public class LastMessage {
    private String lastMessage;
    private String reciverId;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LastMessage() {
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public LastMessage(String lastMessage, String reciverId) {
        this.lastMessage = lastMessage;
        this.reciverId = reciverId;
    }
}
