package com.example.bot.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bot.R;
import com.example.bot.abstracts.GPSActivity;
import com.example.bot.ui.fragments.MainFragment;

import java.util.ArrayList;

import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;

public class MainActivity extends GPSActivity {
    private ArrayList<Fragment> mFragments;

    private final String[] mTitles = {"데이트", "맛집", "문화시설", "축제"};
    private final String[] mContentTypeId = {"12", "39", "14", "15"};
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
        initViewPager();
        int[] imageArray = new int[]{
                R.drawable.date,
                R.drawable.food,
                R.drawable.night_view,
                R.drawable.festival};

        int[] colorArray = new int[]{
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light};

        CoordinatorTabLayout coordinatorTabLayout = (CoordinatorTabLayout) findViewById(R.id.coordinator_tab_layout);
        coordinatorTabLayout.setTranslucentStatusBar(this)
                .setTitle("B.O.T")
                .setBackEnable(true)
                .setImageArray(imageArray, colorArray)
                .setupWithViewPager(mViewPager);

        coordinatorTabLayout.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CourseSelectActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        for (String contentId : mContentTypeId) {
            mFragments.add(MainFragment.getInstance(contentId));
        }
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}