package com.tribe.explorer.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.HomeManager;
import com.tribe.explorer.model.beans.CategoriesData;
import com.tribe.explorer.view.adapters.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    List<CategoriesData.Data> categoriesList;
    RecyclerView recyclerCategories;
    HomeAdapter homeAdapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        initViews(view);

        return view;
    }

    public void initViews(View v) {
        categoriesList = new ArrayList<>();
        recyclerCategories = v.findViewById(R.id.recyclerCategories);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesList.addAll(HomeManager.categoriesList);

        homeAdapter = new HomeAdapter(getActivity(), categoriesList);
        recyclerCategories.setAdapter(homeAdapter);
    }
}
