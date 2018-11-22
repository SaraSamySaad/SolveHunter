package com.solvehunter.solution.hamza.solvehunter;

import android.util.Log;

/**
 * Created by C.M on 16/10/2018.
 */
public class UserHomePosts {

    private  String body;
    private  long likes;
    private long comments;
    private String uploadedById;
    private String id;
    public UserHomePosts() {}

    public UserHomePosts(String body, long likes, long comments, String uploadedById) {
        this.body = body;
        this.likes = likes;
        this.comments = comments;
        this.uploadedById = uploadedById;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public String getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(String uploadedById) {
        this.uploadedById = uploadedById;
    }

    @Override
    public String toString() {
        return "UserHomePosts{" +
                "body='" + body + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                ", uploadedById='" + uploadedById + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
