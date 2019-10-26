package com.example.myapplication.RoomDatabaseConfig;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LockPointsDao {
    @Insert
    public void insertPoints(ImageLock imageLock);

    @Query("select * from imageLock limit 1")
    public ImageLock getPoints();

    @Query("delete from imageLock where id = 1")
    public void deleteData();
}
