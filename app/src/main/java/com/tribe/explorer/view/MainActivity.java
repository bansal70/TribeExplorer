package com.tribe.explorer.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.tribe.explorer.R;
import com.tribe.explorer.model.TEPreferences;
import com.tribe.explorer.model.Utils;
import com.tribe.explorer.model.custom.BottomNavigationViewHelper;
import com.tribe.explorer.view.fragments.CouponFragment;
import com.tribe.explorer.view.fragments.FavouritesFragment;
import com.tribe.explorer.view.fragments.HomeFragment;
import com.tribe.explorer.view.fragments.MeFragment;
import com.tribe.explorer.view.fragments.SearchFragment;

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

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (!(fragment instanceof HomeFragment))
            Utils.goToFragment(this, new HomeFragment(), R.id.frame_layout, false);
       /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment());
        transaction.commit();*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Fragment selectedFragment = null;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        switch (item.getItemId()) {
            case R.id.menu_home:
                if (!(fragment instanceof HomeFragment))
                    Utils.goToFragment(this, new HomeFragment(), R.id.frame_layout, false);
                //selectedFragment = new HomeFragment();
                break;

            case R.id.menu_favourites:
                // selectedFragment = new FavouritesFragment();
                if (!(fragment instanceof FavouritesFragment))
                    Utils.goToFragment(this, new FavouritesFragment(), R.id.frame_layout, false);
                break;

            case R.id.menu_search:
                // selectedFragment = new SearchFragment();
                if (!(fragment instanceof SearchFragment))
                    Utils.goToFragment(this, new SearchFragment(), R.id.frame_layout, false);
                break;

            case R.id.menu_coupon:
                // selectedFragment = new CouponFragment();
                if (!(fragment instanceof CouponFragment))
                    Utils.goToFragment(this, new CouponFragment(), R.id.frame_layout, false);
                break;

            case R.id.menu_me:
                // selectedFragment = new MeFragment();
                if (!(fragment instanceof MeFragment))
                    Utils.goToFragment(this, new MeFragment(), R.id.frame_layout, false);
                break;
        }
        /*if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectedFragment);
        transaction.commit();*/
        return true;
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
