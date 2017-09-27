package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/15/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutData {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public class Data {
        @SerializedName("ID")
        @Expose
        public Integer iD;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("featured_image")
        @Expose
        public String featuredImage;
    }
}
