package com.tribe.explorer.view.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.controller.AboutManager;
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.beans.AboutData;
import com.tribe.explorer.view.adapters.HowItWorkAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class HowItWorksFragment extends Fragment implements View.OnClickListener{

    private Dialog dialog;
    private List<AboutData.Data> dataList;
    String language;
    RecyclerView recyclerAbout;
    HowItWorkAdapter howItWorkAdapter;
    ImageView imgBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<>();
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        language = TEPreferences.readString(getActivity(), "lang");
        ModelManager.getInstance().getAboutManager().aboutTask(Config.ABOUT_URL + language);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_how_it_works, container, false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        imgBack = view.findViewById(R.id.imgBack);
        recyclerAbout = view.findViewById(R.id.recyclerAbout);
        recyclerAbout.setLayoutManager(new LinearLayoutManager(getActivity()));

        howItWorkAdapter = new HowItWorkAdapter(getActivity(), dataList);
        recyclerAbout.setAdapter(howItWorkAdapter);

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
            case Constants.ABOUT_SUCCESS:
                dataList.addAll(AboutManager.dataList);
                howItWorkAdapter.notifyDataSetChanged();
                break;

            case Constants.ABOUT_EMPTY:
                Toast.makeText(getActivity(), R.string.no_data, Toast.LENGTH_SHORT).show();
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
