package com.tribe.explorer.model;

/*
 * Created by rishav on 9/12/2017.
 */

import android.util.Log;

import com.tribe.explorer.model.beans.Language;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Operations {
    private static final String TAG = Operations.class.getSimpleName();
    public static JSONObject jsonObject = new JSONObject();
    public static JSONArray jsonLanguages = new JSONArray();
    public static ArrayList<String> labelsList = new ArrayList<>();
    public static ArrayList<String> hours = new ArrayList<>();
    public static String hoursOfOperation = "";

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

    public static String getCategoriesParams(String lang, int page) {
        String params = Config.CATEGORIES_URL + "?lang=" + lang
                +"&page=" + page;

        Log.e(TAG, "categories params-- "+params);

        return params;
    }

    public static String getAddListingParams(String user_id, String email, String business_name,
                                             String contact_email, String description, String location,
                                             String website, String video, String phone, String category,
                                             String labels, String region, String hours, String languages,
                                             String timezone, int imgCount, String facebookUrl,
                                             String twitterUrl, String instagramUrl, String lang) {

        String params = Config.ADD_LISTING_URL + "?user_id=" +user_id + "&email=" + email
                + "&listing_name=" + business_name +"&contact_email_Url=" + contact_email
                + "&descrtiption=" + description +"&location=" + location + "&website=" + website
                + "&video_url=" + video +"&Phone_number=" + phone + "&listing_category=" + category
                + "&label=" + labels + "&listing_region=" + region + hours
                + "&language=" + languages + "&timezone=" + timezone
                + "&imgcount=" + imgCount + "&facebook_url=" + facebookUrl + "&twitter_url=" + twitterUrl
                + "&instagram_url=" +instagramUrl + "&lang=" + lang;

        Log.e(TAG, "add_listing params-- "+params);

        return params;
    }

    public static String getEditListingParams(int listing_id, String user_id, String email, String business_name,
                                             String contact_email, String description, String location,
                                             String website, String video, String phone, String category,
                                             String labels, String region, String hours, String languages,
                                             String timezone, int imgCount, String facebookUrl,
                                             String twitterUrl, String instagramUrl, String lang) {

        String params = Config.EDIT_LISTING_URL + "?listing_id=" + listing_id + "&user_id=" +user_id
                + "&email=" + email + "&listing_name=" + business_name +"&contact_email_Url=" + contact_email
                + "&descrtiption=" + description +"&location=" + location + "&website=" + website
                + "&video_url=" + video +"&Phone_number=" + phone + "&listing_category=" + category
                + "&label=" + labels + "&listing_region=" + region + hours
                + "&language=" + languages + "&timezone=" + timezone
                + "&imgcount=" + imgCount + "&facebook_url=" + facebookUrl + "&twitter_url=" + twitterUrl
                + "&instagram_url=" +instagramUrl + "&lang=" + lang;

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
            Log.e(TAG, "view_hours: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONArray addLanguages(List<Language> languageList) {
        for (Language languages :  languageList) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", languages.getOwner());
                jsonObject.put("language", languages.getLanguage());
                jsonLanguages.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonLanguages;
    }

    public static String getListingParams(int category_id, String lang, String user_id, int page) {
        String params = Config.LISTING_URL + "?cat_id="+category_id
                +"&lang=" + lang
                +"&user_id=" + user_id
                +"&page=" + page;

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

    public static String getAddPhotoParams(int listing_id, int imgCount, String lang) {
        String params = Config.ADD_PHOTO_URL + "?listing_id="+listing_id
                +"&imgcount="+imgCount
                +"&lang="+lang;

        Log.e(TAG, "add_photos params-- "+params);

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

    public static String getSearchParams(String query, String lat, String lng, String radius,
                                         String categories, String filter, String order,
                                         String lang, String user_id) {
        String params = Config.SEARCH_URL + "?listing_title="+query
                +"&lat="+lat
                +"&lng="+lng
                +"&radius="+radius
                +"&cat_id="+categories
                +"&label="+filter
                +"&orderby="+order
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

    public static String getClaimParams(int listing_id, String user_id, String lang) {
        String params = Config.CLAIM_LISTING_URL + "?listing_id=" + listing_id
                +"&user_id="+user_id
                +"&lang="+lang;

        Log.e(TAG, "claim_listing params-- "+params);

        return params;
    }

    public static String getDeleteListingParams(int listing_id, String lang) {
        String params = Config.DELETE_LISTING_URL + "?listing_id=" + listing_id
                +"&lang=" + lang;

        Log.e(TAG, "delete_listing params-- "+params);

        return params;
    }
}
