package com.gachugusville.development.serviced.User;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Adapters.ProviderSkillsAdapter;
import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.JobRequestModel;
import com.gachugusville.development.serviced.Utils.Provider;
import com.gachugusville.development.serviced.Utils.Skills;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderDetailsActivity extends AppCompatActivity {
    private Provider provider;
    private ProviderSkillsAdapter providerSkillsAdapter;
    private RecyclerView provider_skills_rc;
    private List<Skills> skills;
    private CircleImageView provider_profile;
    private FloatingActionButton img_provider_details_back;
    private ImageView img_provider_cover;
    private TextView txt_provider_name, txt_provider_service_identity, txt_distance, txt_provider_rating,
            txt_provider_numOf_reviews, txt_provider_numOf_likes, txt_provider_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);
        txt_provider_name = findViewById(R.id.txt_provider_name);
        img_provider_details_back = findViewById(R.id.img_provider_details_back);
        provider_profile = findViewById(R.id.provider_profile);
        img_provider_cover = findViewById(R.id.img_provider_cover);
        txt_provider_service_identity = findViewById(R.id.txt_provider_service_identity);
        provider_skills_rc = findViewById(R.id.provider_skills_rc);
        txt_distance = findViewById(R.id.txt_distance);
        txt_provider_rating = findViewById(R.id.txt_provider_rating);
        txt_provider_numOf_reviews = findViewById(R.id.txt_provider_numOf_reviews);
        txt_provider_numOf_likes = findViewById(R.id.txt_provider_numOf_likes);
        txt_provider_description = findViewById(R.id.txt_provider_description);

        img_provider_details_back.setOnClickListener(v -> ProviderDetailsActivity.super.onBackPressed());
        findViewById(R.id.btn_jobRequest).setOnClickListener(v -> requestJob());
        updateData();
        getDetails();
    }

    private void requestJob() {
        //TODO a lot of customizations
        String customerUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        JobRequestModel jobRequest = new JobRequestModel(customerUid, User.getInstance().getFirst_name(),
                "inProgress", "perJob", 500.0);
        FirebaseFirestore.getInstance().collection("Providers").document(provider.getDocumentId())
                .collection("Jobs")
                .add(jobRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        Toast.makeText(ProviderDetailsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProviderDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData() {
        DocumentReference providerRef = FirebaseFirestore.getInstance().collection("Providers")
                .document(provider.getDocumentId());
        providerRef.update("account_views", provider.getAccount_views() + 1);


    }

    private void getDetails() {
        Intent intent = getIntent();
        provider = (Provider) intent.getSerializableExtra("provider_instance");
        handleReceivedDetails();
    }

    private void handleReceivedDetails() {
        if (provider.getUser_name().isEmpty()) {
            txt_provider_name.setText(provider.getBrand_name());
        } else {
            txt_provider_name.setText(provider.getUser_name());
        }
        if (provider.getProfile_pic_url().isEmpty()) {
            //TODO set Default image according to category
            Toast.makeText(this, "DP_error", Toast.LENGTH_SHORT).show();
            Log.d("DP_error", provider.getDocumentId());
        } else {
            Picasso.get()
                    .load(provider.getProfile_pic_url())
                    .fit()
                    .into(provider_profile);
        }
        if (provider.getProvider_cover_photo_url().isEmpty()) {
            //TODO Load default service category cover
            Log.d("Cover_missing for ", provider.getUser_name() + provider.getBrand_name());
        } else {
            Picasso.get()
                    .load(provider.getProvider_cover_photo_url())
                    .into(img_provider_cover);
        }
        try {
            txt_provider_rating.setText(String.valueOf(provider.getRating()));
        } catch (Exception e) {
            txt_provider_rating.setError("error");
            Log.d("rating_error for : ", provider.getUser_name());
        }
        try {
            txt_provider_numOf_reviews.setText(String.valueOf(provider.getNumber_of_reviews()));
        } catch (Exception e) {
            Log.d("num_of_reviewsError_for", provider.getUser_name());
        }
        txt_provider_description.setText(provider.getPersonal_description());
        txt_provider_service_identity.setText(provider.getService_identity());
        txt_provider_numOf_likes.setText(String.valueOf(provider.getNumber_of_profile_likes()));
        getSkills();
        calculateDistanceBetweenClientAndProvider();

    }

    private void getSkills() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Providers").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("Skills")
                        .addSnapshotListener((value, error) -> {
                            if (error != null) {
                                Log.d("Error", Objects.requireNonNull(error.getMessage()));
                            }
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    final Skills skill = documentChange.getDocument().toObject(Skills.class);
                                    skills.add(skill);
                                    providerSkillsAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(ProviderDetailsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                Log.d("SkillsError", e.getLocalizedMessage());
            }
        }

    }

    private void calculateDistanceBetweenClientAndProvider() {
        try {
            //Get client's location
            Location client_location = new Location("client_location");
            client_location.setLatitude(User.getInstance().getLatitude());
            client_location.setLongitude(User.getInstance().getLongitude());

            //Get provider's location
            Location provider_location = new Location("provider_location");
            provider_location.setLatitude(provider.getLatitude());
            provider_location.setLongitude(provider.getLongitude());

            //get the distance between the two
            double distance_between = client_location.distanceTo(provider_location);
            txt_distance.setText(String.format("%s km away", distance_between));
        } catch (Exception e) {
            Log.d("LocationCalcError", e.getMessage());
        }

    }


}