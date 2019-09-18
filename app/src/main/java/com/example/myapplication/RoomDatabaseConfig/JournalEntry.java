package com.example.myapplication.RoomDatabaseConfig;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "JournalEntry")
public class JournalEntry {

    @PrimaryKey
    private int id;

    @ColumnInfo (name = "title")
    private String title;

    @ColumnInfo (name = "body")
    private String body;

    @ColumnInfo (name = "dateTime")
    private String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
