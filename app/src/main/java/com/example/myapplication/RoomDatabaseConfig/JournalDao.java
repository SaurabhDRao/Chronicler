package com.example.myapplication.RoomDatabaseConfig;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface JournalDao {

    @Insert
    public void addEntry(JournalEntry entry);

    @Query("select * from JournalEntry order by id desc")
    public List<JournalEntry> getAllJournalEntries();

    @Query("select max(id) as id from journalentry")
    public int getMaxId();

    @Delete
    public void deleteItem(JournalEntry entry);

    @Query("select * from JournalEntry where dateTime like :dateStr")
    public List<JournalEntry> getEntriesBasedOnDate(String dateStr);

}
