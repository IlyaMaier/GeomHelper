package com.example.geomhelper.Resources;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geomhelper.Course;
import com.example.geomhelper.Courses;
import com.example.geomhelper.Fragments.FragmentCourses;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;

import java.util.ArrayList;

public class RVCoursesAdapter extends RecyclerView.Adapter<RVCoursesAdapter.RecyclerViewCoursesHolder> {

    ArrayList<Course> items = new ArrayList<>();
    Context context;

    public RVCoursesAdapter(Context context, ArrayList<Course> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerViewCoursesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses_card, parent, false);
        return new RecyclerViewCoursesHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewCoursesHolder holder, int position) {
        holder.bind(Person.courses.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Course> items) {
        this.items = items;
    }

    class RecyclerViewCoursesHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView title;
        private ImageView image;
        private Course course;

        public RecyclerViewCoursesHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image_content_courses);
            cardView = itemView.findViewById(R.id.card_content_courses);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person.currentCourse = course;
                    FragmentCourses.click();
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
                            FragmentCourses.delete(Courses.currentCourses.indexOf(course)+"");
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

        public void bind(Course course) {
            this.course = course;
            image.setImageBitmap(BitmapFactory.decodeResource(itemView.getResources(), course.getBackground()));
            title.setText(course.getCourseName());
        }
    }
}
