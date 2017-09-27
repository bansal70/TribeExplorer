package com.tribe.explorer.controller;

/*
 * Created by rishav on 9/26/2017.
 */

import android.app.Activity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tribe.explorer.R;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

public class TwitterLoginManager {
    private String lang, deviceToken;

    public void getTwitterData(final Activity context) {
        lang = TEPreferences.readString(context, "lang");
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        TwitterConfig config = new TwitterConfig.Builder(context)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(context.getString(R.string.CONSUMER_KEY),
                        context.getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        AccountService accountService = twitterApiClient.getAccountService();
        Call<User> call = accountService.verifyCredentials(true, true, true);
        call.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                User user = result.data;
                String name = user.name;
                String twitterID = String.valueOf(user.getId());
                String imageUrl = user.profileImageUrl;

                String userEmail = "";
                if (user.email != null)
                    userEmail = user.email;

                String firstName = "", lastName = "";
                if (name.contains(" ")) {
                    String[] split = name.split(" ");
                    firstName = split[0];
                    lastName = split[1];
                } else {
                    firstName = name;
                }
                ModelManager.getInstance().getLoginManager().socialLoginTask(context,
                        Operations.getSocialLoginParams(firstName, lastName, userEmail, twitterID,
                                deviceToken, "android", "twitter", lang), imageUrl);
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                EventBus.getDefault().postSticky(new Event(Constants.NO_RESPONSE, ""));
            }
        });
    }

}

