package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/25/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FacebookLoginManager {
    private static final String TAG = FacebookLoginManager.class.getSimpleName();
    private String deviceToken = "";
    private String lang;

    public void doFacebookLogin(final Activity context, CallbackManager callbackManager) {
        lang = TEPreferences.readString(context, "lang");
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(context,
                Arrays.asList("email", "user_friends", "public_profile"));

        com.facebook.login.LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String user_id = loginResult.getAccessToken().getUserId();

                        Log.e(TAG, "Id: " + user_id);
                        Log.e(TAG, "Token: " + loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        exception.printStackTrace();
                        EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                    }
                });
    }

    public void getFacebookData(final Activity context) {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,picture.type(large)");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params,
                HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response != null) {
                    try {
                        JSONObject data = response.getJSONObject();
                        Log.e(TAG, "Data: " + data);
                        String id = data.getString("id");
                        String name = data.getString("name");
                        String email = "";
                        String[] splitName = name.split(" ");
                        String firstName = splitName[0];
                        String lastName = splitName[1];

                        try {
                            email = data.getString("email");
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        String profilePicUrl = "";
                        Log.e(TAG, "user_id-- " + id);
                        if (data.has("picture")) {
                            profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");

                            Log.e(TAG, "Image: " + profilePicUrl);
                        }

                        ModelManager.getInstance().getLoginManager().socialLoginTask(context,
                                Operations.getSocialLoginParams(firstName, lastName, email, id,
                                        deviceToken, "android", "facebook", lang), profilePicUrl);

                    } catch (Exception e) {
                        e.printStackTrace();
                        EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                    }
                }
            }
        }).executeAsync();
    }

}
