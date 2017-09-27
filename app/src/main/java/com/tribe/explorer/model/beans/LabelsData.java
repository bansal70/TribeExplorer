
package com.tribe.explorer.model.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabelsData {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public List<Data> data = null;

    public class Data {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        public String name;
    }
}
