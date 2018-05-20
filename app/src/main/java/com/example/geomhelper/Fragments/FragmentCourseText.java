package com.example.geomhelper.Fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;

import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

import java.util.Objects;

import static com.example.geomhelper.Person.pref;

public class FragmentCourseText extends Fragment {

    public FragmentCourseText() {
    }

    private FloatingActionButton floatingActionButton, floatingActionButton2;
    private WebView webView;
    private int scrollDist = 0;
    private boolean isVisible = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_text, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.white));

        webView = view.findViewById(R.id.web_fragment_course_text);
        if (pref.getString("pref_day_night", "").equals("Включен")) {
            webView.loadUrl(Person.currentCourse.getCourseTextUrlNight(Person.currentTheme));
            webView.setBackgroundColor(getResources().getColor(R.color.background_color));
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        } else webView.loadUrl(Person.currentCourse.getCourseTextUrl(Person.currentTheme));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    int dy = scrollY - oldScrollY;
                    if (isVisible && scrollDist > 25) {
                        hide();
                        scrollDist = 0;
                        isVisible = false;
                    } else if (!isVisible && scrollDist < -25) {
                        show();
                        scrollDist = 0;
                        isVisible = true;
                    }

                    if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                        scrollDist += dy;
                    }
                }

                void show() {
                    floatingActionButton.animate().translationY(0)
                            .setInterpolator(new DecelerateInterpolator(2)).start();
                    floatingActionButton2.animate().translationY(0)
                            .setInterpolator(new DecelerateInterpolator(2)).start();
                }

                void hide() {
                    floatingActionButton.animate().translationY(
                            floatingActionButton.getHeight() + 16).
                            setInterpolator(new AccelerateInterpolator(2)).start();
                    floatingActionButton2.animate().translationY(
                            floatingActionButton.getHeight() + 16).
                            setInterpolator(new AccelerateInterpolator(2)).start();
                }
            });
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_grow);

        floatingActionButton2 = view.findViewById(R.id.floatingActionButton3);
        floatingActionButton2.startAnimation(animation);
        floatingActionButton2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (Person.currentTheme == 0) {
                    FragmentTransaction fragmentTransaction = Objects.
                            requireNonNull(getFragmentManager()).beginTransaction();
                    FragmentThemes fragmentThemes = new FragmentThemes();
                    fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                    fragmentTransaction.commit();
                    MainActivity.back = 1;
                    Person.backCourses = 1;
                } else {
                    floatingActionButton.setImageResource(R.drawable.ic_next);
                    Person.currentTheme--;
                    if (pref.getString("pref_day_night", "").equals("Включен")) {
                        webView.loadUrl(Person.currentCourse.getCourseTextUrlNight(Person.currentTheme));
                        webView.setBackgroundColor(getResources().getColor(R.color.background_color));
                        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                    } else webView.loadUrl(Person.currentCourse.getCourseTextUrl(Person.currentTheme));
                }
            }
        });

        floatingActionButton = view.findViewById(R.id.floatingActionButton2);
        if (Person.currentCourse.getThemesSize() - 1 == Person.currentTheme)
            floatingActionButton.setImageResource(R.drawable.ic_completed);
        floatingActionButton.startAnimation(animation);
        floatingActionButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (Person.currentCourse.getThemesSize() - 1 == Person.currentTheme) {
                    FragmentTransaction fragmentTransaction = Objects.
                            requireNonNull(getFragmentManager()).beginTransaction();
                    FragmentThemes fragmentThemes = new FragmentThemes();
                    fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                    fragmentTransaction.commit();
                    MainActivity.back = 1;
                    Person.backCourses = 1;
                } else {
                    Person.currentTheme++;
                    if (pref.getString("pref_day_night", "").equals("Включен")) {
                        webView.loadUrl(Person.currentCourse.getCourseTextUrlNight(Person.currentTheme));
                        webView.setBackgroundColor(getResources().getColor(R.color.background_color));
                        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                    } else webView.loadUrl(Person.currentCourse.getCourseTextUrl(Person.currentTheme));
                    if (Person.currentCourse.getThemesSize() - 1 == Person.currentTheme)
                        floatingActionButton.setImageResource(R.drawable.ic_completed);
                }
            }
        });

        return view;
    }

}
