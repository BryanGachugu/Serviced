package com.gachugusville.development.serviced.Provider.AccountCreation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gachugusville.development.serviced.Provider.AccountCreation.SupportClasses.CategorySpinnerAdapter;
import com.gachugusville.development.serviced.Provider.AccountCreation.SupportClasses.ServiceCategoryList;
import com.gachugusville.development.serviced.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceSetUpActivity extends AppCompatActivity {

    TextView welcome_msg;
    RecyclerView service_category_rv;
    List<ServiceCategoryList> serviceCategoryList;
    CategorySpinnerAdapter categorySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_set_up);
        //Set Welcome message according to previous data provided
        welcome_msg = findViewById(R.id.welcome_msg);
        service_category_rv = findViewById(R.id.service_category_rv);
        setWelcomeMsg();
        serviceCategoryList = new ArrayList<>();
        categorySpinnerAdapter = new CategorySpinnerAdapter(this, serviceCategoryList);
        service_category_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        service_category_rv.setAdapter(categorySpinnerAdapter);


        FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("ServiceCategory")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Error", Objects.requireNonNull(error.getMessage()));
                    }
                    assert value != null;
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            final ServiceCategoryList category = documentChange.getDocument().toObject(ServiceCategoryList.class);
                            serviceCategoryList.add(category);
                            categorySpinnerAdapter.notifyDataSetChanged();
                        }
                    }
                });


    }

    private void setWelcomeMsg() {
        Intent intent = getIntent();
        String first_name = intent.getStringExtra("first_name");
        String brand_name = intent.getStringExtra("brand_name");
        if (first_name.equals(""))
            welcome_msg.setText(String.format("Welcome %s", brand_name));
        else
            welcome_msg.setText(String.format("Welcome %s", first_name));
    }
}