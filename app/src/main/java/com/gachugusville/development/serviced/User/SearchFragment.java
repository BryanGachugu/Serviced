package com.gachugusville.development.serviced.User;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.gachugusville.development.serviced.Adapters.ProviderRecyclerAdapter;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Constants;
import com.gachugusville.development.serviced.Utils.Provider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {
    private FirebaseFirestore db;
    private List<Provider> listProviders;
    private ProviderRecyclerAdapter providerRecyclerAdapter;
    private RecyclerView search_RC;
    private EditText edt_search_string;
    private Index index;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        search_RC = view.findViewById(R.id.search_RC);
        edt_search_string = view.findViewById(R.id.edt_search_string);
        listProviders = new ArrayList<>();
        getProviders();

        Client client = new Client(Constants.ALGOLIA_APP_ID, Constants.ALGOLIA_API_KEY);
        index = client.getIndex("serviced_PROVIDERS");

        edt_search_string.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CompletionHandler completionHandler = (content, error1) -> {
                    try {
                        if (content != null) {
                            JSONArray hits = content.getJSONArray("hits");
                            providerRecyclerAdapter.filterRecyclerView(hits);
                        } else {
                            //TODO NO RESULTS
                            Log.d("Results_Empty", "EMPTY");
                            listProviders.clear();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                index.searchAsync(new Query(edt_search_string.getText().toString()),
                        completionHandler);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    private void getProviders() {
        db.collection("Providers")
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
                            listProviders.add(provider);
                            providerRecyclerAdapter = new ProviderRecyclerAdapter(listProviders, getContext());
                            search_RC.setNestedScrollingEnabled(true);
                            search_RC.setAdapter(providerRecyclerAdapter);
                            search_RC.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            providerRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
