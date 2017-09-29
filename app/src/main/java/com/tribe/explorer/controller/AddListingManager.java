package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/14/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.custom.ImagePicker;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddListingManager {
    private static final String TAG = AddListingManager.class.getSimpleName();

    public void addListingTask(Context context, String params, Uri uriLogo,
                               Uri uriCover, ArrayList<Uri> uriGallery) {
        File logoFile = null, coverFile = null;
        RequestBody requestLogo = null, requestCover;
        MultipartBody.Part bodyLogo = null, bodyCover = null;
        MultipartBody.Part[] bodyGalleries = new MultipartBody.Part[0];
        if (uriLogo != null)
            logoFile = new File(ImagePicker.getRealPathFromURI(context, uriLogo));
        if (uriCover != null)
            coverFile = new File(ImagePicker.getRealPathFromURI(context, uriCover));

        if (logoFile != null) {
            requestLogo = RequestBody.create(MediaType.parse("multipart/form-data"), logoFile);
            bodyLogo = MultipartBody.Part.createFormData("company_avatar",
                    logoFile.getName(), requestLogo);
        }
        if (coverFile != null) {
            requestCover = RequestBody.create(MediaType.parse("multipart/form-data"), coverFile);
            bodyCover = MultipartBody.Part.createFormData("cover_image",
                    coverFile.getName(), requestCover);
        }
        if (!uriGallery.isEmpty()) {
            bodyGalleries = new MultipartBody.Part[uriGallery.size()];

            for (int i=0; i<uriGallery.size(); i++) {
                File galleryFile = new File(ImagePicker.getRealPathFromURI(context, uriGallery.get(i)));;
                RequestBody requestGallery = RequestBody.create(MediaType.parse("multipart/form-data"),
                        galleryFile);
                bodyGalleries[i] = MultipartBody.Part.createFormData("gallery_images"+ ++i,
                        galleryFile.getName(), requestGallery);
            }
        }

        final APIService apiInterface = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> resultCall = apiInterface.addListing(params,
                bodyLogo, bodyCover, bodyGalleries);
        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String output = response.body().string();
                    Log.e(TAG, "add_listing response-- "+output);
                    JSONObject jsonObject = new JSONObject(output);
                    String status = jsonObject.getString("status");

                    if (status.equals("success"))
                        EventBus.getDefault().postSticky(new Event(Constants.ADD_LISTING_SUCCESS, ""));
                    else
                        EventBus.getDefault().postSticky(new Event(Constants.ADD_LISTING_FAILED, ""));

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
