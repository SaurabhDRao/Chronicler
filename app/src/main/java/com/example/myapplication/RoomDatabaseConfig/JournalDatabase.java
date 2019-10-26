package com.example.myapplication.RoomDatabaseConfig;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = { JournalEntry.class, ImageLock.class }, version = 2, exportSchema = false)
public abstract class JournalDatabase extends RoomDatabase {

    public abstract JournalDao journalDao();

    public abstract LockPointsDao lockPointsDao();

}
