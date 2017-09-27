package com.tribe.explorer.model.network;

/*
 * Created by rishav on 9/12/2017.
 */

import com.tribe.explorer.model.beans.AboutData;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.model.beans.HoursData;
import com.tribe.explorer.model.beans.LabelsData;
import com.tribe.explorer.model.beans.LanguageData;
import com.tribe.explorer.model.beans.ListingData;
import com.tribe.explorer.model.beans.ListingDetailsData;
import com.tribe.explorer.model.beans.ProfileData;
import com.tribe.explorer.model.beans.RegionData;
import com.tribe.explorer.model.beans.ReviewsData;
import com.tribe.explorer.model.beans.TimezoneData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {

    @POST
    Call<ResponseBody> response(@Url String string);

    @POST
    Call<ResponseBody> facebookLogin(@Url String string, @Query("image") String image);

    @Multipart
    @POST
    Call<ResponseBody> registerUser(@Url String string, @Part MultipartBody.Part file);

    @POST
    Call<CategoriesData> categories(@Url String string);

    @POST
    Call<LanguageData> languages(@Url String string);

    @POST
    Call<RegionData> getRegion(@Url String string);

    @POST
    Call<TimezoneData> getTimezone(@Url String string);

    @POST
    Call<LabelsData> getLabels(@Url String string);

    @Multipart
    @POST
    Call<ResponseBody> addListing(@Url String string, @Part MultipartBody.Part coverFile,
                                  @Part MultipartBody.Part[] galleryFiles,
                                  @Body HoursData hoursData);

    @POST
    Call<ListingData> listings(@Url String string);

    @POST
    Call<ListingDetailsData> listingsDetails(@Url String string);

    @POST
    Call<ReviewsData> getReviews(@Url String string);

    @POST
    Call<AboutData> about(@Url String string);

    @POST
    Call<ProfileData> profile(@Url String string);

    @Multipart
    @POST
    Call<ResponseBody> editProfile(@Url String string, @Part MultipartBody.Part file);

}
