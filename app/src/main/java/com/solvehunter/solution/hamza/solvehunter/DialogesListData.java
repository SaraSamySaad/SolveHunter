package com.solvehunter.solution.hamza.solvehunter;

/**
 * Created by C.M on 21/10/2018.
 */

public class DialogesListData {

    private String reciverId;
    private String lastMessage;
    private String id;

    public DialogesListData(String reciverId, String lastMessage) {
        this.reciverId = reciverId;
        this.lastMessage = lastMessage;
    }

    public DialogesListData() {
    }

    public String getReciverId() {
        return reciverId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DialogesListData{" +
                "reciverId='" + reciverId + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
