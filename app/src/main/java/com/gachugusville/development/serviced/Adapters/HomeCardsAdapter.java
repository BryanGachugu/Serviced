package com.gachugusville.development.serviced.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.Main.HomeCard;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Provider;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeCardsAdapter extends RecyclerView.Adapter<HomeCardsAdapter.ViewHolderCards> {

    private final List<HomeCard> homeCardList;
    private final Context context;

    public HomeCardsAdapter(List<HomeCard> homeCardList, Context context) {
        this.homeCardList = homeCardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderCards onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_layout, parent, false);
        return new ViewHolderCards(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCards holder, int position) {
        String txt_see_all = "See all";
        holder.home_cardTitle.setText(homeCardList.get(position).getservice_category_name());
        holder.txt_see_all.setText(txt_see_all);

        holder.home_card_layout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_recycler_view));

        List<Provider> providers = new ArrayList<>();
        UserRecyclerViewPerCategoryAdapter userRecyclerViewPerCategoryAdapter = new UserRecyclerViewPerCategoryAdapter(providers, context);
        holder.users_per_category_rcView.setHasFixedSize(true);
        holder.users_per_category_rcView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.users_per_category_rcView.setAdapter(userRecyclerViewPerCategoryAdapter);

        if (!User.getInstance().getCountry().isEmpty()) {
            FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
            mFirebaseFirestore.collection("Providers")
                    .whereEqualTo("country", User.getInstance().getCountry())
                    .limit(10)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d("Error", Objects.requireNonNull(error.getMessage()));
                        }
                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                final Provider provider = documentChange.getDocument().toObject(Provider.class);
                                provider.setDocumentId(documentChange.getDocument().getId());
                                providers.add(provider);
                                userRecyclerViewPerCategoryAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        } else {
            FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
            mFirebaseFirestore.collection("Providers")
                    .limit(10)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d("Error", Objects.requireNonNull(error.getMessage()));
                        }
                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                final Provider provider = documentChange.getDocument().toObject(Provider.class);
                                provider.setDocumentId(documentChange.getDocument().getId());
                                Log.d("Doc_IdFromDb = ", documentChange.getDocument().getId());
                                providers.add(provider);
                                userRecyclerViewPerCategoryAdapter.notifyDataSetChanged();
                            }
                        }
                    });

            //If there are no users in a Category, set its view to Gone

        }
    }

    @Override
    public int getItemCount() {
        return homeCardList.size();
    }

    public static class ViewHolderCards extends RecyclerView.ViewHolder {
        TextView home_cardTitle, txt_see_all;
        RecyclerView users_per_category_rcView;
        MaterialCardView home_card_layout;

        public ViewHolderCards(@NonNull View itemView) {
            super(itemView);
            home_cardTitle = itemView.findViewById(R.id.home_cardTitle);
            txt_see_all = itemView.findViewById(R.id.txt_see_all);
            users_per_category_rcView = itemView.findViewById(R.id.users_per_category_rcView);
            home_card_layout = itemView.findViewById(R.id.home_card_layout);
        }
    }
}
