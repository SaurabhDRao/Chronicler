package com.example.myapplication.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface JournalDao {

    @Insert
    public void addEntry(JournalEntry entry);

    @Query("select * from JournalEntry")
    public List<JournalEntry> getAllJournalEntries();

    @Query("select max(id) as id from journalentry")
    public int getMaxId();

}
