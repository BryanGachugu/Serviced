package com.gachugusville.development.serviced.User;

import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gachugusville.development.serviced.Adapters.ProviderReviewsAdapter;
import com.gachugusville.development.serviced.Adapters.ProviderSkillsAdapter;
import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.FCMNotificaton.APIService;
import com.gachugusville.development.serviced.FCMNotificaton.Client;
import com.gachugusville.development.serviced.FCMNotificaton.Data;
import com.gachugusville.development.serviced.FCMNotificaton.MyResponse;
import com.gachugusville.development.serviced.FCMNotificaton.NotificationSender;
import com.gachugusville.development.serviced.FCMNotificaton.Token;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.CustomDialog;
import com.gachugusville.development.serviced.Utils.JobRequestModel;
import com.gachugusville.development.serviced.Utils.Provider;
import com.gachugusville.development.serviced.Utils.ProviderReviews;
import com.gachugusville.development.serviced.Utils.Skills;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.infobip.mobile.messaging.Message;
import org.infobip.mobile.messaging.MobileMessaging;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderDetailsActivity extends AppCompatActivity {

    private APIService apiService;
    private Provider provider;
    private List<ProviderReviews> reviews;
    private ProviderReviewsAdapter providerReviewsAdapter;
    private ProviderSkillsAdapter providerSkillsAdapter;
    private RecyclerView reviews_rc;
    private RecyclerView provider_skills_rc;

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
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        updateToken();
        getDetails();

        new MobileMessaging
                .Builder(getApplication()).build();

        findViewById(R.id.message_btn).setOnClickListener(v -> {
            sendSmS();

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
        //This fastens activity start up by updating firebase value after activity starts
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::updateData, 500);



    }

    private void sendSmS() {
        try {
            Message message = new Message();
            message.setDestination("254792432505");
            message.setBody("New Job from Bryan Gachugu");
            MobileMessaging.getInstance(this).sendMessages(message);
        } catch (Exception e) {
            Log.d("MESSAGE_ERROR", e.getLocalizedMessage());
        }
    }

    private void getReviews() {
        reviews = new ArrayList<>();
        providerReviewsAdapter = new ProviderReviewsAdapter(this, reviews);
        reviews_rc.setHasFixedSize(true);
        reviews_rc.setLayoutManager(new GridLayoutManager(this, 2));
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
                .addOnSuccessListener(documentReference -> {
                    customDialog.dismissDialog();
                    Toast.makeText(ProviderDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(provider.getDocumentId()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            String userToken = snapshot.getValue(String.class);
                            sendNotification(userToken, "New Job request from " + User.getInstance().getLast_name(), " Request for App development");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

                }).addOnFailureListener(e -> {
            customDialog.dismissDialog();
            Toast.makeText(ProviderDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("failed", e.getLocalizedMessage());
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
        //TODO REMOVE BELOW LINE
        provider.setProvider_cover_photo_url("https://t4.ftcdn.net/jpg/02/86/02/67/360_F_286026740_xWkobcEk5g38qrH7cpfeImAnlUUSIrc5.jpg");
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
            Log.d("rating_error for : ", provider.getUser_name() + provider.getBrand_name());
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
        provider_skills_rc.setLayoutManager(new GridLayoutManager(this, 2));
        provider_skills_rc.setAdapter(providerSkillsAdapter);

        //TODO check this out
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            try {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Providers").document(provider.getDocumentId())
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

            Log.d("ClientLat", String.valueOf(client_location));
            Log.d("ProvLat", String.valueOf(provider_location));

            //get the distance between the two
            int distance_between = (int) client_location.distanceTo(provider_location);
            txt_distance.setText(String.valueOf(distance_between));
        } catch (Exception e) {
            Log.d("LocationCalcError", e.getMessage());
        }

    }

    /*
    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response[0] = org.apache.commons.io.IOUtils.toString(in, StandardCharsets.UTF_8);

                JSONObject jsonObject = new JSONObject(response[0]);
                JSONArray array = jsonObject.getJSONArray("routes");
                JSONObject routes = array.getJSONObject(0);
                JSONArray legs = routes.getJSONArray("legs");
                JSONObject steps = legs.getJSONObject(0);
                JSONObject distance = steps.getJSONObject("distance");
                parsedDistance[0] = distance.getString("text");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("DIST", parsedDistance[0]);
        return parsedDistance[0];
    }
    */

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateToken() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseMessaging.getInstance().getToken().toString();
        Token token = new Token(refreshToken);
        assert user != null;
        FirebaseDatabase.getInstance().getReference("Tokens").child(user.getUid()).setValue(token);
    }

    private void sendNotification(String clientId_token, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, clientId_token);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null && response.body().success != 1) {

                        Toast.makeText(ProviderDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Log.d("NOTIFERROR", t.getMessage());
            }
        });

    }

}