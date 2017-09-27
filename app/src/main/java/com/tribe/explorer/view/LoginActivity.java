package com.tribe.explorer.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    TextView tvLogin, tvGuest, tvForgotPassword, tvSignUp, tvFbLogin, tvTwitterLogin;
    EditText editEmail, editPassword;
    CheckBox cbRemember;
    String deviceToken, lang;
    private Dialog pd;
    boolean isChecked = false;
    CallbackManager callbackManager;
    TwitterAuthClient mTwitterAuthClient ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.CONSUMER_KEY),
                        getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    public void initViews() {
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        lang = TEPreferences.readString(this, "lang");
        pd = Utils.showDialog(this);
        callbackManager = CallbackManager.Factory.create();

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvGuest = (TextView) findViewById(R.id.tvGuest);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);
        tvFbLogin = (TextView) findViewById(R.id.tvFbLogin);
        tvTwitterLogin = (TextView) findViewById(R.id.tvTwitterLogin);

        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvGuest.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvFbLogin.setOnClickListener(this);
        tvTwitterLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                loginUser();
                break;

            case R.id.tvSignUp:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;

            case R.id.tvGuest:
                startActivity(new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;

            case R.id.tvForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

            case R.id.tvFbLogin:
                ModelManager.getInstance().getFacebookLoginManager()
                        .doFacebookLogin(this, callbackManager);
                break;

            case R.id.tvTwitterLogin:
                mTwitterAuthClient = new TwitterAuthClient();
                mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {

                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
                break;
        }
    }

    public void loginUser() {
        isChecked = cbRemember.isChecked();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.please_fill_data, Toast.LENGTH_SHORT).show();
            return;
        }
        pd.show();
        ModelManager.getInstance().getLoginManager().loginTask(this, Operations
                .getLoginParams(email, password, deviceToken, "android", lang), isChecked);
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(Event event) {
        EventBus.getDefault().removeAllStickyEvents();
        pd.dismiss();
        switch (event.getKey()) {
            case Constants.LOGIN_SUCCESS:
                startActivity(new Intent(this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;

            case Constants.LOGIN_ERROR:
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                break;

            case Constants.ALREADY_REGISTERED:
                Toast.makeText(this, R.string.email_already_registered, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(this, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE){
            pd.show();
            ModelManager.getInstance().getTwitterLoginManager().getTwitterData(this);
        } else if (resultCode == RESULT_OK) {
            pd.show();
            ModelManager.getInstance().getFacebookLoginManager().getFacebookData(this);
        }
    }
}
