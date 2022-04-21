package com.example.examenfinal.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewbinding.BuildConfig;

import com.example.examenfinal.dto.BodyDTO;
import com.example.examenfinal.model.ContactInfo;
import com.example.examenfinal.model.SyncInfo;
import com.example.examenfinal.repository.ContactRepository;
import com.example.examenfinal.repository.SyncRepository;
import com.example.examenfinal.services.proxy.ApiProxy;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncService extends Service {


    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SyncService getService() {
            return SyncService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private long tokenExpire;
    private String token;
    private ContactRepository contactRepository;
    private SyncRepository syncRepository;
    private ApiProxy proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        initRepo();
        initProxy();
    }

    private void observe() {
        syncRepository.liveDirty().observeForever(this.syncObserver);
    }

    private Observer<List<SyncInfo>> syncObserver = syncInfos -> {
        for(SyncInfo syncInfo: syncInfos){
            if(syncInfo.isOf(ContactInfo.class)){
                contactRepository.getAsyncNewContacts(contacts ->{
                    syncContacts(syncInfo, contacts, null);
                });
                continue;
            }
        }
    };

    @Override
    public void onDestroy() {
        syncRepository.liveDirty().removeObserver(syncObserver);
        super.onDestroy();
    }

    private void initRepo() {
        contactRepository = new ContactRepository(getApplication());
        syncRepository = new SyncRepository(getApplication());
    }

    public void syncContacts(SyncInfo sync, List<ContactInfo> contacts, Runnable complete) {
        BodyDTO<List<ContactInfo>> body = new BodyDTO<>();
        body.time = sync.getUpdated().toEpochSecond(ZoneOffset.UTC);
        body.data = contacts;
        proxy

    }

    private void initProxy() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(loggingInterceptor)
                .readTimeout(300, TimeUnit.SECONDS)
                .connectTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain
                            .request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", token)
                            .build();
                    return chain.proceed(request);
                }).build();

        proxy =  new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
                .build()
                .create(ApiProxy.class);
        fcmProxy = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGsonBuilder()))
                .baseUrl(BuildConfig.FCM_BASE_URL)
                .build().create(FCMProxy.class);

    }
}
