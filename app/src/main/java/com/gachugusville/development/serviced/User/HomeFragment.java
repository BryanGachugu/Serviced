package com.gachugusville.development.serviced.User;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Adapters.HomeCardsAdapter;
import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.Main.HomeCard;
import com.gachugusville.development.serviced.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    CardView toProvidersApp;
    RecyclerView lading_page_rcView;
    List<HomeCard> homeCards;
    private String Uid;
    private String country;
    HomeCardsAdapter homeCardsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BottomNavigationView bottom_nav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //initialize our views
        toProvidersApp = view.findViewById(R.id.toProvidersApp);
        bottom_nav = Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_nav);
        db = FirebaseFirestore.getInstance();
        lading_page_rcView = view.findViewById(R.id.lading_page_rcView);

        homeCards = new ArrayList<>();
        homeCardsAdapter = new HomeCardsAdapter(homeCards, getContext());
        lading_page_rcView.setHasFixedSize(true);
        lading_page_rcView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lading_page_rcView.setAdapter(homeCardsAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Uid = user.getUid();
            FirebaseFirestore.getInstance().collection("Users").document(Uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            country = Objects.requireNonNull(documentSnapshot.toObject(User.class)).getCountry();
                        }
                    });
        }


        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("ServiceCategory")
                    .limit(10)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.d("Error", Objects.requireNonNull(error.getMessage()));
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                final HomeCard card = documentChange.getDocument().toObject(HomeCard.class);
                                homeCards.add(card);
                                homeCardsAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        } catch (Exception e){
            Log.d("ProviderRead", e.getMessage());
        }

        return view;

    }

}