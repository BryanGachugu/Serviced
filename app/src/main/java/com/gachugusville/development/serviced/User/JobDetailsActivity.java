package com.gachugusville.development.serviced.User;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;

public class JobDetailsActivity extends AppCompatActivity {

    MaterialButton buy_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_job_details);



    }
}