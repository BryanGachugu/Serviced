package com.gachugusville.development.serviced.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gachugusville.development.serviced.Common.LogInActivity;
import com.gachugusville.development.serviced.Common.RegistrationActivities.SignUp;
import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        //YoYo is a github library for applying animation to views
        TextView textView = findViewById(R.id.txtWelcome);
        YoYo.with(Techniques.Shake).duration(4000).repeat(10).playOn(textView);

        MaterialButton btn_signUp = findViewById(R.id.btn_signUp);
        YoYo.with(Techniques.SlideInUp).duration(6000).playOn(btn_signUp);

        MaterialButton btn_logIn = findViewById(R.id.btn_logIn);
        YoYo.with(Techniques.SlideInUp).duration(6000).playOn(btn_logIn);

        btn_signUp.setOnClickListener(v -> {
            Intent sharedIntent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(sharedIntent);
            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
        });

        btn_logIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);

        });


    }

}