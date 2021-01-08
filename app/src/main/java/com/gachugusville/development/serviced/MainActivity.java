package com.gachugusville.development.serviced;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //YoYo is a github library for applying animation to views
        TextView textView = findViewById(R.id.txtWelcome);
        YoYo.with(Techniques.Shake).duration(4000).repeat(4).playOn(textView);

        MaterialButton btn_googleSignUp = findViewById(R.id.btn_googleSignUp);
        YoYo.with(Techniques.SlideInUp).duration(6000).playOn(btn_googleSignUp);

        MaterialButton btn_LogIn = findViewById(R.id.btn_logIn);
        YoYo.with(Techniques.SlideInUp).duration(6000).playOn(btn_LogIn);

        MaterialButton btn_signUp = findViewById(R.id.btn_signUp);
        YoYo.with(Techniques.SlideInUp).duration(6000).playOn(btn_signUp);


    }


}