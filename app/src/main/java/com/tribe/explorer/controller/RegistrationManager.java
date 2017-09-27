package com.tribe.explorer.controller;

/*
 * Created by win 10 on 9/12/2017.
 */

import android.util.Log;

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationManager {
    private static final String TAG = RegistrationManager.class.getSimpleName();

    public void registerTask(String params, String filePath) {
        File file = new File(filePath);
        APIService apiService = APIClient.getClient().create(APIService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("myfile",
                file.getName(), requestFile);

        Call<ResponseBody> call = apiService.registerUser(params, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    Log.e(TAG, "registration response-- "+output);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success"))
                        EventBus.getDefault().postSticky(new Event(Constants.REGISTRATION_SUCCESS, ""));
                    else
                        EventBus.getDefault().postSticky(new Event(Constants.REGISTRATION_ERROR, ""));
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
