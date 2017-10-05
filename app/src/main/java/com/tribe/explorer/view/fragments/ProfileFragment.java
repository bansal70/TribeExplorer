package com.tribe.explorer.view.fragments;

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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.controller.ProfileManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.ProfileData;
import com.tribe.explorer.model.custom.CircleTransform;
import com.tribe.explorer.model.custom.ImagePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    View view;
    CircleImageView imgProfilePic;
    ImageView imgBack;
    TextView tvEditProfile, tvUpdatePassword;
    EditText editFirstName, editLastName, editEmail, editGender;
    EditText editOldPassword, editNewPassword, editConfirmPassword;
    TextView tvConfirmPassword, tvCancelPassword;
    TextView editBirthDate;
    RadioGroup rgOwner;
    RadioButton rbYes, rbNo;
    private Dialog dialog, dialogEdit, dialogPassword;
    TextView tvConfirm, tvCancel;
    String user_id, lang, role, filePath="";
    boolean isEdit = false;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        user_id = TEPreferences.readString(getActivity(), "user_id");
        lang = TEPreferences.readString(getActivity(), "lang");
        ModelManager.getInstance().getProfileManager().profileTask(Operations
                .getProfileParams(user_id, lang));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            initViews(view);
        }

        return view;
    }

    public void initViews(View view) {
        imgBack = view.findViewById(R.id.imgBack);

        imgProfilePic = view.findViewById(R.id.imgProfilePic);
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editEmail = view.findViewById(R.id.editEmail);
        editBirthDate = view.findViewById(R.id.editBirthDate);
        editGender = view.findViewById(R.id.editGender);
        rgOwner = view.findViewById(R.id.rgOwner);
        rbYes = view.findViewById(R.id.rbYes);
        rbNo = view.findViewById(R.id.rbNo);

        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        tvUpdatePassword = view.findViewById(R.id.tvUpdatePassword);

        imgBack.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);
        tvUpdatePassword.setOnClickListener(this);
        editBirthDate.setOnClickListener(this);

        editFields(false);
        initDialog();
        selectOwner();
        initPasswordDialog();
    }

    public void initDialog() {
        dialogEdit = Utils.createDialog(getActivity(), R.layout.dialog_alert);
        tvCancel = dialogEdit.findViewById(R.id.tvCancel);
        tvConfirm = dialogEdit.findViewById(R.id.tvConfirm);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
    }

    public void initPasswordDialog() {
        dialogPassword = Utils.createDialog(getActivity(), R.layout.dialog_change_password);
        editOldPassword = dialogPassword.findViewById(R.id.editOldPassword);
        editNewPassword = dialogPassword.findViewById(R.id.editNewPassword);
        editConfirmPassword = dialogPassword.findViewById(R.id.editConfirmPassword);

        tvConfirmPassword = dialogPassword.findViewById(R.id.tvConfirmPassword);
        tvCancelPassword = dialogPassword.findViewById(R.id.tvCancelPassword);

        tvConfirmPassword.setOnClickListener(this);
        tvCancelPassword.setOnClickListener(this);
    }

    public void setProfileData() {
        tvEditProfile.setText(R.string.edit);
        ProfileData.Data data = ProfileManager.data;
        editFirstName.setText(data.firstName);
        editLastName.setText(data.lastName);
        editEmail.setText(data.userEmail);
        editBirthDate.setText(data.dob);
        editGender.setText(data.gender);

        if (data.role.equals("1"))
            rbYes.setChecked(true);
        else
            rbNo.setChecked(true);

        TEPreferences.putString(getActivity(), "image", data.userUrl);
        Glide.with(getActivity())
                .load(data.userUrl)
                .placeholder(R.mipmap.ic_user)
                .transform(new CircleTransform(getContext()))
                .into(imgProfilePic);
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

    public void editProfile() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String birthDate = editBirthDate.getText().toString().trim();
        String gender = editGender.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || role.isEmpty()) {
            Toast.makeText(getActivity(), R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
        } else if (!gender.isEmpty() && !(gender.equalsIgnoreCase("male")
                || gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("other"))) {
            Toast.makeText(getActivity(), R.string.gender_male_female, Toast.LENGTH_SHORT).show();
        } else {
            ModelManager.getInstance().getProfileManager().editProfileTask(Operations
                    .getUpdateProfileParams(user_id, firstName, lastName, email, role, gender,
                            birthDate, lang), filePath);
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                backScreen();
                break;

            case R.id.editBirthDate:
                Utils.datePicker(getContext(), editBirthDate);
                break;

            case R.id.tvEditProfile:
                if (isEdit)
                    editProfile();
                else
                    dialogEdit.show();
                break;

            case R.id.tvConfirm:
                editFields(true);
                dialogEdit.dismiss();
                tvEditProfile.setText(R.string.submit);
                break;

            case R.id.tvCancel:
                dialogEdit.dismiss();
                break;

            case R.id.imgProfilePic:
                dispatchTakePictureIntent();
                break;

            case R.id.tvUpdatePassword:
                dialogPassword.show();
                break;

            case R.id.tvConfirmPassword:
                changePassword();
                break;

            case R.id.tvCancelPassword:
                dialogPassword.dismiss();
                break;
        }
    }

    public void changePassword() {
        String oldPass = editOldPassword.getText().toString().trim();
        String newPass = editNewPassword.getText().toString().trim();
        String confirmPass = editConfirmPassword.getText().toString().trim();
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty())
            Toast.makeText(getActivity(), R.string.fill_required_fields, Toast.LENGTH_SHORT).show();
        else if (!newPass.equals(confirmPass))
            Toast.makeText(getActivity(), R.string.password_unmatch, Toast.LENGTH_SHORT).show();
        else if (newPass.length() < 6)
            Toast.makeText(getActivity(), R.string.atleast_6_digits_password, Toast.LENGTH_SHORT).show();
        else {
            dialog.show();
            ModelManager.getInstance().getChangePasswordManager().changePassword(
                    Operations.getUpdatePasswordParams(user_id, newPass, oldPass, lang));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(Event event) {
        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.PROFILE_SUCCESS:
                dialog.dismiss();
                setProfileData();
                editFields(false);
                break;

            case Constants.PROFILE_EMPTY:
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.invalid_user, Toast.LENGTH_SHORT).show();
                break;

            case Constants.EDIT_PROFILE_SUCCESS:
                ModelManager.getInstance().getProfileManager().profileTask(Operations
                        .getProfileParams(user_id, lang));
                Toast.makeText(getActivity(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
                break;

            case Constants.EDIT_PROFILE_EMPTY:
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.failed_profile_update, Toast.LENGTH_SHORT).show();
                break;

            case Constants.CHANGE_PASSWORD_SUCCESS:
                dialogPassword.dismiss();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.password_updated, Toast.LENGTH_SHORT).show();
                break;

            case Constants.CHANGE_PASSWORD_FAILED:
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.incorrect_password, Toast.LENGTH_SHORT).show();
                break;


            case Constants.NO_RESPONSE:
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void editFields(boolean status) {
        editFirstName.setEnabled(status);
        editLastName.setEnabled(status);
        editEmail.setEnabled(status);
        editBirthDate.setEnabled(status);
        editGender.setEnabled(status);
        rbYes.setEnabled(status);
        rbNo.setEnabled(status);
        imgProfilePic.setEnabled(status);
        isEdit = status;
    }

    private void dispatchTakePictureIntent() {

        if (Build.VERSION.SDK_INT >= M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        } else {
            chooseImage();
        }
    }

    public void chooseImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
        startActivityForResult(chooseImageIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            chooseImage();
        } else {
            Toast.makeText(getActivity(), R.string.grant_permissions, Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap photo = ImagePicker.getImageFromResult(getActivity(), resultCode, data);

            Uri tempUri = ImagePicker.getImageUri(getActivity(), photo);
            File finalFile = new File(ImagePicker.getRealPathFromURI(getActivity(), tempUri));
            filePath = finalFile.getAbsolutePath();
            Glide.with(getContext())
                    .load(filePath)
                    .transform(new CircleTransform(getContext()))
                    .placeholder(R.mipmap.ic_user)
                    .into(imgProfilePic);
            TEPreferences.putString(getActivity(), "image", filePath);

            //imgProfilePic.setImageBitmap(photo);
        }
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}