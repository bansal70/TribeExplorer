package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/11/2017.
 */

import android.content.Context;
import android.util.Log;

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class SimpleLoginManager {

    public void loginTask(final Context context, String params, final boolean isChecked) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> call = apiService.response(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String user_id = data.getString("user_id");
                        String first_name = data.getString("first_name");
                        String last_name = data.getString("last_name");
                        String image = data.getString("user_url");
                        String email = data.getString("email");
                        String isOwner = data.getString("businessowner");

                        if (isChecked) {
                            TEPreferences.putString(context, "user_id", user_id);
                            TEPreferences.putString(context, "first_name", first_name);
                            TEPreferences.putString(context, "last_name", last_name);
                            TEPreferences.putString(context, "email", email);
                            TEPreferences.putString(context, "isOwner", isOwner);
                            TEPreferences.putString(context, "image", image);
                        }

                        EventBus.getDefault().postSticky(new Event(Constants.LOGIN_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.LOGIN_ERROR, ""));
                     }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }

    public void socialLoginTask(final Context context, String params, String image) {
        APIService apiInterface = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> call = apiInterface.facebookLogin(params, image);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    Log.e(TAG, "Login response-- "+output);
                    JSONObject jsonObject = new JSONObject(output);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String user_id = data.getString("user_id");
                        String first_name = data.getString("first_name");
                        String last_name = data.getString("last_name");
                      //  String image = data.getString("image");
                        String email = data.getString("email");

                        TEPreferences.putString(context, "user_id", user_id);
                        TEPreferences.putString(context, "first_name", first_name);
                        TEPreferences.putString(context, "last_name", last_name);
                      //  TEPreferences.putString(context, "profile_pic", image);
                        TEPreferences.putString(context, "email", email);

                        EventBus.getDefault().postSticky(new Event(Constants.LOGIN_SUCCESS, ""));
                    }
                    else
                        EventBus.getDefault().postSticky(new Event(Constants.ALREADY_REGISTERED, ""));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
