package com.example.examenfinal.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examenfinal.model.ContactInfo;

import java.util.List;

@Dao
public interface ContactDAO {

    @Query("SELECT * FROM contactinfo")
    LiveData<List<ContactInfo>> getLiveAll();

    @Query("SELECT * FROM contactinfo")
    List<ContactInfo> getAll();

    @Query("SELECT * FROM ContactInfo WHERE needUpdate")
    List<ContactInfo> getNewContacts();

    @Insert
    void insertAll(ContactInfo...contacts);


}
