package com.gachugusville.development.serviced.Common.RegistrationActivities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.R;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;

public class SignUp extends AppCompatActivity {
    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    ImageView back_btn;
    EditText phone_signUp;
    MaterialButton btn_toSecondSignUp;
    TextView phone_signUp_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);
        setContentView(R.layout.activity_sign_up);

        //hooks
        phone_signUp = findViewById(R.id.phone_signUp);
        btn_toSecondSignUp = findViewById(R.id.btn_toSecondSignUp);
        phone_signUp_error = findViewById(R.id.phone_signUp_error);
        back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(v -> SignUp.super.onBackPressed());

        btn_toSecondSignUp.setOnClickListener(v -> nextActivity());

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            HintRequest hintRequest = new HintRequest.Builder()
                    .setPhoneNumberIdentifierSupported(true)
                    .build();


            PendingIntent intent = Credentials.getClient(getApplicationContext()).getHintPickerIntent(hintRequest);
            try {
                startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }, 500);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            //get the selected phone number
            phone_signUp.setText(credentials.getId());
            Intent nextActivity = new Intent(getApplicationContext(), VerifyOTPActivity.class);

            String phone_number = credentials.getId();
            nextActivity.putExtra("phone_number", phone_number);
            startActivity(nextActivity);
            //Do what ever you want to do with your selected phone number here

        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            // *** No phone numbers available ***
            Toast.makeText(this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }


    }

    public void nextActivity() {
        if (phone_signUp.getText().toString().isEmpty()) {
            String error = "Field cannot be empty";
            phone_signUp_error.setText(error);
        } else {
            CountryCodePicker country_picker = findViewById(R.id.country_picker);
            Intent nextActivity = new Intent(getApplicationContext(), VerifyOTPActivity.class);
            String _phone_entered = phone_signUp.getText().toString().trim();
            String phone_number = "+" + country_picker.getFullNumber() + _phone_entered;
            nextActivity.putExtra("phone_number", phone_number);
            startActivity(nextActivity);
        }
    }

}