package com.gachugusville.development.serviced.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

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
        getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            docRef = db.collection("Users").document(uid);
            getUserData();
        } else
            startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void getUserData() {
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            documentSnapshot.toObject(User.class);
                        } catch (Exception e) {
                            Log.d("UserValueErrors", e.getMessage());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d("UserDataRetrieveError", e.getMessage()));
        startActivity(new Intent(SplashScreen.this, DashboardActivity.class));
    }



}