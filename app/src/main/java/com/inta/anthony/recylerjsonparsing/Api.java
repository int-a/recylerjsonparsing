package com.inta.anthony.recylerjsonparsing;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anthony on 7/1/2017.
 */

public class Api {

    static ArrayList<AndroidVersion> data;

    public static Retrofit getRestAdapter(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.learn2crack.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
