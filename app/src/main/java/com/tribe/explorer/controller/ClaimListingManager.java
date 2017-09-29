package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/27/2017.
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

public class ClaimListingManager {
    private final String TAG = ClaimListingManager.class.getSimpleName();

    public void claimListingTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);

        Call<ResponseBody> call = apiService.response(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    Log.e(TAG, "claim_listing response-- "+output);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success"))
                        EventBus.getDefault().postSticky(new Event(Constants.CLAIMED_SUCCESS, ""));
                    else
                        EventBus.getDefault().postSticky(new Event(Constants.CLAIMED_ERROR, ""));
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
