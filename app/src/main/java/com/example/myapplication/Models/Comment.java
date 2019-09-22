package com.example.myapplication.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String uid, uname, uimg, content;
    private Object timeStamp;

    public  Comment() {

    }

    public Comment(String uid, String uname, String uimg, String content) {
        this.uid = uid;
        this.uname = uname;
        this.uimg = uimg;
        this.content = content;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Comment(String uid, String uname, String uimg, String content, Object timeStamp) {
        this.uid = uid;
        this.uname = uname;
        this.uimg = uimg;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
