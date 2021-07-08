package com.gachugusville.development.serviced.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.User.ProviderDetailsActivity;
import com.gachugusville.development.serviced.Utils.Provider;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderRecyclerAdapter extends RecyclerView.Adapter<ProviderRecyclerAdapter.SearchedProviderViewHolder> {

    List<Provider> searchList;
    Context context;

    public ProviderRecyclerAdapter(List<Provider> searchList, Context context) {
        this.searchList = searchList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchedProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_results, parent, false);
        return new SearchedProviderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedProviderViewHolder holder, int position) {
        holder.provider_in_search_result_layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProviderDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("FROM_ACTIVITY", "Search");
            bundle.putSerializable("provider_instance", searchList.get(position));
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        if (!searchList.get(position).getProfile_pic_url().equals("")) {
            Picasso.get()
                    .load(searchList.get(position).getProfile_pic_url())
                    .into(holder.img_search_dp);
        }

        holder.txt_provider_name.setText(searchList.get(position).getUser_name());
        holder.txt_provider_service.setText(searchList.get(position).getService_identity());
        holder.provider_distance.setText(String.valueOf(searchList.get(position).getUser_name()));
        holder.provider_number_of_jobs.setText(String.valueOf(searchList.get(position).getJobs_done()));
        holder.provider_number_of_reviews.setText(String.valueOf(searchList.get(position).getNumber_of_reviews()));
        holder.txt_provider_description.setText(searchList.get(position).getPersonal_description());
        holder.provider_rating_bar.setRating(searchList.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class SearchedProviderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout provider_in_search_result_layout;
        CircleImageView img_search_dp;

        TextView txt_provider_name, txt_provider_service, provider_distance,
                provider_number_of_jobs, provider_number_of_reviews, txt_provider_description;
        RatingBar provider_rating_bar;

        public SearchedProviderViewHolder(@NonNull View itemView) {
            super(itemView);
            img_search_dp = itemView.findViewById(R.id.img_search_dp);
            txt_provider_name = itemView.findViewById(R.id.txt_provider_name);
            txt_provider_service = itemView.findViewById(R.id.txt_provider_service);
            provider_distance = itemView.findViewById(R.id.provider_distance);
            provider_number_of_jobs = itemView.findViewById(R.id.provider_number_of_jobs);
            provider_number_of_reviews = itemView.findViewById(R.id.provider_number_of_reviews);
            provider_rating_bar = itemView.findViewById(R.id.provider_rating_bar);
            txt_provider_description = itemView.findViewById(R.id.txt_provider_description);
            provider_in_search_result_layout = itemView.findViewById(R.id.provider_in_search_result_layout);
        }
    }

}
