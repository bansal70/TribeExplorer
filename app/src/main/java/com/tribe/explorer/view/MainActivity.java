package com.tribe.explorer.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Constants;
import com.tribe.explorer.model.Event;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.custom.BottomNavigationViewHelper;
import com.tribe.explorer.view.fragments.CouponFragment;
import com.tribe.explorer.view.fragments.FavouritesFragment;
import com.tribe.explorer.view.fragments.HomeFragment;
import com.tribe.explorer.view.fragments.MeFragment;
import com.tribe.explorer.view.fragments.SearchFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    String user_id;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void initViews() {
        user_id = TEPreferences.readString(this, "user_id");
        String lang = TEPreferences.readString(this, "lang");
        dialog = Utils.showDialog(this);
        dialog.show();
       // ModelManager.getInstance().getHomeManager().categoriesTask(Operations.getCategoriesParams(lang, 1));

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment());
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.menu_home:
                selectedFragment = new HomeFragment();
                break;

            case R.id.menu_favourites:
                selectedFragment = new FavouritesFragment();
                break;

            case R.id.menu_search:
                selectedFragment = new SearchFragment();
                break;

            case R.id.menu_coupon:
                selectedFragment = new CouponFragment();
                break;

            case R.id.menu_me:
                selectedFragment = new MeFragment();
                break;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();
        return true;
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
        EventBus.getDefault().removeAllStickyEvents();
        dialog.dismiss();
        switch (event.getKey()) {
            case Constants.CATEGORIES_SUCCESS:
                /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, new HomeFragment());
                transaction.commit();*/
                break;

            case Constants.CATEGORIES_ERROR:
                break;

            case Constants.NO_RESPONSE:
                Toast.makeText(this, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

}
