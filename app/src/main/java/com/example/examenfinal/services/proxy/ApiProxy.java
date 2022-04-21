package com.example.examenfinal.services.proxy;

import java.util.List;

import com.example.examenfinal.model.ContactInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;


public interface ApiProxy {

    @GET("contact/data/")
    Call<List<ContactInfo>> get();

}
