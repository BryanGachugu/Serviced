package com.gachugusville.development.serviced.Provider.AccountCreation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.gachugusville.development.serviced.R;
import com.google.android.material.textfield.TextInputEditText;

public class Availability_Activity extends AppCompatActivity {

    TextInputEditText edit_distance_radius;
    CheckBox checkbox_available_countrywide, checkbox_always_available;
    LinearLayout linear_layout_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_);

        checkbox_available_countrywide = findViewById(R.id.checkbox_available_countrywide);
        checkbox_always_available = findViewById(R.id.checkbox_always_available);
        edit_distance_radius = findViewById(R.id.edit_distance_radius);
        linear_layout_distance = findViewById(R.id.linear_layout_distance);

        checkbox_available_countrywide.setOnClickListener(v -> {
            if (checkbox_available_countrywide.isChecked()) {
                edit_distance_radius.setText("");
                linear_layout_distance.setVisibility(View.GONE);
            } else linear_layout_distance.setVisibility(View.VISIBLE);
        });


    }
}