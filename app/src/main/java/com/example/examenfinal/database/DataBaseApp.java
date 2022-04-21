package com.example.examenfinal.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.examenfinal.database.dao.ContactDAO;
import com.example.examenfinal.database.dao.SyncDAO;
import com.example.examenfinal.model.ContactInfo;
import com.example.examenfinal.model.SyncInfo;

@Database(entities = {ContactInfo.class,
        SyncInfo.class},
            version = 3)
public abstract class DataBaseApp extends RoomDatabase {
    private static DataBaseApp instance;
    public abstract ContactDAO contactDAO();
    public abstract SyncDAO syncDAO();

    public synchronized static DataBaseApp instance(Context context) {
        if(instance == null){
            instance =  Room.databaseBuilder(context, DataBaseApp.class, "Dataapp").build();
        }
        return instance;
    }
}
