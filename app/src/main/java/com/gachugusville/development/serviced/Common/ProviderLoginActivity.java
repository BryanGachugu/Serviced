package com.gachugusville.development.serviced.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.Common.RegistrationActivities.SignUp;
import com.gachugusville.development.serviced.R;

public class ProviderLoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_provider_login);
    }

    public void callLogInScreen(View view) {
        startActivity(new Intent(getApplicationContext(), RetailerLogin.class));

    }

    public void callSignUpFirstActivity(View view) {
        startActivity(new Intent(getApplicationContext(), SignUp.class));
    }
}