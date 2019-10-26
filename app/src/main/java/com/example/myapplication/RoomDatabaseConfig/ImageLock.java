package com.example.myapplication.RoomDatabaseConfig;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "imageLock")
public class ImageLock {

    @PrimaryKey
    private int id;

    @ColumnInfo (name = "point1x")
    private int point1x;

    @ColumnInfo (name = "point1y")
    private int point1y;

    @ColumnInfo (name = "point2x")
    private int point2x;

    @ColumnInfo (name = "point2y")
    private int point2y;

    @ColumnInfo (name = "point3x")
    private int point3x;

    @ColumnInfo (name = "point3y")
    private int point3y;

    @ColumnInfo (name = "point4x")
    private int point4x;

    @ColumnInfo (name = "point4y")
    private int point4y;

    @ColumnInfo (name = "image")
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint1x() {
        return point1x;
    }

    public void setPoint1x(int point1x) {
        this.point1x = point1x;
    }

    public int getPoint1y() {
        return point1y;
    }

    public void setPoint1y(int point1y) {
        this.point1y = point1y;
    }

    public int getPoint2x() {
        return point2x;
    }

    public void setPoint2x(int point2x) {
        this.point2x = point2x;
    }

    public int getPoint2y() {
        return point2y;
    }

    public void setPoint2y(int point2y) {
        this.point2y = point2y;
    }

    public int getPoint3x() {
        return point3x;
    }

    public void setPoint3x(int point3x) {
        this.point3x = point3x;
    }

    public int getPoint3y() {
        return point3y;
    }

    public void setPoint3y(int point3y) {
        this.point3y = point3y;
    }

    public int getPoint4x() {
        return point4x;
    }

    public void setPoint4x(int point4x) {
        this.point4x = point4x;
    }

    public int getPoint4y() {
        return point4y;
    }

    public void setPoint4y(int point4y) {
        this.point4y = point4y;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
