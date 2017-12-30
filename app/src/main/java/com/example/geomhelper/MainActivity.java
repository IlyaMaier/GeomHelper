package com.example.geomhelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.geomhelper.Fragments.FragmentCourses;
import com.example.geomhelper.Fragments.FragmentLeaderboard;
import com.example.geomhelper.Fragments.FragmentProfile;
import com.example.geomhelper.Fragments.FragmentSettings;
import com.example.geomhelper.Fragments.FragmentTests;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tests:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_courses:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_leaderboard:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_settings:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager.setCurrentItem(2);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTitle(getString(R.string.tests));
                        bottomNavigationView.setSelectedItemId(R.id.navigation_tests);
                        break;
                    case 1:
                        setTitle(getString(R.string.courses));
                        bottomNavigationView.setSelectedItemId(R.id.navigation_courses);
                        break;
                    case 2:
                        setTitle(getString(R.string.profile));
                        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                        break;
                    case 3:
                        setTitle(getString(R.string.leaderboard));
                        bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);
                        break;
                    case 4:
                        setTitle(getString(R.string.settings));
                        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
                        break;
                }
            }

            public void onPageScrollStateChanged(int state) { }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 2) {
            super.onBackPressed();
        } else if (viewPager.getCurrentItem() >= 3) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentTests();
                case 1:
                    return new FragmentCourses();
                case 2:
                    return new FragmentProfile();
                case 3:
                    return new FragmentLeaderboard();
                case 4:
                    return new FragmentSettings();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
