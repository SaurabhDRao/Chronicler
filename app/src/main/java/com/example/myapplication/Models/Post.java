package com.example.myapplication.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey, userId, title, body, userPhoto, username;
    private Object postedDateTime;
    private int likeCount;

    public Post () {

    }

    public Post(String userId, String title, String body, String userPhoto, String username, int likeCount) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.userPhoto = userPhoto;
        this.username = username;
        this.postedDateTime = ServerValue.TIMESTAMP;
        this.likeCount = likeCount;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getPostedDateTime() {
        return postedDateTime;
    }

    public void setPostedDateTime(Object postedDateTime) {
        this.postedDateTime = postedDateTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
