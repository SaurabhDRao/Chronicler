package com.example.myapplication.RoomDatabaseConfig;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = { JournalEntry.class }, version = 1, exportSchema = false)
public abstract class JournalDatabase extends RoomDatabase {

    public abstract JournalDao journalDao();

}
