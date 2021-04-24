package com.gachugusville.development.serviced.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.R;

public class ProviderDetailsActivity extends AppCompatActivity {
    private TextView txt_provider_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);
        txt_provider_name = findViewById(R.id.txt_provider_name);
        getDetails();
    }

    private void getDetails() {
        Intent intent = getIntent();
        txt_provider_name.setText(intent.getStringExtra("provider_brand_name"));
    }
}