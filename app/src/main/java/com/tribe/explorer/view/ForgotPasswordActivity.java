package com.tribe.explorer.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editEmail;
    TextView tvSubmit;
    private Dialog dialog;
    private String lang;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
    }

    public void initViews() {
        lang = TEPreferences.readString(this, "lang");
        dialog = Utils.showDialog(this);
        editEmail = (EditText) findViewById(R.id.editEmail);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        tvSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                submitEmail();
                break;

            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void submitEmail() {
        String email = editEmail.getText().toString();
        if (email.isEmpty() || !Utils.emailValidator(email)) {
            Toast.makeText(this, R.string.invalid_email_address, Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        ModelManager.getInstance().getForgotPasswordManager().passwordTask(Operations
                .getForgotPassParams(email, lang));
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
        dialog.dismiss();
        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.FORGOT_PASSWORD_SUCCESS:
                Toast.makeText(this, R.string.password_sent, Toast.LENGTH_SHORT).show();
                break;

            case Constants.FORGOT_PASSWORD_FAILED:
                Toast.makeText(this, R.string.enter_registered_email, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(this, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
