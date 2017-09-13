package com.tribe.explorer.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.model.TEPreferences;

public class MeFragment extends Fragment implements View.OnClickListener{

    TextView tvUser, tvAccount, tvAddListing;
    ImageView imgUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        tvAccount = view.findViewById(R.id.tvAccount);
        tvAddListing = view.findViewById(R.id.tvAddListing);
        tvUser = view.findViewById(R.id.tvUser);
        imgUser = view.findViewById(R.id.imgUser);

        tvAccount.setOnClickListener(this);
        tvAddListing.setOnClickListener(this);

        setAccount();
    }

    public void setAccount() {
        String first_name = TEPreferences.readString(getActivity(), "first_name");
        String last_name = TEPreferences.readString(getActivity(), "last_name");
        String name = first_name + " " + last_name;
        String image = TEPreferences.readString(getActivity(), "image");

        tvUser.setText(name);
        Glide.with(getActivity())
                .load(image)
                .crossFade()
                .fitCenter()
                .centerCrop()
                .into(imgUser);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.tvAccount:
                fragment = new ProfileFragment();
                selectFragment(fragment);
                break;

            case R.id.tvAddListing:
                fragment = new AddListingFragment();
                selectFragment(fragment);
                break;
        }
    }

    public void selectFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
