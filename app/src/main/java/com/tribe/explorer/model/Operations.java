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

    public static String getForgotPassParams(String email, String lang) {
        String params = Config.FORGOT_PASSWORD_URL + "?useremail="+email
                +"&lang="+lang;

        Log.e(TAG, "forgot password params-- "+params);

        return params;
    }

    public static String getProfileParams(String user_id, String lang) {
        String params = Config.PROFILE_URL + "?user_id=" + user_id
                +"&lang="+lang;

        Log.e(TAG, "profile params-- "+params);

        return params;
    }

    public static String getUpdateProfileParams(String firstName, String lastName, String email,
                                                String role, String gender, String dob, String lang) {
        String params = Config.UPDATE_PROFILE_URL + "?firstname=" + firstName
                +"&lastname="+lastName
                +"&useremail="+email
                +"&role="+role
                +"&gender="+gender
                +"&dob="+dob
                +"&lang="+lang;

        Log.e(TAG, "update profile params-- "+params);

        return params;
    }

    public static String getCategoriesParams(String lang) {
        String params = Config.CATEGORIES_URL + "?lang="+lang;

        Log.e(TAG, "categories params-- "+params);

        return params;
    }

    public static String getAddListingParams(String email, String business_name, String contact_email,
                                             String description, String location, String website,
                                             String video, String phone, String category, String labels,
                                             String region, String lang) {
        String params = Config.ADD_LISTING_URL + "?email=" + email + "&listing_name=" + business_name
                +"&contact_email_Url=" + contact_email + "&descrtiption=" + description
                +"&location=" + location + "&website=" + website + "&video_url=" + video
                +"&Phone_number=" + phone + "&listing_category=" + category + "&business_labels=" + labels
                +"&listing_region=" + region + "&lang=" + lang;

        Log.e(TAG, "add_listing params-- "+params);

        return params;
    }

    public static String getListingParams(int category_id, String lang) {
        String params = Config.LISTING_URL + "?cat_id="+category_id
                +"&lang="+lang;

        Log.e(TAG, "listing params-- "+params);

        return params;
    }

    public static String getFavouriteParams(String url, String user_id, int cat_id, String lang) {
        String params = url + user_id +"&listing_id="+cat_id
                +"&lang="+lang;

        Log.e(TAG, "add_favourite params-- "+params);

        return params;
    }

    public static String getFavListParams(String user_id, String lang) {
        String params = Config.FAV_LIST_URL + "?user_id="+user_id
                +"&lang="+lang;

        Log.e(TAG, "favourite_list params-- "+params);

        return params;
    }

    public static String getMyListingParams(String user_id, String lang){
        String params = Config.MY_LIST_URL + "?user_id="+user_id
                +"&lang="+lang;

        Log.e(TAG, "my_list params-- "+params);

        return params;
    }
}
