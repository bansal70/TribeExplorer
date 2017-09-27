package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/26/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.ReviewsData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsManager {
    private static final String TAG = ReviewsManager.class.getSimpleName();
    public static List<ReviewsData.Data> reviewsList = new ArrayList<>();

    public void reviewsTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<ReviewsData> call = apiService.getReviews(params);
        call.enqueue(new Callback<ReviewsData>() {
            @Override
            public void onResponse(Call<ReviewsData> call, Response<ReviewsData> response) {
                try {
                    ReviewsData reviewsData = response.body();
                    String status = reviewsData.status;
                    if (status.equalsIgnoreCase("success")) {
                        reviewsList = reviewsData.data;
                        EventBus.getDefault().postSticky(new Event(Constants.REVIEW_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.REVIEW_ERROR, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }
            }

            @Override
            public void onFailure(Call<ReviewsData> call, Throwable t) {
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
