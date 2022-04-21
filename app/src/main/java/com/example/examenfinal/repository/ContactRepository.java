package com.example.examenfinal.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.examenfinal.ContactAPP;
import com.example.examenfinal.database.dao.ContactDAO;
import com.example.examenfinal.model.ContactInfo;

import java.util.List;
import java.util.function.Consumer;

public class ContactRepository {
    private final ContactDAO dao;
    private final ContactAPP app;

    public ContactRepository(Application context) {
        app = (ContactAPP) context;
        dao = app.getDb().contactDAO();
    }

    public void getAsyncAll(Consumer<List<ContactInfo>> completion){
        app.postDB(()-> {
            List<ContactInfo> contacts = dao.getAll();
            if (completion != null) {
                new Handler(Looper.getMainLooper()).post(()->{
                    completion.accept(contacts);
                });
            }
        });
    }

    public LiveData<List<ContactInfo>>liveAll() { return dao.getLiveAll(); }

    public void getAsyncNewContacts(Consumer<List<ContactInfo>> completion) {
        app.postDB(()->{
            final List<ContactInfo> data = dao.getNewContacts();
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(()->{
                    completion.accept(data);
                });
            }
        } );
    }
}
