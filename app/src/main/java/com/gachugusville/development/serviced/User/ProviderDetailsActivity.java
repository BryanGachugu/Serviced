package com.gachugusville.development.serviced.User;

import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Adapters.ProviderReviewsAdapter;
import com.gachugusville.development.serviced.Adapters.ProviderSkillsAdapter;
import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.CustomDialog;
import com.gachugusville.development.serviced.Utils.JobRequestModel;
import com.gachugusville.development.serviced.Utils.Provider;
import com.gachugusville.development.serviced.Utils.ProviderReviews;
import com.gachugusville.development.serviced.Utils.Skills;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProviderDetailsActivity extends AppCompatActivity {

    private Provider provider;
    private List<ProviderReviews> reviews;
    private ProviderReviewsAdapter providerReviewsAdapter;
    private ProviderSkillsAdapter providerSkillsAdapter;
    private RecyclerView reviews_rc;
    private RecyclerView provider_skills_rc;
    public static final String ACCOUNT_SID = "AC6eeeead7f60b007a3097a9a2be09a576";
    public static final String AUTH_TOKEN = "010b70af1ba401a150541af86999b782";
    private static final String phoneTo = "+254792432505";
    private static final String phoneFrom = "+12147314589";
    private CustomDialog customDialog;
    private List<Skills> skills;
    private CircleImageView provider_profile;
    private ImageView img_provider_cover;
    private TextView txt_provider_name, txt_provider_service_identity, txt_distance, txt_provider_rating,
            txt_provider_numOf_reviews, txt_provider_numOf_likes, txt_provider_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_details);
        txt_provider_name = findViewById(R.id.txt_provider_name);
        FloatingActionButton img_provider_details_back = findViewById(R.id.img_provider_details_back);
        provider_profile = findViewById(R.id.provider_profile);
        img_provider_cover = findViewById(R.id.img_provider_cover);
        txt_provider_service_identity = findViewById(R.id.txt_provider_service_identity);
        provider_skills_rc = findViewById(R.id.provider_skills_rc);
        txt_distance = findViewById(R.id.txt_distance);
        txt_provider_rating = findViewById(R.id.txt_provider_rating);
        txt_provider_numOf_reviews = findViewById(R.id.txt_provider_numOf_reviews);
        txt_provider_numOf_likes = findViewById(R.id.txt_provider_numOf_likes);
        txt_provider_description = findViewById(R.id.txt_provider_description);
        reviews_rc = findViewById(R.id.reviews_rc);
        customDialog = new CustomDialog(this);
        getDetails();

        findViewById(R.id.message_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, UserMapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("provider_data", provider);
            intent.putExtras(bundle);
            startActivity(intent);

        });

        img_provider_details_back.setOnClickListener(v -> ProviderDetailsActivity.super.onBackPressed());
        findViewById(R.id.btn_jobRequest).setOnClickListener(v ->
                {
                    if (isNetworkAvailable()) {
                        requestJob();
                    } else {
                        //TODO USE ALERTER LIBRARY
                        Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        updateData();

    }

    private void getReviews() {
        reviews = new ArrayList<>();
        providerReviewsAdapter = new ProviderReviewsAdapter(this, reviews);
        reviews_rc.setHasFixedSize(true);
        reviews_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviews_rc.setAdapter(providerReviewsAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String Uid = provider.getDocumentId();
        db.collection("Providers").document(Uid).collection("reviews")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d("Error", Objects.requireNonNull(error.getMessage()));
                    }
                    assert value != null;
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            final ProviderReviews review = documentChange.getDocument().toObject(ProviderReviews.class);
                            reviews.add(review);
                            providerReviewsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    private void requestJob() {
        //TODO a lot of customizations
        customDialog.startDialog();
        String customerUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        JobRequestModel jobRequest = new JobRequestModel(customerUid, "https://s.abcnews.com/images/GMA/beb-rexha-file-gty-jef-200224_hpMain_1x1_608.jpg",
                "Kate Winslet", "Writing", "pending", "Per Hour", 200, 0);

        FirebaseFirestore.getInstance().collection("Providers").document(provider.getDocumentId())
                .collection("Jobs")
                .add(jobRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        customDialog.dismissDialog();
                        Toast.makeText(ProviderDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                customDialog.dismissDialog();
                Toast.makeText(ProviderDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.d("failed", e.getLocalizedMessage());
            }
        });
    }

    private void updateData() {
        try {
            DocumentReference providerRef = FirebaseFirestore.getInstance().collection("Providers")
                    .document(provider.getDocumentId());
            providerRef.update("account_views", provider.getAccount_views() + 1);
        } catch (Exception e) {
            Log.d("account_views", e.getLocalizedMessage());
        }


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
        getReviews();
        calculateDistanceBetweenClientAndProvider();

    }

    private void getSkills() {
        skills = new ArrayList<>();
        providerSkillsAdapter = new ProviderSkillsAdapter(skills, this);
        provider_skills_rc.setHasFixedSize(true);
        provider_skills_rc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        provider_skills_rc.setAdapter(providerSkillsAdapter);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}