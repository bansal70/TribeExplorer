package com.tribe.explorer.model.beans;

/*
 * Created by rishav on 9/15/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileData {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {
        @SerializedName("user_login")
        @Expose
        public String userLogin;
        @SerializedName("user_email")
        @Expose
        public String userEmail;
        @SerializedName("user_registered")
        @Expose
        public String userRegistered;
        @SerializedName("user_status")
        @Expose
        public String userStatus;
        @SerializedName("display_name")
        @Expose
        public String displayName;
        @SerializedName("user_url")
        @Expose
        public String userUrl;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("Role")
        @Expose
        public String role;
        @SerializedName("Gender")
        @Expose
        public String gender;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("social_type")
        @Expose
        public String socialType;
    }
}
