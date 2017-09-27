package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/15/2017.
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

public class FavouriteManager {
    private static final String TAG = FavouriteManager.class.getSimpleName();

    public void favouriteTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);


        Call<ResponseBody> call = apiService.response(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    Log.e(TAG, "favorite response-- "+output);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success"))
                        EventBus.getDefault().postSticky(new Event(Constants.FAVOURITE_SUCCESS, ""));
                    else
                        EventBus.getDefault().postSticky(new Event(Constants.FAVOURITE_ERROR, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });

    }
}
