package com.noshadow.app.api;

import com.noshadow.app.model.EmptyProxy;
import com.noshadow.app.model.LocationPayload;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Liga on 26/06/2017.
 */

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/location")
    Call<EmptyProxy> sendLocation(@Body LocationPayload payload);
}
