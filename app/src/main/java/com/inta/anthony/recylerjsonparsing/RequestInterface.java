package com.inta.anthony.recylerjsonparsing;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Anthony on 6/24/2017.
 */

public interface RequestInterface {

    @GET("android/jsonandroid")
    Call<JSONResponse> getJSON();
}
