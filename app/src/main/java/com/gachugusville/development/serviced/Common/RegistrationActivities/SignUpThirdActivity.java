package com.gachugusville.development.serviced.Common.RegistrationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.User.DashboardActivity;
import com.gachugusville.development.serviced.Common.User;
import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpThirdActivity extends AppCompatActivity {
    MaterialButton btn_done;
    TextView txt_email_error;
    ImageView retailer_signUp_back_btn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText edt_email;
    String email_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_third);

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

        retailer_signUp_back_btn.setOnClickListener(v -> SignUpThirdActivity.super.onBackPressed());
    }

    private void addUserToDatabase() {
        User newUser = new User(getIntent().getStringExtra("first_name"), //first name from previous activity
                getIntent().getStringExtra("last_name"), null, // last name and photo URI
                getIntent().getStringExtra("phone_number"), edt_email.getText().toString(), 0, null); // Phone number, rating and reviews
        db.collection("Users").add(newUser);
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
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
}