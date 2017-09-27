package com.tribe.explorer.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tribe.explorer.R;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.view.LoginActivity;

public class MeFragment extends Fragment implements View.OnClickListener{

    TextView tvUser, tvAccount, tvAddListing, tvMyListing, tvBlog, tvAbout, tvLogout;
    ImageView imgUser;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id = TEPreferences.readString(getActivity(), "user_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        tvUser = view.findViewById(R.id.tvUser);
        imgUser = view.findViewById(R.id.imgUser);

        tvAccount = view.findViewById(R.id.tvAccount);
        tvAddListing = view.findViewById(R.id.tvAddListing);
        tvMyListing = view.findViewById(R.id.tvMyListing);
        tvBlog = view.findViewById(R.id.tvBlog);
        tvAbout = view.findViewById(R.id.tvAbout);
        tvLogout = view.findViewById(R.id.tvLogout);

        tvAccount.setOnClickListener(this);
        tvAddListing.setOnClickListener(this);
        tvMyListing.setOnClickListener(this);
        tvBlog.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        if (user_id.isEmpty())
            tvLogout.setText(R.string.login);

        setAccount();
    }

    public void setAccount() {
        String first_name = TEPreferences.readString(getActivity(), "first_name");
        String last_name = TEPreferences.readString(getActivity(), "last_name");
        String name = first_name + " " + last_name;
        String image = TEPreferences.readString(getActivity(), "image");

        if (user_id.isEmpty())
            tvUser.setText(R.string.guest);
        else
            tvUser.setText(name);

        Glide.with(getActivity())
                .load(image)
                .crossFade()
                .fitCenter()
                .placeholder(R.mipmap.ic_user)
                .error(R.mipmap.ic_user)
                .centerCrop()
                .into(imgUser);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.tvAccount:
                if (user_id.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.login_to_create_account, Toast.LENGTH_SHORT).show();
                    return;
                }
                fragment = new ProfileFragment();
                selectFragment(fragment);
                break;

            case R.id.tvAddListing:
                if (user_id.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.login_to_add_business, Toast.LENGTH_SHORT).show();
                    return;
                }
                fragment = new AddListingFragment();
                selectFragment(fragment);
                break;

            case R.id.tvMyListing:
                if (user_id.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.login_to_check_listings, Toast.LENGTH_SHORT).show();
                    return;
                }
                fragment = new MyListingFragment();
                selectFragment(fragment);
                break;

            case R.id.tvBlog:
                fragment = new BlogFragment();
                selectFragment(fragment);
                break;

            case R.id.tvAbout:
                fragment = new HowItWorksFragment();
                selectFragment(fragment);
                break;

            case R.id.tvLogout:
                if (!user_id.isEmpty())
                    Utils.logoutAlert(getActivity());
                else
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
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
