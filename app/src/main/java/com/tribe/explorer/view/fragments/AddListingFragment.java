package com.tribe.explorer.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tribe.explorer.R;
import com.tribe.explorer.view.adapters.HoursAdapter;

public class AddListingFragment extends Fragment implements View.OnClickListener{

    EditText editEmail, editBusinessName, editLocation, editContact, editRegion, editCategory,
            editBusinessLabel, editWeb, editPhone, editVideoURL, editDescription;

    TextView tvCover, tvGallery, tvAddHours, tvAddLanguage, tvDone;
    RecyclerView recyclerHours, recyclerLanguage;
    HoursAdapter hoursAdapter;
    boolean isHours = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View view) {
        editEmail = view.findViewById(R.id.editEmail);
        editBusinessName = view.findViewById(R.id.editBusinessName);
        editLocation = view.findViewById(R.id.editLocation);
        editContact = view.findViewById(R.id.editContact);
        editRegion = view.findViewById(R.id.editRegion);
        editCategory = view.findViewById(R.id.editCategory);
        editBusinessLabel = view.findViewById(R.id.editBusinessLabel);
        editWeb = view.findViewById(R.id.editWeb);
        editPhone = view.findViewById(R.id.editPhone);
        editVideoURL = view.findViewById(R.id.editVideoURL);
        editDescription = view.findViewById(R.id.editDescription);

        tvCover = view.findViewById(R.id.tvCover);
        tvGallery = view.findViewById(R.id.tvGallery);
        tvAddHours = view.findViewById(R.id.tvAddHours);
        tvAddLanguage = view.findViewById(R.id.tvAddLanguage);
        tvDone = view.findViewById(R.id.tvDone);

        recyclerHours = view.findViewById(R.id.recyclerHours);
        recyclerLanguage = view.findViewById(R.id.recyclerLanguage);

        recyclerHours.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerHours.setNestedScrollingEnabled(false);
        recyclerLanguage.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLanguage.setNestedScrollingEnabled(false);

        hoursAdapter = new HoursAdapter(getActivity());
        recyclerHours.setAdapter(hoursAdapter);

        tvAddHours.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddHours:
                if (!isHours) {
                    recyclerHours.setVisibility(View.VISIBLE);
                    isHours = true;
                    tvAddHours.setText(R.string.minus);
                }
                else {
                    recyclerHours.setVisibility(View.GONE);
                    isHours = false;
                    tvAddHours.setText(R.string.plus);
                }
                break;
        }
    }
}