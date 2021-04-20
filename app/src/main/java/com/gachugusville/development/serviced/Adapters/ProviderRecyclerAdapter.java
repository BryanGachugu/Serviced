package com.gachugusville.development.serviced.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Provider;
import com.google.android.material.card.MaterialCardView;

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

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class SearchedProviderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_search_dp;
        TextView txt_provider_name, txt_provider_service, user_pay_rate_currency,
                user_pay_rate_value, user_pay_rate_duration, integer_likes;

        public SearchedProviderViewHolder(@NonNull View itemView) {
            super(itemView);
           
        }
    }

}
