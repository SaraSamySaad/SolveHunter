package com.solvehunter.solution.hamza.solvehunter;

/**
 * Created by C.M on 18/10/2018.
 */

public class PendingPosts {

    private String body;
    private String uploadedById;
    // private String id;

    public PendingPosts(){}
    public PendingPosts(String body, String uploadedById) {
        this.body = body;
        this.uploadedById = uploadedById;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUploadedById(String uploadedById) {
        this.uploadedById = uploadedById;
    }

    public String getBody() {
        return body;
    }

    public String getUploadedById() {
        return uploadedById;
    }
}
