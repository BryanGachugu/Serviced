package com.gachugusville.development.serviced.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gachugusville.development.serviced.R;

public class RetailerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setEnterTransition(null);

    }
}