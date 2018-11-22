package com.solvehunter.solution.hamza.solvehunter;

/**
 * Created by C.M on 23/10/2018.
 */

public class DialogesData {
    private String IsRead;
    private String body;
    private  String senderID;

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getIsRead() {
        return IsRead;
    }

    public String getBody() {
        return body;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getId() {
        return id;
    }

    public DialogesData() {
    }

    public DialogesData(String isRead, String body, String senderID) {
        IsRead = isRead;
        this.body = body;
        this.senderID = senderID;
    }
}
