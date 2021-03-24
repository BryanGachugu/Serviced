package com.gachugusville.development.serviced.Provider.AccountCreation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gachugusville.development.serviced.R;
import com.google.android.material.textfield.TextInputEditText;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.util.ArrayList;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class TagsActivity extends AppCompatActivity {

    ImageView tags_back_btn;
    TextInputEditText edt_provider_description, note_to_user, edit_distance_radius;

    List<String> tags;
    NachoTextView nacho_text_view;
    MaterialDayPicker day_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        //Hooks
        nacho_text_view = findViewById(R.id.nacho_text_view);
        edt_provider_description = findViewById(R.id.edt_provider_description);
        note_to_user = findViewById(R.id.note_to_user);
        tags_back_btn = findViewById(R.id.tags_back_btn);
        tags_back_btn.setOnClickListener(v -> super.onBackPressed());
        edit_distance_radius = findViewById(R.id.edit_distance_radius);
        day_picker = findViewById(R.id.day_picker);


        //Get passed data from the category Adapter
        try {
            Bundle bundle = getIntent().getExtras();
            tags = new ArrayList<>();
            tags.addAll(bundle.getStringArrayList("category_tags"));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tags);
            nacho_text_view.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
            nacho_text_view.setAdapter(adapter);
        } catch (Exception e) {
            Log.d("tags", String.valueOf(e.getMessage()));
        }

    }
}