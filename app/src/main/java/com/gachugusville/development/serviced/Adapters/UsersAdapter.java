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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolderCategories> {
    private final List<User> userList;
    Context mcontext;

    public UsersAdapter(Context context, List<User> list) {
        userList = list;
        mcontext = context;

    }

    @NonNull
    @Override
    public ViewHolderCategories onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new ViewHolderCategories(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCategories holder, final int position) {

        holder.user_card.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.fade_transition_recycler_view));
        holder.user_card.setOnClickListener(v -> Toast.makeText(mcontext, holder.user_name.getText(), Toast.LENGTH_SHORT).show());


        //Set the background
        Picasso.get()
                .load(userList.get(position).getProfile_picture_url())
                .fit()
                .centerCrop()
                .into(holder.user_image);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolderCategories extends RecyclerView.ViewHolder {

        TextView user_name, txt_age, txt_userService, integer_likes;
        ImageView user_image;
        String profile_picture_url, service_category_offered, about_user,
                highest_education_certification, certificate_attained, phone;
        MaterialCardView user_card;

        public ViewHolderCategories(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            txt_age = itemView.findViewById(R.id.txt_age);
            txt_userService = itemView.findViewById(R.id.txt_userService);
            integer_likes = itemView.findViewById(R.id.integer_likes);
            user_image = itemView.findViewById(R.id.user_image);
            user_card = itemView.findViewById(R.id.user_card);


        }
    }


}