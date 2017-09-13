package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/13/2017.
 */

public class HoursData {
    private String days, am, pm;

    public HoursData(String days, String am, String pm) {
        this.days = days;
        this.am = am;
        this.pm = pm;
    }

    public String getDays() {
        return days;
    }

    public String getAm() {
        return am;
    }

    public String getPm() {
        return pm;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }
}
