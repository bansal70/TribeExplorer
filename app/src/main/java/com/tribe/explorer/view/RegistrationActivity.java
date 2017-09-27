package com.tribe.explorer.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.custom.Captcha;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.custom.TextCaptcha;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.custom.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import static android.os.Build.VERSION_CODES.M;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener{

    Dialog dialog;
    ImageView imgCaptcha, imgProfilePic, imgBack;
    boolean isCaptcha;
    TextView tvReload, tvSubmit;
    EditText editFirstName, editLastName, editEmail, editPassword, editConfirmPassword,
            editBirthDate, editGender, editCaptcha;
    RadioGroup rgOwner;
    Captcha captcha;
    String deviceToken= "", lang, role= "", filePath= "";

    private static final int PERMISSION_REQUEST_CODE = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();
    }

    public void initViews() {
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        lang = TEPreferences.readString(this, "lang");
        dialog = Utils.showDialog(this);

        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editBirthDate = (EditText) findViewById(R.id.editBirthDate);
        editGender = (EditText) findViewById(R.id.editGender);
        editCaptcha = (EditText) findViewById(R.id.editCaptcha);
        rgOwner = (RadioGroup) findViewById(R.id.rgOwner);

        imgProfilePic = (ImageView)findViewById(R.id.imgProfilePic);
        imgCaptcha = (ImageView)findViewById(R.id.imgCaptcha);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        tvReload = (TextView) findViewById(R.id.tvReload);
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);

        tvReload.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);

        setImageCaptcha();
        selectOwner();
    }

    public void selectOwner() {
        rgOwner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (checkedId == R.id.rbYes)
                    role = "1";
                else
                    role = "0";
            }
        });
    }

    public void setImageCaptcha() {
        captcha = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
        imgCaptcha.setImageBitmap(captcha.getImage());
        imgCaptcha.setLayoutParams(new LinearLayout.LayoutParams(captcha.getWidth() *2,
                captcha.getHeight() *2));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvReload:
                setImageCaptcha();
                break;

            case R.id.imgProfilePic:
                dispatchTakePictureIntent();
                break;

            case R.id.tvSubmit:
                registerUser();
                break;

            case R.id.imgBack:
                finish();
                break;
        }
    }

    public void registerUser() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String birthDate = editBirthDate.getText().toString().trim();
        String gender = editGender.getText().toString().trim();
        String str_captcha = editCaptcha.getText().toString().trim();

        isCaptcha = captcha.checkAnswer(str_captcha);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, R.string.password_length, Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.password_unmatch, Toast.LENGTH_SHORT).show();
        } else if (!gender.isEmpty() &&
                !(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
            Toast.makeText(this, R.string.gender_male_female, Toast.LENGTH_SHORT).show();
        } else if (!isCaptcha) {
            setImageCaptcha();
            Toast.makeText(this, R.string.wrong_captcha, Toast.LENGTH_SHORT).show();
        } else if (filePath.isEmpty()) {
            Toast.makeText(this, R.string.select_pic, Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            ModelManager.getInstance().getRegistrationManager().registerTask
                    (Operations.getRegisterParams(firstName, lastName, email, password, role, gender,
                            birthDate, deviceToken, "android", lang), filePath);
        }
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

    @Subscribe
    public void onEvent(Event event) {
        dialog.dismiss();
        switch (event.getKey()) {
            case Constants.REGISTRATION_SUCCESS:
                finish();
                Toast.makeText(this, R.string.registered_successfully, Toast.LENGTH_SHORT).show();
                break;

            case Constants.REGISTRATION_ERROR:
                Toast.makeText(this, R.string.failed_to_register, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(this, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void dispatchTakePictureIntent() {

        if (Build.VERSION.SDK_INT >= M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        } else {
            chooseImage();
        }
    }

    public void chooseImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            chooseImage();
        } else {
            Toast.makeText(this, R.string.grant_permissions, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap photo = ImagePicker.getImageFromResult(this, resultCode, data);

            Uri tempUri = ImagePicker.getImageUri(this, photo);
            File finalFile = new File(ImagePicker.getRealPathFromURI(this, tempUri));
            filePath = finalFile.getAbsolutePath();

            imgProfilePic.setImageBitmap(photo);
        }
    }
}