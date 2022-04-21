package com.example.examenfinal.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.examenfinal.ContactAPP;
import com.example.examenfinal.database.dao.SyncDAO;
import com.example.examenfinal.model.SyncInfo;

import java.time.LocalDateTime;
import java.util.List;


public class SyncRepository {
    private final SyncDAO dao;
    private ContactAPP app;
    public SyncRepository(Application context){
        this.app = (ContactAPP) context;
        dao = app.getDb().syncDAO();
    }

    public LiveData<List<SyncInfo>>liveAll(){
       return  dao.getLiveAll();
    }
    public LiveData<List<SyncInfo>>liveDirty(){
        return  dao.getLiveDirty();
    }

    public LiveData<SyncInfo>live(String name){
        return  dao.getLive(name);
    }

    public void save(@Nullable SyncInfo info, @Nullable  Class<?> entityClass, Runnable completion) {
        String name;
        if(entityClass != null) {
            name = entityClass.getSimpleName().toLowerCase();
            app.postDB(()->{
                SyncInfo entity = dao.get(name);
                if(entity == null){
                    entity = new SyncInfo();
                    entity.setEntity(name);
                    entity.setCreated(LocalDateTime.now());
                }
                entity.setUpdated(LocalDateTime.of(1970,01, 01,0,0,0,0));
                entity.setDirty(true);
                dao.insert(entity);
                if(completion != null) {
                    new Handler(Looper.getMainLooper()).post(completion);
                }
            });
        }else{
           update(completion, info);
        }
    }
    private void save(Runnable completion, SyncInfo syncInfos){

        app.postDB(()->{
             dao.insert(syncInfos);
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }

        });

    }
    private void update(Runnable completion, SyncInfo... syncInfos){
        app.postDB(()->{
            dao.update(syncInfos);
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }

        });

    }

    public void delete(SyncInfo... syncInfos) {
        delete(null, syncInfos);
    }
    public void delete(Runnable completion, SyncInfo... syncInfos){
        app.postDB(()->{
             dao.delete(syncInfos);

            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }

        });

    }

    public void requestSync(String name) {

            app.postDB(()->{
                SyncInfo entity = dao.get(name);
                if(entity == null){
                    entity = new SyncInfo();
                    entity.setEntity(name);
                    entity.setCreated(LocalDateTime.now());
                }
                entity.setDirty(true);
                dao.insert(entity);

            });

    }

    public void unDirty(SyncInfo syncInfo) {
        syncInfo.setDirty(false);
        update(null, syncInfo);
    }
}
