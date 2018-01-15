package com.example.geomhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.geomhelper.Activities.LoginActivity;
import com.example.geomhelper.Fragments.FragmentCourses;
import com.example.geomhelper.Fragments.FragmentLeaderboard;
import com.example.geomhelper.Fragments.FragmentProfile;
import com.example.geomhelper.Fragments.FragmentSettings;
import com.example.geomhelper.Fragments.FragmentTests;
import com.example.geomhelper.Fragments.FragmentThemes;
import com.example.geomhelper.Resources.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    public static int back = 0;
    SharedPreferences mSettings;
    final int NUM_PAGES = 5;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    SharedPreferences.Editor editor;

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

        if (!Courses.currentCourses.contains(Courses.basics))
            Courses.currentCourses.add(0, Courses.basics);
        if (!Courses.currentCourses.contains(Courses.second))
            Courses.currentCourses.add(1, Courses.second);

        mSettings = getSharedPreferences(Person.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.getBoolean(Person.APP_PREFERENCES_WELCOME, false)) {
            Person.name = mSettings.getString(Person.APP_PREFERENCES_NAME, "Произошла ошибка");
            Person.currentLevel = mSettings.getString(Person.APP_PREFERENCES_LEVEL, "Произошла ошибка");
            Person.experience = mSettings.getInt(Person.APP_PREFERENCES_EXPERIENCE, -1);
            Person.currentLevelExperience = mSettings.getInt(Person.APP_PREFERENCES_LEVEL_EXPERIENCE, -1);
            Person.leaderBoardPlace = mSettings.getLong(Person.APP_PREFERENCES_LEADERBOARDPLACE, -1);
            for (int i = 0; i < mSettings.getInt(Person.APP_PREFERENCES_COURSES_SIZE, 0); i++) {
                if (Person.courses.size() != mSettings.getInt(Person.APP_PREFERENCES_COURSES_SIZE,0)) {
                    String course = Person.APP_PREFERENCES_COURSES + String.valueOf(i);
                    for (int j = 0; j < Courses.currentCourses.size(); j++) {
                        if (mSettings.getString(course, "").equals(Courses.currentCourses.get(j).getCourseName()))
                            Person.courses.add(i, Courses.currentCourses.get(j));
                    }
                }
            }
        } else {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(i, 1);
        }

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager.setCurrentItem(2);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setChecked(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

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

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (back == 0) {
            if (viewPager.getCurrentItem() == 2) {
                super.onBackPressed();
            } else if (viewPager.getCurrentItem() >= 3) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        } else if (back == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentCourses()).commit();
            getSupportFragmentManager().beginTransaction().remove(new FragmentCourses()).commit();
            back = 0;
        } else if (back == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentThemes()).commit();
            getSupportFragmentManager().beginTransaction().remove(new FragmentThemes()).commit();
            back = 1;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
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

    @Override
    protected void onStop() {
        super.onStop();
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_courses ||
                bottomNavigationView.getSelectedItemId() == R.id.navigation_profile ||
                bottomNavigationView.getSelectedItemId() == R.id.navigation_tests)
            editor = mSettings.edit();
        editor.putBoolean(Person.APP_PREFERENCES_WELCOME, true);
        editor.putString(Person.APP_PREFERENCES_NAME, Person.name);
        editor.putString(Person.APP_PREFERENCES_LEVEL, Person.currentLevel);
        editor.putInt(Person.APP_PREFERENCES_EXPERIENCE, Person.experience);
        editor.putInt(Person.APP_PREFERENCES_LEVEL_EXPERIENCE, Person.currentLevelExperience);
        editor.putLong(Person.APP_PREFERENCES_LEADERBOARDPLACE, Person.leaderBoardPlace);
        editor.putInt(Person.APP_PREFERENCES_COURSES_SIZE, Person.courses.size());
        for (int i = 0; i < Person.courses.size(); i++) {
            String course = Person.APP_PREFERENCES_COURSES + String.valueOf(i);
            editor.putString(course, Person.courses.get(i).getCourseName());
        }
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) onRestart();
    }

}
