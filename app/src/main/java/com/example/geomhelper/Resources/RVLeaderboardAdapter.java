package com.example.geomhelper.Resources;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geomhelper.R;

import java.util.List;

public class RVLeaderboardAdapter extends RecyclerView.Adapter<RVLeaderboardAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;

    public RVLeaderboardAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_leaderboard_content, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = users.get(position);

        if (position == 0) holder.linearLayout.setBackgroundColor(Color.parseColor("#FFD700"));
        else if (position == 1) holder.linearLayout.setBackgroundColor(Color.parseColor("#c6ccd2"));
        else if (position == 2) holder.linearLayout.setBackgroundColor(Color.parseColor("#f6a120"));

        if(position == 9) holder.place.setTextSize(17);
        holder.place.setText(position + 1 + "");
        holder.name.setText(user.getName());
        holder.experience.setText(user.getExperience());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView name, experience, place;
        ImageView image;
        CardView cardView;
        LinearLayout linearLayout;

        public UserViewHolder(final View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.line_leaderboard);
            name = itemView.findViewById(R.id.text_name_leaderboard);
            experience = itemView.findViewById(R.id.text_expierence_leaderboard);
            place = itemView.findViewById(R.id.text_place_leaderboard);
            image = itemView.findViewById(R.id.image_profile_leaderboard);
            cardView = itemView.findViewById(R.id.cardView_leaderboard);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Ну и че мы выклабучиваемся?!?", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
