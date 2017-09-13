package com.tribe.explorer.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    TextView tvLogin, tvGuest, tvForgotPassword, tvSignUp;
    EditText editEmail, editPassword;
    CheckBox cbRemember;
    String deviceToken, lang;
    private Dialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    public void initViews() {
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        lang = TEPreferences.readString(this, "lang");
        pd = Utils.showDialog(this);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvGuest = (TextView) findViewById(R.id.tvGuest);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        cbRemember = (CheckBox) findViewById(R.id.cbRemember);

        tvLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvGuest.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
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
                break;

            case R.id.tvForgotPassword:
                break;
        }
    }

    public void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.please_fill_data, Toast.LENGTH_SHORT).show();
            return;
        }
        pd.show();
        ModelManager.getInstance().getLoginManager().loginTask(this, Operations
                .getLoginParams(email, password, deviceToken, "android", lang));
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
                startActivity(new Intent(this, MainActivity.class));
                break;

            case Constants.LOGIN_ERROR:
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(this, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
