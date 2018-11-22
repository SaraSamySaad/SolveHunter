package com.solvehunter.solution.hamza.solvehunter;

/**
 * Created by C.M on 24/10/2018.
 */

public class CommentData {

    private String body;
    private String addBy;

    public void setBody(String body) {
        this.body = body;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
    }

    public String getAddBy() {
        return addBy;
    }

    public String getBody() {
        return body;
    }

    public CommentData(String body, String addBy) {
        this.body = body;
        this.addBy = addBy;
    }

    public CommentData() {
    }
}
