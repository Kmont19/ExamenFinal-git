package com.example.examenfinal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.multidex.MultiDexApplication;

import com.example.examenfinal.database.DataBaseApp;
import com.example.examenfinal.services.SyncService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactAPP extends MultiDexApplication {
    public DataBaseApp db;
    private SyncService service;
    private boolean serviceBound;
    private ViewModelStoreOwner storeOwner;
    private ViewModelProvider.AndroidViewModelFactory vmFactory;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        db = DataBaseApp.instance(this);
        startService();
        storeOwner = ViewModelStore::new;
        vmFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this);
    }

    protected ServiceConnection serviceStarter = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder s) {
            if(s instanceof  SyncService.LocalBinder) {
                SyncService.LocalBinder binder = (SyncService.LocalBinder) s;
                service = binder.getService();
                serviceBound = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };

    public void startService() {
        Intent intent = new Intent(this, SyncService.class);
        bindService(intent, serviceStarter, Context.BIND_AUTO_CREATE);
    }

    public DataBaseApp getDb() {
        return db;
    }

    public void postDB(Runnable dbTask) {
        executor.submit(dbTask);
    }



    public ViewModelStoreOwner getStoreOwner(){
        return storeOwner;
    }
    public ViewModelProvider.AndroidViewModelFactory getFactory(){
        return  vmFactory;
    }
}
