package com.tribe.explorer.controller;

/*
 * Created by rishav on 10/5/2017.
 */

import android.util.Log;

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordManager {
    private final String TAG = ChangePasswordManager.class.getSimpleName();

    public void changePassword(String params) {
        APIService apiInterface = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> call = apiInterface.response(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String status = response.body().string();
                    Log.e(TAG, "change_password response-- "+status);
                    JSONObject jsonObject = new JSONObject(status);
                    String output = jsonObject.getString("status");

                    if (output.equalsIgnoreCase("success")) {
                        EventBus.getDefault().postSticky(new Event(Constants.CHANGE_PASSWORD_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.CHANGE_PASSWORD_FAILED, ""));
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
}
