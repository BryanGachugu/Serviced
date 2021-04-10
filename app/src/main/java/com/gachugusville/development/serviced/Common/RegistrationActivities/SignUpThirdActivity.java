package com.gachugusville.development.serviced.Common.RegistrationActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.Main.MainActivity;
import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.User.DashboardActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpThirdActivity extends AppCompatActivity {
    MaterialButton btn_done;
    TextView txt_email_error;
    ImageView retailer_signUp_back_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;
    EditText edt_email;
    String email_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_third);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        //Hooks
        edt_email = findViewById(R.id.edt_email);
        retailer_signUp_back_btn = findViewById(R.id.retailer_signUp_back_btn);
        txt_email_error = findViewById(R.id.txt_email_error);
        email_error = "Invalid Email";
        //hooks
        btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(v -> {
            if (isEmailValid(edt_email.getText().toString())) {
                addUserToDatabase();
            }
            //else notify the user that the email is invalid
            else txt_email_error.setText(email_error);
        });

        checkLocationPermission();

        retailer_signUp_back_btn.setOnClickListener(v -> SignUpThirdActivity.super.onBackPressed());
    }

    private void addUserToDatabase() {
        User.getInstance().setFirst_name(getIntent().getStringExtra("first_name"));
        User.getInstance().setLast_name(getIntent().getStringExtra("last_name"));
        User.getInstance().setPhone_number(getIntent().getStringExtra("phone_number"));
        User.getInstance().setProfile_picture_url(null);
        User.getInstance().setEmail(edt_email.getText().toString());
        User.getInstance().setCountry("Kenya");
        User.getInstance().setReviews(null);

        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(User.getInstance().getClass())
                .addOnSuccessListener(aVoid -> startActivity(new Intent(getApplicationContext(), DashboardActivity.class)))
                .addOnFailureListener(e -> Toast.makeText(SignUpThirdActivity.this, "An error occurred", Toast.LENGTH_SHORT).show());
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        //regex to validate input email address
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(SignUpThirdActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // location-related task you need to do.
                getLocation();
            } else {

                User.getInstance().setCountry("United States");
               User.getInstance().setLongitude(0);
               User.getInstance().setLatitude(0);

            }
        }
    }

    private void getLocation() {
        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                MainActivity.user.setLongitude(addresses.get(0).getLongitude());
                MainActivity.user.setLatitude(addresses.get(0).getLatitude());
                MainActivity.user.setCountry(addresses.get(0).getCountryName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Could not get your location", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

}