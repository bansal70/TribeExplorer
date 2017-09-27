package com.tribe.explorer.model.beans;


import com.google.gson.annotations.SerializedName;

public class HoursOperation {
    @SerializedName("open")
    private String open;
    @SerializedName("close")
    private String close;

    public HoursOperation(String open, String close) {
        this.open = open;
        this.close = close;
    }

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
