package com.example.examenfinal.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.examenfinal.model.SyncInfo;

import java.util.List;

@Dao
public interface SyncDAO {
    @Query("SELECT * FROM syncinfo")
    LiveData<List<SyncInfo>> getLiveAll();

    @Query("SELECT * FROM syncinfo WHERE entity = :entityName")
    LiveData<SyncInfo> getLive(String entityName);

    @Query("SELECT * FROM syncinfo WHERE entity = :entityName")
    SyncInfo get(String entityName);

    @Query("SELECT * FROM syncinfo WHERE isDirty")
    LiveData<List<SyncInfo>> getLiveDirty();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SyncInfo... contacts);

    @Delete()
    void delete(SyncInfo... contacts);

    @Update()
    void update(SyncInfo... syncInfos);


}
