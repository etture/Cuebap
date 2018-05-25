package com.jinoolee.cuebap.RestaurantPage;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinoolee.cuebap.BaseActivity;
import com.jinoolee.cuebap.Helper.Utils;
import com.jinoolee.cuebap.R;

public class RestaurantActivity extends BaseActivity {

    Bundle fragmentBundle;
    private ImageView topRestaurantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Intent intent = getIntent();

        topRestaurantImage = findViewById(R.id.restaurant_page_top_image);

        //Cart Button setup
        //cartBtn = findViewById(R.id.restaurant_page_cart_btn);


        //ActionBar set text to center, with back button
        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);

        TextView actionBarTitle = actionBar.getCustomView().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(getString(intent.getIntExtra("restaurant_name", 0)));


        actionBar.setDisplayHomeAsUpEnabled(true);
        */
        //Code for displaying back button --> true: back button, false: no back button
        //ActionBar configuration DONE

        //Toolbar setup
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.restaurant_page_collapsing_toolbar);
        collapsingToolbar.setTitle(Utils.getLangString(this, curLang, intent.getIntExtra("restaurant_name", R.string.crazy_brown)));
        topRestaurantImage.setImageResource(intent.getIntExtra("restaurant_image", R.drawable.crazy_brown));
        //collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        Toolbar toolbar = findViewById(R.id.restaurant_page_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Window w = getWindow();
        //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initViewPagerAndTabs();

        fragmentBundle = new Bundle();
        fragmentBundle.putInt("buildingIndex", intent.getIntExtra("buildingIndex", 0));
        fragmentBundle.putInt("restaurantIndex", intent.getIntExtra("restaurantIndex", 0));
        fragmentBundle.putString("curLang", curLang);

        layoutElementStringSetup();
    }

    @Override
    protected void layoutElementStringSetup() {

    }

    private void initViewPagerAndTabs(){
        ViewPager viewPager = findViewById(R.id.restaurant_page_view_pager);
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.restaurant_page_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{
        private final int NUM_FRAGMENTS = 3;

        public MyPagerAdapter(android.support.v4.app.FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    RestaurantPageMenuFragment menuFragment = RestaurantPageMenuFragment.createInstance();
                    menuFragment.setArguments(fragmentBundle);
                    return menuFragment;
                case 1:
                    RestaurantPageSampleFragment sampleFragment = RestaurantPageSampleFragment.createInstance();
                    sampleFragment.setArguments(fragmentBundle);
                    return sampleFragment;
                case 2:
                    RestaurantPageSampleFragment sampleFragment2 = RestaurantPageSampleFragment.createInstance();
                    sampleFragment2.setArguments(fragmentBundle);
                    return sampleFragment2;
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return Utils.getLangString(getApplicationContext(), curLang, R.string.menu);
                case 1:
                    return Utils.getLangString(getApplicationContext(), curLang, R.string.review);
                case 2:
                    return Utils.getLangString(getApplicationContext(), curLang, R.string.information);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_FRAGMENTS;
        }
    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){

            onBackPressed();
            return true;

        }

        return false;
    }
}
