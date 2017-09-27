package com.tribe.explorer.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tribe.explorer.R;
import com.tribe.explorer.model.TEPreferences;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{

    ProgressBar progressBar;
    List<String> languageList;
    Spinner spinner;
    Handler handler;
    Runnable runnable;
    LinearLayout langLayout;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
    }

    public void initViews() {
        user_id = TEPreferences.readString(this, "user_id");
        FirebaseInstanceId.getInstance().getToken();
        languageList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        langLayout = (LinearLayout) findViewById(R.id.langLayout);
        spinner = (Spinner) findViewById(R.id.spinner);

        languageList.add("Select language");
        languageList.add("English");
        languageList.add("Spanish");
        languageList.add("Chinese");
        languageList.add("French");
        languageList.add("Tagalog");
        languageList.add("Hindustani");
        languageList.add("Arabic");
        languageList.add("Korean");
        languageList.add("Russian");
        languageList.add("Portuguese");
        languageList.add("Italian");

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, languageList);

        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(listAdapter);
        spinner.setOnItemSelectedListener(this);
        handleSleep();
    }

    public void handleSleep() {
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                langLayout.setVisibility(View.VISIBLE);
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        // String item = parent.getItemAtPosition(position).toString();
        if (position == 0)
            return;

        TEPreferences.putString(this, "lang", "en");

        if (user_id.isEmpty())
            startActivity(new Intent(this, LoginActivity.class));
        else
            startActivity(new Intent(this, MainActivity.class));

        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        if (handler != null)
            handler.removeCallbacks(runnable);
        finish();
    }
}
