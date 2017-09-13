package com.tribe.explorer.model;

/*
 * Created by rishav on 9/12/2017.
 */

import android.util.Log;

public class Operations {
    private static final String TAG = Operations.class.getSimpleName();

    public static String getLoginParams(String email, String password, String deviceToken,
                                        String deviceType, String lang) {
        String params = Config.LOGIN_URL + "?useremail=" + email
                +"&password="+password
                +"&deviceToken="+deviceToken
                +"&deviceType="+deviceType
                +"&lang="+lang;

        Log.e(TAG, "Login params-- "+params);

        return params;
    }

    public static String getRegisterParams(String firstName, String lastName, String email,
                                           String password, String role, String gender, String dob,
                                           String deviceToken, String deviceType, String lang) {
        String params = Config.REGISTRATION_URL + "?firstname=" + firstName
                +"&lastname="+lastName
                +"&useremail="+email
                +"&password="+password
                +"&role="+role
                +"&gender="+gender
                +"&dob="+dob
                +"&deviceToken="+deviceToken
                +"&deviceType="+deviceType
                +"&lang="+lang;

        Log.e(TAG, "Registration params-- "+params);

        return params;
    }

    public static String getCategoriesParams(String lang) {
        String params = Config.CATEGORIES_URL + "?lang="+lang;

        Log.e(TAG, "categories params-- "+params);

        return params;
    }
}
