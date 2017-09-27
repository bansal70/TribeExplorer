package com.tribe.explorer.model.beans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobHours {
    private List<Days> days = null;

    public List<Days> getDays() {
        return days;
    }

    public void setDays(List<Days> days) {
        this.days = days;
    }

    public static class Days {
        @SerializedName("open")
        @Expose
        public String open;
        @SerializedName("close")
        @Expose
        public String close;

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }
    }
}
