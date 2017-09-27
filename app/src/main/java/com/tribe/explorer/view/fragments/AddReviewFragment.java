package com.tribe.explorer.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class AddReviewFragment extends Fragment implements View.OnClickListener{

    private Dialog dialog;
    RatingBar rbSpeed, rbQuality, rbPrice;
    EditText editComment;
    String user_id, lang, listing_id;
    Button btSubmitReview;

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
        editComment = view.findViewById(R.id.editComment);
        btSubmitReview = view.findViewById(R.id.btSubmitReview);
        btSubmitReview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btSubmitReview:
                float speed = rbSpeed.getRating();
                float quality = rbQuality.getRating();
                float price = rbPrice.getRating();
                String comment = editComment.getText().toString();
                if (speed == 0 || quality == 0 || price == 0) {
                    Toast.makeText(getActivity(), R.string.rating_can_not_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (comment.trim().isEmpty())
                    Toast.makeText(getActivity(), R.string.add_comment, Toast.LENGTH_SHORT).show();
                dialog.show();
                ModelManager.getInstance().getAddReviewManager().addReviewTask(Operations
                        .getAddReviewsParams(user_id, listing_id, comment, speed, quality, price, lang));
                break;

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
            case Constants.ADD_REVIEW_SUCCESS:
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

    public void backScreen() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
