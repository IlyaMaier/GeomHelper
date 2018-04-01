package com.example.geomhelper.Resources;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.geomhelper.Fragments.FragmentTests;
import com.example.geomhelper.Person;
import com.example.geomhelper.R;
import com.example.geomhelper.Test;

import java.util.List;

public class RVTestsAdapter extends RecyclerView.Adapter<RVTestsAdapter.TestViewHolder> {

    private Context context;
    private List<Test> tests;

    public void setData(List<Test> t) {
        tests = t;
    }

    public RVTestsAdapter(Context context, List<Test> tests) {
        this.context = context;
        this.tests = tests;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_tests_content, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {

        final Test test = tests.get(position);
        holder.test = test;

        holder.cardView.setBackgroundResource(test.getBackground());
        holder.name.setText(test.getTestName());

    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CardView cardView;
        Test test;

        public void setTest(Test test) {
            this.test = test;
        }

        public TestViewHolder(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.text_rv_tests);
            cardView = itemView.findViewById(R.id.card_tests);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Person.currentTest = test;
                    FragmentTests.click();
                }
            });
        }

    }

}
