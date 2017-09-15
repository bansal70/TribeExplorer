package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/15/2017.
 */

import android.util.Log;

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.ProfileData;
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

public class ProfileManager {
    private static final String TAG = ProfileManager.class.getSimpleName();
    public static ProfileData.Data data;

    public void profileTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);

        Call<ProfileData> call = apiService.profile(params);
        call.enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                try {
                    ProfileData profileData = response.body();
                    String status = profileData.status;
                    Log.e(TAG, "profile status-- "+status);
                    if (status.equalsIgnoreCase("success")) {
                        data = profileData.data;
                        EventBus.getDefault().postSticky(new Event(Constants.PROFILE_SUCCESS, ""));
                    }
                    else {
                        EventBus.getDefault().postSticky(new Event(Constants.PROFILE_EMPTY, ""));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }
            }

            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }

    public void editProfileTask(String params, String filePath) {
        Call<ResponseBody> resultCall;
        APIService apiInterface = APIClient.getClient().create(APIService.class);

        if (!filePath.isEmpty()) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("profilePic", file.getName(), requestFile);
            resultCall = apiInterface.editProfile(params, body);
        } else {
            resultCall = apiInterface.response(params);
        }

        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    JSONObject jsonObject = new JSONObject(output);
                    Log.e(TAG, "edit_profile response-- "+output);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("success"))
                        EventBus.getDefault().postSticky(new Event(Constants.EDIT_PROFILE_SUCCESS, ""));
                    else
                        EventBus.getDefault().postSticky(new Event(Constants.EDIT_PROFILE_EMPTY, ""));
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
