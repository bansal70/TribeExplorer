package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/22/2017.
 */

import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.beans.RegionData;
import com.tribe.explorer.model.beans.TimezoneData;
import com.tribe.explorer.model.network.APIClient;
import com.tribe.explorer.model.network.APIService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegionManager {
    private static final String TAG = RegionManager.class.getSimpleName();

    public static List<RegionData.Data> regionsList = new ArrayList<>();
    public static List<String> timezoneList = new ArrayList<>();

    public void regionTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<RegionData> call = apiService.getRegion(params);
        call.enqueue(new Callback<RegionData>() {
            @Override
            public void onResponse(Call<RegionData> call, Response<RegionData> response) {
                try {
                    RegionData regionData = response.body();
                    String status = regionData.status;
                    if (status.equalsIgnoreCase("success")) {
                        regionsList = regionData.data;
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.REGION_EMPTY, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }

            }

            @Override
            public void onFailure(Call<RegionData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }

    public void timezoneTask(String params) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<TimezoneData> call = apiService.getTimezone(params);
        call.enqueue(new Callback<TimezoneData>() {
            @Override
            public void onResponse(Call<TimezoneData> call, Response<TimezoneData> response) {
                try {
                    TimezoneData timezoneData = response.body();
                    String status = timezoneData.status;
                    if (status.equalsIgnoreCase("success")) {
                        timezoneList = timezoneData.data;
                    } else {
                        EventBus.getDefault().postSticky(new Event(Constants.TIMEZONE_EMPTY, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
                }

            }

            @Override
            public void onFailure(Call<TimezoneData> call, Throwable t) {
                t.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }
}
