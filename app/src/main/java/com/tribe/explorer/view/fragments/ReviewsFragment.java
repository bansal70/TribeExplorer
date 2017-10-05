package com.tribe.explorer.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.controller.ReviewsManager;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.Operations;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.ReviewsData;
import com.tribe.explorer.view.adapters.ReviewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment implements View.OnClickListener{

    View view;
    String listing_id, lang;
    RecyclerView recyclerReviews;
    ReviewAdapter reviewAdapter;
    private List<ReviewsData.Data> reviewsList;
    Dialog dialog;
    ImageView imgBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            listing_id = bundle.getString("listing_id");
        }
        lang = TEPreferences.readString(getActivity(), "lang");
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        ModelManager.getInstance().getReviewsManager().reviewsTask(Operations
                .getReviewsParams(listing_id, lang));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_reviews, container, false);
            initViews(view);
        }

        return view;
    }

    public void initViews(View view) {
        reviewsList = new ArrayList<>();
        imgBack = view.findViewById(R.id.imgBack);
        recyclerReviews = view.findViewById(R.id.recyclerReviews);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerReviews.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        reviewAdapter = new ReviewAdapter(getActivity(), reviewsList);
        recyclerReviews.setAdapter(reviewAdapter);

        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                backScreen();
                break;
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
        dialog.dismiss();
        EventBus.getDefault().removeAllStickyEvents();
        switch (event.getKey()) {
            case Constants.REVIEW_SUCCESS:
                reviewsList.addAll(ReviewsManager.reviewsList);
                reviewAdapter.notifyDataSetChanged();
                break;

            case Constants.REVIEW_ERROR:
                Toast.makeText(getActivity(), R.string.no_rating, Toast.LENGTH_SHORT).show();
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(getActivity(), R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
