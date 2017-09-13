package com.tribe.explorer.model.network;

/*
 * Created by rishav on 9/12/2017.
 */

import com.tribe.explorer.model.beans.CategoriesData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface APIService {

    @POST
    Call<ResponseBody> response(@Url String string);

    @Multipart
    @POST
    Call<ResponseBody> registerUser(@Url String string, @Part MultipartBody.Part file);

    @POST
    Call<CategoriesData> categories(@Url String string);
}
