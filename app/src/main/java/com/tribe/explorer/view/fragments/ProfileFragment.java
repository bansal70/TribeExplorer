package com.tribe.explorer.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tribe.explorer.R;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    ImageView imgProfilePic, imgBack;
    TextView tvEditProfile, tvUpdatePassword;
    EditText editFirstName, editLastName, editEmail,
            editBirthDate, editGender;
    RadioGroup rgOwner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        imgBack = view.findViewById(R.id.imgBack);

        imgProfilePic = view.findViewById(R.id.imgProfilePic);
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editEmail = view.findViewById(R.id.editEmail);
        rgOwner = view.findViewById(R.id.rgOwner);
        editBirthDate = view.findViewById(R.id.editBirthDate);
        editGender = view.findViewById(R.id.editGender);

        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        tvUpdatePassword = view.findViewById(R.id.tvUpdatePassword);

        imgBack.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);
        tvUpdatePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                backScreen();
                break;
        }
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

}