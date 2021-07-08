package com.gachugusville.development.serviced.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gachugusville.development.serviced.Common.LogInActivity;
import com.gachugusville.development.serviced.Common.RegistrationActivities.SignUp;
import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private static final int GPS_REQUEST_CODE = 1001;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable landscape mod

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                //Below unassigned variable needed
                LocationSettingsResponse response = task.getResult(ApiException.class);
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(MainActivity.this, GPS_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            switch (resultCode) {
                case MainActivity.RESULT_OK:
                    Toast.makeText(this, "GPS turned on successfully", Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.RESULT_CANCELED:
                    Toast.makeText(this, "Location permission needed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        }

    }

}