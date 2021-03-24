package com.gachugusville.development.serviced.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Common.User;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserRecyclerViewPerCategoryAdapter extends RecyclerView.Adapter<UserRecyclerViewPerCategoryAdapter.ViewHolderUsersHorizontal> {
    List<User> usersPerCategoryList;
    Context context;

    public UserRecyclerViewPerCategoryAdapter(List<User> usersPerCategoryList, Context context) {
        this.usersPerCategoryList = usersPerCategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderUsersHorizontal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_in_category_layout, parent, false);
        return new ViewHolderUsersHorizontal(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsersHorizontal holder, int position) {
        holder.user_in_card_layout.setOnClickListener(v -> {
            //TODO display user details activity
            Toast.makeText(context, holder.user_name_in_card.getText(), Toast.LENGTH_SHORT).show();
        });

        //TODO  set these values as those of the providers in the database

        holder.user_in_card_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_recycler_view));
        holder.user_name_in_card.setText(usersPerCategoryList.get(position).getFirst_name());
        holder.txt_userService.setText(usersPerCategoryList.get(position).getLast_name());
        holder.user_pay_rate_currency.setText(usersPerCategoryList.get(position).getLast_name());
        holder.user_pay_rate_value.setText(usersPerCategoryList.get(position).getFirst_name());
        holder.user_pay_rate_duration.setText(usersPerCategoryList.get(position).getLast_name());
        holder.service_category_offered = usersPerCategoryList.get(position).getFirst_name();
        holder.service_offered = usersPerCategoryList.get(position).getLast_name();
        holder.highest_education_certification = usersPerCategoryList.get(position).getFirst_name();
        holder.certificate_attained = usersPerCategoryList.get(position).getFirst_name();

        String url = usersPerCategoryList.get(position).getProfile_picture_url();
        Picasso.get()
                .load(url)
                .centerCrop()
                .fit()
                .into(holder.user_cover_photo);

    }

    @Override
    public int getItemCount() {
        return usersPerCategoryList.size();
    }

    public static class ViewHolderUsersHorizontal extends RecyclerView.ViewHolder{

        String service_category_offered;
        String service_offered;
        String highest_education_certification;
        String certificate_attained;
        int age;
        ImageView user_cover_photo;
        TextView user_name_in_card, txt_userService, user_pay_rate_currency,
                user_pay_rate_value, user_pay_rate_duration, integer_likes;
        MaterialCardView top_cert, user_in_card_layout;

        public ViewHolderUsersHorizontal(@NonNull View itemView) {
            super(itemView);
            user_cover_photo = itemView.findViewById(R.id.user_cover_photo);
            user_name_in_card = itemView.findViewById(R.id.user_name_in_card);
            txt_userService = itemView.findViewById(R.id.txt_userService);
            user_pay_rate_currency = itemView.findViewById(R.id.user_pay_rate_currency);
            user_pay_rate_value = itemView.findViewById(R.id.user_pay_rate_value);
            user_pay_rate_duration = itemView.findViewById(R.id.user_pay_rate_duration);
            integer_likes = itemView.findViewById(R.id.integer_likes);
            user_in_card_layout = itemView.findViewById(R.id.user_in_card_layout);

        }
    }
}