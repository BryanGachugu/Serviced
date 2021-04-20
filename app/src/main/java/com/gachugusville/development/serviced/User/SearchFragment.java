package com.gachugusville.development.serviced.User;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gachugusville.development.serviced.Adapters.CustomSearchAdapter;
import com.gachugusville.development.serviced.R;

public class SearchFragment extends Fragment {
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            //execution come here when an item is clicked from
            //the search results displayed after search form is submitted
            //you can continue from here with user clicked search item
            Toast.makeText(getContext(),
                    "clicked search result item is" + ((TextView) view1).getText(),
                    Toast.LENGTH_SHORT).show();
        });
        // search
        handleSearch();
        return view;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch();
    }
    private void handleSearch() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);

            CustomSearchAdapter adapter = new CustomSearchAdapter(this,
                    android.R.layout.simple_dropdown_item_1line,
                    StoresData.filterData(searchQuery));
            listView.setAdapter(adapter);

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            String selectedSuggestionRowId =  intent.getDataString();
            //execution comes here when an item is selected from search suggestions
            //you can continue from here with user selected search item
            Toast.makeText(getContext(), "selected search suggestion "+selectedSuggestionRowId,
                    Toast.LENGTH_SHORT).show();
        }
    }
}