package com.tribe.explorer.view.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.custom.Captcha;
import com.tribe.explorer.model.custom.ImagePicker;
import com.tribe.explorer.model.custom.TextCaptcha;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.M;

public class AddReviewFragment extends Fragment implements View.OnClickListener{

    private Dialog dialog;
    RatingBar rbSpeed, rbQuality, rbPrice;
    EditText editComment, editCaptcha;
    String user_id, lang, listing_id;
    Button btSubmitReview;
    ImageView imgBack, imgCaptcha;
    TextView tvImage, tvImagePath, tvReload;
    Captcha captcha;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String filePath = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listing_id = bundle.getString("listing_id");
        }
        user_id = TEPreferences.readString(getActivity(), "user_id");
        lang = TEPreferences.readString(getActivity(), "lang");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        dialog = Utils.showDialog(getActivity());
        rbSpeed = view.findViewById(R.id.rbSpeed);
        rbQuality = view.findViewById(R.id.rbQuality);
        rbPrice = view.findViewById(R.id.rbPrice);
        tvImage = view.findViewById(R.id.tvImage);
        tvImagePath = view.findViewById(R.id.tvImagePath);
        tvReload = view.findViewById(R.id.tvReload);
        editCaptcha = view.findViewById(R.id.editCaptcha);
        editComment = view.findViewById(R.id.editComment);
        imgCaptcha = view.findViewById(R.id.imgCaptcha);
        imgBack = view.findViewById(R.id.imgBack);
        btSubmitReview = view.findViewById(R.id.btSubmitReview);

        btSubmitReview.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvReload.setOnClickListener(this);
        tvImage.setOnClickListener(this);
        setImageCaptcha();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvImage:
                dispatchTakePictureIntent();
                break;

            case R.id.tvReload:
                setImageCaptcha();
                break;

            case R.id.btSubmitReview:
                float speed = rbSpeed.getRating();
                float quality = rbQuality.getRating();
                float price = rbPrice.getRating();
                String comment = editComment.getText().toString();
                String str_captcha = editCaptcha.getText().toString().trim();

                boolean isCaptcha = captcha.checkAnswer(str_captcha);

                if (speed == 0 || quality == 0 || price == 0) {
                    Toast.makeText(getActivity(), R.string.rating_can_not_be_empty, Toast.LENGTH_SHORT).show();
                } else if (comment.trim().isEmpty())
                    Toast.makeText(getActivity(), R.string.add_comment, Toast.LENGTH_SHORT).show();
                else if (!isCaptcha) {
                    Toast.makeText(getActivity(), R.string.wrong_captcha, Toast.LENGTH_SHORT).show();
                    setImageCaptcha();
                    editCaptcha.setText("");
                } else {
                    dialog.show();
                    ModelManager.getInstance().getAddReviewManager().addReviewTask(Operations
                            .getAddReviewsParams(user_id, listing_id, comment, speed,
                                    quality, price, lang), filePath);
                }
                break;

            case R.id.imgBack:
                backScreen();
                break;
        }
    }

    public void setImageCaptcha() {
        captcha = new TextCaptcha(300, 100, 5, TextCaptcha.TextOptions.NUMBERS_AND_LETTERS);
        imgCaptcha.setImageBitmap(captcha.getImage());
        imgCaptcha.setLayoutParams(new LinearLayout.LayoutParams(captcha.getWidth() *2,
                captcha.getHeight() *2));
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
        dialog.dismiss();
        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.ADD_REVIEW_SUCCESS:
                backScreen();
                Toast.makeText(getActivity(), R.string.review_added, Toast.LENGTH_SHORT).show();
                break;

            case Constants.ADD_REVIEW_ERROR:
                Toast.makeText(getActivity(), R.string.add_review_failed, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
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

            tvImagePath.setText(filePath);
        }
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
