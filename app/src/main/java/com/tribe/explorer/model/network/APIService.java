package com.tribe.explorer.model.network;

/*
 * Created by rishav on 9/12/2017.
 */

import com.tribe.explorer.model.beans.AboutData;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.model.beans.LanguageData;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.model.beans.ProfileData;

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

    @POST
    Call<LanguageData> languages(@Url String string);

    @Multipart
    @POST
    Call<ResponseBody> addListing(@Url String string, @Part MultipartBody.Part coverFile,
                                  @Part MultipartBody.Part[] galleryFiles);

    @POST
    Call<ListingData> listings(@Url String string);

    @POST
    Call<AboutData> about(@Url String string);

    @POST
    Call<ProfileData> profile(@Url String string);

    @Multipart
    @POST
    Call<ResponseBody> editProfile(@Url String string, @Part MultipartBody.Part file);

}
