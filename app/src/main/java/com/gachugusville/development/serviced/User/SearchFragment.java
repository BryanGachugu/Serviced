package com.gachugusville.development.serviced.User;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Adapters.ProviderRecyclerAdapter;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Provider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Provider> listProviders;
    private ProviderRecyclerAdapter providerRecyclerAdapter;
    private RecyclerView search_RC;
    private EditText edt_user_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        search_RC = view.findViewById(R.id.search_RC);
        edt_user_name = view.findViewById(R.id.edt_user_name);

        

        return view;

    }

    private void getProviders() {
        db.collection("Providers").whereEqualTo("service_name", 1)
                .limit(10)
                .whereLessThanOrEqualTo("location", 85)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            System.err.println("Listen failed:" + e);
                            return;
                        }

                        listProviders = new ArrayList<>();

                        for (DocumentSnapshot doc : snapshots) {
                            Provider provider = doc.toObject(Provider.class);
                            listProviders.add(provider);
                        }
                        updateListUsers((ArrayList<Provider>) listProviders);
                    }
                });
    }

    private void searchUsers(String recherche) {
        if (recherche.length() > 0)
            recherche = recherche.substring(0, 1).toUpperCase() + recherche.substring(1).toLowerCase();

        ArrayList<Provider> results = new ArrayList<>();
        for (Provider provider : listProviders) {
            if (provider.getUser_name() != null && provider.getUser_name().contains(recherche)) {
                results.add(provider);
            }
        }
        updateListUsers(results);
    }

    private void updateListUsers(ArrayList<Provider> listUsers) {

        // Sort the list by date
        Collections.sort(listUsers, new Comparator<Provider>() {
            @Override
            public int compare(Provider o1, Provider o2) {
                int res = -1;
                if (o1.getRating() > (o2.getRating())) {
                    res = 1;
                }
                return res;
            }
        });

        providerRecyclerAdapter = new ProviderRecyclerAdapter(listUsers, getContext());
        search_RC.setNestedScrollingEnabled(true);
        search_RC.setAdapter(providerRecyclerAdapter);
        search_RC.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        providerRecyclerAdapter.notifyDataSetChanged();
    }

}
