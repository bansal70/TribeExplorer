package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/14/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.custom.ImagePicker;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        Call<ResponseBody> resultCall;
        final APIService apiInterface = APIClient.getClient().create(APIService.class);
        File logoFile, coverFile;
        RequestBody requestLogo, requestCover;
        MultipartBody.Part bodyLogo = null, bodyCover = null;
        MultipartBody.Part[] bodyGalleries = new MultipartBody.Part[0];
        Log.e(TAG, "uri:" + uriLogo + "\nuri cover: "+uriCover + "\nuri gallery: "
                +uriGallery);

        if (uriCover != null) {
            coverFile = new File(ImagePicker.getRealPathFromURI(context, uriCover));
            requestCover = RequestBody.create(MediaType.parse("multipart/form-data"), coverFile);
            bodyCover = MultipartBody.Part.createFormData("cover_image",
                    coverFile.getName(), requestCover);
        }
        if (uriLogo != null) {
            logoFile = new File(ImagePicker.getRealPathFromURI(context, uriLogo));
            requestLogo = RequestBody.create(MediaType.parse("multipart/form-data"), logoFile);
            bodyLogo = MultipartBody.Part.createFormData("company_avatar",
                    logoFile.getName(), requestLogo);
        }
        if (!uriGallery.isEmpty()) {
            bodyGalleries = new MultipartBody.Part[uriGallery.size()];
            int j = 1;
            for (int i=0; i<uriGallery.size(); i++) {
                File galleryFile = new File(ImagePicker.getRealPathFromURI(context, uriGallery.get(i)));
                RequestBody requestGallery = RequestBody.create(MediaType.parse("multipart/form-data"),
                        galleryFile);
                bodyGalleries[i] = MultipartBody.Part.createFormData("gallery_images"+ j,
                        galleryFile.getName(), requestGallery);
                j++;
            }
        }

        if (uriLogo == null && uriCover == null && uriGallery.isEmpty())
            resultCall = apiInterface.response(params);
        else
            resultCall = apiInterface.addListing(params, bodyLogo, bodyCover, bodyGalleries);

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

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String root = Environment.getExternalStorageDirectory().toString();
            File copyFile = new File(root + File.separator + context.getString(R.string.app_name) +
                     File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
