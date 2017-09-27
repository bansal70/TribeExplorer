package com.tribe.explorer.model;

/*
 * Created by rishav on 9/12/2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Operations {
    private static final String TAG = Operations.class.getSimpleName();
    public static JSONObject jsonObject = new JSONObject();
    public static ArrayList<String> langList = new ArrayList<>();
    public static ArrayList<String> categoriesList = new ArrayList<>();
    public static ArrayList<String> galleriesList = new ArrayList<>();
    public static ArrayList<String> labelsList = new ArrayList<>();

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

    public static String getSocialLoginParams(String firstName, String lastName, String email, String id,
                                              String deviceToken, String deviceType,
                                              String social_type, String lang) {
        String params = Config.SOCIAL_LOGIN_URL+"?firstname=" + firstName
                +"&lastname=" + lastName
                +"&useremail=" + email
                +"&social_id=" + id
                +"&deviceToken=" + deviceToken
                +"&deviceType=" + deviceType
                +"&social_type="+social_type
                +"&lang="+lang;

        Log.i(TAG, "social_login params-- "+params);

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

    public static String getUpdateProfileParams(String user_id, String firstName, String lastName,
                                                String email, String role, String gender,
                                                String dob, String lang) {
        String params = Config.UPDATE_PROFILE_URL + "?user_id=" + user_id
                +"&firstname=" + firstName
                +"&lastname="+lastName
                +"&email="+email
                +"&isbusinessowner="+role
                +"&Gender="+gender
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

    public static JSONObject jsonHours(String day, String openTime, String closeTime) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("open", openTime);
            obj.put("close", closeTime);
            jsonArray.put(obj);
            jsonObject.put(day, jsonArray);
            Log.e(TAG, "hours: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static ArrayList<String> addLanguages(String owner, String language) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", owner);
            jsonObject.put("language", language);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            for (int i = 0; i < jsonArray.length(); i++) {
                langList.add(jsonArray.get(i).toString());
            }

            Log.e(TAG, "languages: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return langList;
    }

    public static String getListingParams(int category_id, String lang, String user_id) {
        String params = Config.LISTING_URL + "?cat_id="+category_id
                +"&lang="+lang
                +"&user_id="+user_id;

        Log.e(TAG, "listing params-- "+params);

        return params;
    }

    public static String getListingDetailsParams(int category_id, String lang, String user_id) {
        String params = Config.LISTING_DETAILS_URL + "?listing_id="+category_id
                +"&lang="+lang
                +"&user_id="+user_id;

        Log.e(TAG, "listing_details params-- "+params);

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

    public static String getSearchParams(String query, String lat, String lng,
                                         String radius, String lang, String user_id) {
        String params = Config.SEARCH_URL + "?listing_title="+query
                +"&lat="+lat
                +"&lng="+lng
                +"&radius="+radius
                +"&lang="+lang
                +"&user_id="+user_id;

        Log.e(TAG, "search_listing params-- "+params);

        return params;
    }

    public static String getAddReviewsParams(String user_id, String listing_id, String comment,
                                             float speed, float qualtiy, float price, String lang) {
        String params = Config.ADD_REVIEW_URL + "?user_id="+user_id
                +"&listing_id="+listing_id
                +"&comment="+comment
                +"&speed="+speed
                +"&quality="+qualtiy
                +"&price="+price
                +"&lang="+lang;

        Log.e(TAG, "add_review params-- "+params);

        return params;
    }

    public static String getReviewsParams(String listing_id, String lang) {
        String params = Config.REVIEWS_URL+"?listing_id="+listing_id
                +"&lang="+lang;

        Log.e(TAG, "reviews params-- "+params);

        return params;
    }
}
