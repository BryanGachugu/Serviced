package com.gachugusville.development.serviced.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.Common.RegistrationActivities.SignUpSecondActivity;
import com.gachugusville.development.serviced.Main.MainActivity;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.User.DashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            docRef = db.collection("Users").document(uid);
            getUserData();
        } else
            startActivity(new Intent(this, MainActivity.class));
    }

    private void getUserData() {
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            //Document exists, that means user data is available
                            User.getInstance().setFirst_name(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getFirst_name());
                            User.getInstance().setLast_name(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getLast_name());
                            User.getInstance().setFirst_name(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getFirst_name());
                            User.getInstance().setProfile_picture_url(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getProfile_picture_url());
                            User.getInstance().setPhone_number(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getPhone_number());
                            User.getInstance().setEmail(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getEmail());
                            User.getInstance().setCountry(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getCountry());
                            User.getInstance().setLatitude(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getLatitude());
                            User.getInstance().setLongitude(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getLongitude());
                            User.getInstance().setRating(Objects.requireNonNull(documentSnapshot.toObject(User.getInstance().getClass())).getRating());

                        } catch (Exception e) {
                            Log.d("UserValueErrors", e.getMessage());
                        }
                        startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                    } else {
                        //This means user did not complete registration, take him to the activity just after authentication
                        startActivity(new Intent(SplashScreen.this, SignUpSecondActivity.class));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("UserDataRetrieveError", e.getMessage());
                    //TODO make sure there is an active internet connection
                    //if there is no internet, the app continues with previous fetched data
                    startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
                });
    }


}