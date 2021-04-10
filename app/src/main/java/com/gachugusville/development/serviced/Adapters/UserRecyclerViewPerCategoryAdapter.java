package com.gachugusville.development.serviced.Adapters;

import android.annotation.SuppressLint;
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
import com.gachugusville.development.serviced.Utils.Provider;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserRecyclerViewPerCategoryAdapter extends RecyclerView.Adapter<UserRecyclerViewPerCategoryAdapter.ViewHolderUsersHorizontal> {
    List<Provider> providersList;
    Context context;

    public UserRecyclerViewPerCategoryAdapter(List<Provider> providersList, Context context) {
        this.providersList = providersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderUsersHorizontal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_in_category_layout, parent, false);
        return new ViewHolderUsersHorizontal(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsersHorizontal holder, int position) {
        holder.user_in_card_layout.setOnClickListener(v -> {
            //TODO display user details activity
            Toast.makeText(context, holder.user_name_in_card.getText(), Toast.LENGTH_SHORT).show();
        });

        //TODO  set these values as those of the providers in the database

        holder.user_in_card_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_recycler_view));
        //Set brand or Username according to Provider's data
        if (!holder.user_name_in_card.getText().toString().isEmpty())
            holder.user_name_in_card.setText(providersList.get(position).getUser_name());
        else holder.user_name_in_card.setText(providersList.get(position).getBrand_name());
        holder.txt_userService.setText(providersList.get(position).getPersonal_description());
        holder.user_pay_rate_currency.setText("$");
        holder.user_pay_rate_value.setText("50");
        holder.user_pay_rate_duration.setText("per job");
        holder.service_category_offered = providersList.get(position).getService_category();
        holder.service_offered = providersList.get(position).getService_identity();
        holder.highest_education_certification = providersList.get(position).getUser_name();
        holder.certificate_attained = providersList.get(position).getUser_name();

        String url = providersList.get(position).getProfile_pic_url();
        Picasso.get()
                .load(url)
                .centerCrop()
                .fit()
                .into(holder.user_cover_photo);

    }

    @Override
    public int getItemCount() {
        return providersList.size();
    }

    public static class ViewHolderUsersHorizontal extends RecyclerView.ViewHolder {

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
