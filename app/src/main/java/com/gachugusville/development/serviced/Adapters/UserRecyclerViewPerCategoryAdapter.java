package com.gachugusville.development.serviced.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.User.ProviderDetailsActivity;
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
            Intent intent = new Intent(context, ProviderDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("provider_instance", providersList.get(position));
            intent.putExtras(bundle);
            context.startActivity(intent);

        });

        //TODO  set these values as those of the providers in the database

        holder.user_in_card_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_recycler_view));
        //Set brand or Username according to Provider's data
        holder.documentId = providersList.get(position).getDocumentId();
        holder.user_name_in_card.setText(providersList.get(position).getUser_name() + providersList.get(position).getBrand_name());
        holder.txt_userService.setText(providersList.get(position).getPersonal_description());
        calculateDistanceBetweenClientAndProvider(position, holder.provider_distance);
        holder.txt_pro_rating.setText(String.valueOf(providersList.get(position).getRating()));
        holder.service_category_offered = providersList.get(position).getService_category();
        holder.service_offered = providersList.get(position).getService_identity();
        holder.highest_education_certification = providersList.get(position).getUser_name();
        holder.certificate_attained = providersList.get(position).getUser_name();

        String url = providersList.get(position).getProfile_pic_url();
        if (!url.isEmpty()) {
            Picasso.get()
                    .load(url)
                    .centerCrop()
                    .fit()
                    .into(holder.user_cover_photo);
        } else {
            Picasso.get()
                    .load(R.drawable.test_1)
                    .into(holder.user_cover_photo);
        }

    }

    @Override
    public int getItemCount() {
        return providersList.size();
    }

    public static class ViewHolderUsersHorizontal extends RecyclerView.ViewHolder {

        String service_category_offered;
        String service_offered;
        String documentId;
        String highest_education_certification;
        String certificate_attained;
        ImageView user_cover_photo;
        TextView user_name_in_card, txt_userService,
                provider_distance, txt_pro_rating;
        MaterialCardView user_in_card_layout;

        public ViewHolderUsersHorizontal(@NonNull View itemView) {
            super(itemView);
            user_cover_photo = itemView.findViewById(R.id.user_cover_photo);
            user_name_in_card = itemView.findViewById(R.id.user_name_in_card);
            txt_userService = itemView.findViewById(R.id.txt_userService);
            provider_distance = itemView.findViewById(R.id.provider_distance);
            txt_pro_rating = itemView.findViewById(R.id.txt_pro_rating);
            user_in_card_layout = itemView.findViewById(R.id.user_in_card_layout);

        }
    }

    private void calculateDistanceBetweenClientAndProvider(int position, TextView txt_distance) {
        try {
            //Get client's location
            Location client_location = new Location("client_location");
            client_location.setLatitude(User.getInstance().getLatitude());
            client_location.setLongitude(User.getInstance().getLongitude());

            //Get provider's location
            Location provider_location = new Location("provider_location");
            provider_location.setLatitude(providersList.get(position).getLatitude());
            provider_location.setLongitude(providersList.get(position).getLongitude());

            Log.d("ClientLat", String.valueOf(client_location));
            Log.d("ProvLat", String.valueOf(provider_location));

            //get the distance between the two
            int distance_between = (int) client_location.distanceTo(provider_location);
            providersList.get(position).setDistance(distance_between);
            if (distance_between > 1000) {
                distance_between = (int) client_location.distanceTo(provider_location) / 1000;
                txt_distance.setText(String.format("%s km", distance_between));
            } else {
                txt_distance.setText(String.format("%s m", distance_between));
            }

        } catch (Exception e) {
            Log.d("LocationCalcError", e.getMessage());
        }

    }
}
