package com.gachugusville.development.serviced.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.Main.MainActivity;
import com.gachugusville.development.serviced.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
            getAllDataFromDatabase();
        } else
            startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void getAllDataFromDatabase() {
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        documentSnapshot.toObject(User.class);
                        
                    }
                })
    }

}