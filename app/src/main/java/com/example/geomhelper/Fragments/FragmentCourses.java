package com.example.geomhelper.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.Activities.AddCourseActivity;
import com.example.geomhelper.Content.Course;
import com.example.geomhelper.Content.Courses;
import com.example.geomhelper.MainActivity;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.User;
import com.example.geomhelper.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FragmentCourses extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager verticalManager;
    RVAdapter adapterCourses;
    FloatingActionButton floatingActionButton;
    View rootView;
    FragmentManager fragmentManager;
    int scrollDist = 0;
    boolean isVisible = true, j = true;
    float MINIMUM = 25;
    FrameLayout frameLayout;
    CardView card;
    float y, y0;
    long millis;
    public static boolean h = false;
    int height;
    Async async;
    ArrayList<String> themesAll, themesP;
    EditText et;
    ImageView imageView;
    ArrayList<Short> shortsAll, shortsP;
    BottomNavigationView bottomNavigationView;
    List<Course> courses;

    public FragmentCourses() {
    }

    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        courses = new Courses().getCurrentCourses();

        frameLayout = rootView.findViewById(R.id.fragment);

        card =
                (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_search, null);
        int width = 1000;
        height = 175;
        WindowManager wm = (WindowManager) Objects.requireNonNull(
                getContext()).getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            width = wm.getDefaultDisplay().getWidth() - 50;
            height = (int) (wm.getDefaultDisplay().getHeight() * 0.1);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        frameLayout.addView(card, layoutParams);
        card.setTranslationY(-height);

        et = rootView.findViewById(R.id.search);

        recyclerView = rootView.findViewById(R.id.rv_courses);

        fragmentManager = getFragmentManager();

        verticalManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(verticalManager);

        adapterCourses = new RVAdapter(getContext(), Person.courses);
        recyclerView.setAdapter(adapterCourses);

        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_grow);

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.startAnimation(animation);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddCourseActivity.class);
                startActivityForResult(i, 10);
            }
        });

        final int finalHeight = height;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (System.currentTimeMillis() - millis > 500) {
                    y = event.getY();
                    millis = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getY() - y > 90 && y0 == 0) {
                        recyclerView.animate().translationY(finalHeight + 25).start();
                        card.animate().translationY(25).alpha(1).start();
                        h = true;
                        async = new Async();
                        async.execute();
                        if (j) {
                            themesAll = new ArrayList<>();
                            themesP = new ArrayList<>();
                            shortsAll = new ArrayList<>();
                            shortsP = new ArrayList<>();

                            for (int i = 0; i < courses.size(); i++)
                                for (int j = 0; j < courses.get(i).getNumberOfThemes(); j++) {
                                    themesAll.add(courses.get(i).getTheme(j).toLowerCase());
                                    shortsAll.add((short) i);
                                }

                            for (int i = 0; i < Person.courses.size(); i++)
                                for (int j = 0; j < Person.courses.get(i).getNumberOfThemes(); j++) {
                                    themesP.add(Person.courses.get(i).getTheme(j).toLowerCase());
                                    shortsP.add((short) i);
                                }
                            j = false;
                        }
                    } else if (h && y - event.getY() > 5) {
                        card.animate().translationY(-height).alpha(0).start();
                        recyclerView.animate().translationY(0).start();
                        h = false;
                        if (async != null)
                            async.cancel(true);
                    }
                }
                return false;
            }

        });

        imageView = rootView.findViewById(R.id.image_search);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String theme = et.getText().toString().toLowerCase();
                if (!themesAll.contains(theme))
                    Toast.makeText(getContext(),
                            "Такой темы не существует.",
                            Toast.LENGTH_SHORT).show();
                else if (themesP.contains(theme)) {
                    recyclerView.smoothScrollToPosition(
                            adapterCourses.items.indexOf(
                                    Person.courses.get(
                                            shortsP.get(
                                                    themesP.indexOf(theme)))));
                    InputMethodManager imm = (InputMethodManager)
                            getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Toast.makeText(getContext(),
                            "Откройте курс  \"" +
                                    Person.courses.get(
                                            shortsP.get(
                                                    themesP.indexOf(theme)))
                                            .getCourseName() + "\".",
                            Toast.LENGTH_SHORT).show();
                    card.animate().translationY(-height).alpha(0).start();
                    if (async != null)
                        async.cancel(true);
                    async = null;
                    h = false;
                    Person.currentCourse = Person.courses.get(
                            shortsP.get(
                                    themesP.indexOf(theme)));
                    for (int i = 0; i < Person.currentCourse.getNumberOfThemes(); i++) {
                        if (Person.currentCourse.getTheme(i).toLowerCase().equals(theme))
                            Person.currentTheme = i;
                    }
                    MainActivity.back = 2;
                    Person.backCourses = 2;
                    floatingActionButton.hide();
                    recyclerView.setVisibility(View.INVISIBLE);
                    recyclerView.setClickable(false);
                    FragmentTransaction fragmentTransaction = Objects.
                            requireNonNull(getFragmentManager()).beginTransaction();
                    FragmentCourseText fragmentCourseText = new FragmentCourseText();
                    fragmentTransaction.replace(R.id.fragment, fragmentCourseText);
                    fragmentTransaction.commit();
                } else if (themesAll.contains(theme)) {
                    Intent i = new Intent(getContext(), AddCourseActivity.class);
                    startActivityForResult(i, 10);
                    Toast.makeText(getContext(),
                            "Добавьте курс  \"" +
                                    courses.get(
                                            shortsAll.get(
                                                    themesAll.indexOf(theme)))
                                            .getCourseName() + "\".",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                y0 += dy;

                if (isVisible && scrollDist > MINIMUM) {
                    hide();
                    scrollDist = 0;
                    isVisible = false;
                } else if (!isVisible && scrollDist < -MINIMUM) {
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
            }

            void hide() {
                floatingActionButton.animate().translationY(
                        floatingActionButton.getHeight() + 16).
                        setInterpolator(new AccelerateInterpolator(2)).start();
            }

        });

        bottomNavigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                recyclerView.smoothScrollToPosition(0);
                y0 = 0;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            adapterCourses.setItems(Person.courses);
            adapterCourses.notifyDataSetChanged();
            if (!j) {
                themesAll = new ArrayList<>();
                themesP = new ArrayList<>();
                shortsAll = new ArrayList<>();
                shortsP = new ArrayList<>();

                for (int i = 0; i < courses.size(); i++)
                    for (int j = 0; j < courses.get(i).getNumberOfThemes(); j++) {
                        themesAll.add(courses.get(i).getTheme(j).toLowerCase());
                        shortsAll.add((short) i);
                    }

                for (int i = 0; i < Person.courses.size(); i++)
                    for (int j = 0; j < Person.courses.get(i).getNumberOfThemes(); j++) {
                        themesP.add(Person.courses.get(i).getTheme(j).toLowerCase());
                        shortsP.add((short) i);
                    }
            }
        }

    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RecyclerViewCoursesHolder> {

        ArrayList<Course> items;
        Context context;

        RVAdapter(Context context, ArrayList<Course> items) {
            this.items = items;
            this.context = context;
        }

        @NonNull
        @Override
        public RVAdapter.RecyclerViewCoursesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses_card, parent, false);
            return new RecyclerViewCoursesHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RVAdapter.RecyclerViewCoursesHolder holder, int position) {
            holder.bind(Person.courses.get(position), position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        void setItems(ArrayList<Course> items) {
            this.items = items;
        }

        class RecyclerViewCoursesHolder extends RecyclerView.ViewHolder {

            private CardView cardView;
            private TextView title;
            private ImageView image;
            private Course course;
            private int position;

            RecyclerViewCoursesHolder(final View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                image = itemView.findViewById(R.id.image_content_courses);
                cardView = itemView.findViewById(R.id.card_content_courses);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        card.animate().translationY(-height).alpha(0).start();
                        if (async != null)
                            async.cancel(true);
                        async = null;
                        h = false;
                        Person.currentCourse = course;
                        MainActivity.back = 1;
                        Person.backCourses = 1;
                        floatingActionButton.hide();
                        recyclerView.setVisibility(View.INVISIBLE);
                        recyclerView.setClickable(false);
                        recyclerView = null;
                        floatingActionButton = null;
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        FragmentThemes fragmentThemes = new FragmentThemes();
                        fragmentTransaction.replace(R.id.fragment, fragmentThemes);
                        fragmentTransaction.commit();
                    }
                });

                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder ad;
                        ad = new AlertDialog.Builder(context);
                        ad.setTitle("Удаление курса");
                        ad.setMessage("Вы точно хотите удалить данный курс?");
                        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                Person.courses.remove(course);
                                adapterCourses.setItems(Person.courses);
                                adapterCourses.notifyDataSetChanged();
                                floatingActionButton.show();
                                Person.c = Person.c.replace(
                                        Person.c.charAt(Person.c.length() - position-1)
                                                + "", "");
                                System.out.println(courses.indexOf(course));
                                if (!j) {
                                    themesAll = new ArrayList<>();
                                    themesP = new ArrayList<>();
                                    shortsAll = new ArrayList<>();
                                    shortsP = new ArrayList<>();

                                    for (int i = 0; i < courses.size(); i++)
                                        for (int j = 0; j < courses.get(i).getNumberOfThemes(); j++) {
                                            themesAll.add(courses.get(i).getTheme(j).toLowerCase());
                                            shortsAll.add((short) i);
                                        }

                                    for (int i = 0; i < Person.courses.size(); i++)
                                        for (int j = 0; j < Person.courses.get(i).getNumberOfThemes(); j++) {
                                            themesP.add(Person.courses.get(i).getTheme(j).toLowerCase());
                                            shortsP.add((short) i);
                                        }
                                }
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(User.URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .build();
                                UserService userService = retrofit.create(UserService.class);
                                userService.updateUser(Person.id, "courses", Person.c)
                                        .enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                                if (Objects.requireNonNull(response.body()).equals("0"))
                                                    Toast.makeText(context, "Не удалось отправить данные на сервер",
                                                            Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                                Toast.makeText(context, "Не удалось отправить данные на сервер",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        });
                        ad.setCancelable(true);
                        ad.show();
                        return false;
                    }
                });
            }

            void bind(Course course, int position) {
                this.course = course;
                this.position = position;
                image.setImageBitmap(BitmapFactory.decodeResource(itemView.getResources(), course.getBackground()));
                title.setText(course.getCourseName());
            }

        }

    }

    class Async extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            while (h) {

            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (card != null)
                card.animate().translationY(-height).alpha(0).start();
            if (recyclerView != null)
                recyclerView.animate().translationY(0).start();
            h = false;
            cancel(true);
        }
    }

}