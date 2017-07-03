package com.inta.anthony.recylerjsonparsing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anthony on 6/24/2017.
 */

public class JSONResponse {

    @SerializedName("android")
    @Expose
    private AndroidVersion[] android;

    public AndroidVersion[] getAndroid(){
        return android;
    }
}
