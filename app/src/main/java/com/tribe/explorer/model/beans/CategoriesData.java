package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesData {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public List<Data> data = null;

    public class Data {
        @SerializedName("term_id")
        @Expose
        public Integer termId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("img")
        @Expose
        public String img;
        private boolean isSelected;
        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

    }

}
