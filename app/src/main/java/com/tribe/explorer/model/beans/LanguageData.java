package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/14/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageData {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public class Data {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("language")
        @Expose
        public String language;
    }

}
