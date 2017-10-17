package com.tribe.explorer.view;

import android.app.Dialog;
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
import com.tribe.explorer.controller.ModelManager;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    ProgressBar progressBar, progressBar2;
    List<String> languageList;
    Spinner spinner;
    Handler handler;
    Runnable runnable;
    LinearLayout langLayout;
    String user_id;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
    }

    public void initViews() {
        dialog = Utils.showDialog(this);
        user_id = TEPreferences.readString(this, "user_id");
        FirebaseInstanceId.getInstance().getToken();
        languageList = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        langLayout = (LinearLayout) findViewById(R.id.langLayout);
        spinner = (Spinner) findViewById(R.id.spinner);

        languageList.add("Select language");
        languageList.add("English");
        languageList.add("Spanish");
        languageList.add("Chinese");
        languageList.add("French");
        languageList.add("Tagalog");
        languageList.add("Hindustani");
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

    public void setLanguages(int position) {
        switch (position) {
            case 1:
                Utils.setLanguage(this, "en");
                TEPreferences.putString(this, "lang", "en");
                break;
            case 2:
                Utils.setLanguage(this, "es");
                TEPreferences.putString(this, "lang", "es");
                break;
            case 3:
                Utils.setLanguage(this, "zh");
                TEPreferences.putString(this, "lang", "zh");
                break;
            case 4:
                Utils.setLanguage(this, "fr");
                TEPreferences.putString(this, "lang", "fr");
                break;
            case 5:
                Utils.setLanguage(this, "tl");
                TEPreferences.putString(this, "lang", "tl");
                break;
            case 6:
                Utils.setLanguage(this, "hi");
                TEPreferences.putString(this, "lang", "hi");
                break;
            case 7:
                Utils.setLanguage(this, "ko");
                TEPreferences.putString(this, "lang", "ko");
                break;
            case 8:
                Utils.setLanguage(this, "ru");
                TEPreferences.putString(this, "lang", "ru");
                break;
            case 9:
                Utils.setLanguage(this, "pt");
                TEPreferences.putString(this, "lang", "pt");
                break;
            case 10:
                Utils.setLanguage(this, "it");
                TEPreferences.putString(this, "lang", "it");
                break;
        }
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

        setLanguages(position);

        progressBar2.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ModelManager.getInstance().getLanguageManager()
                        .languageTask(SplashActivity.this, Config.LANGUAGE_URL);
                if (user_id.isEmpty())
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);

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
