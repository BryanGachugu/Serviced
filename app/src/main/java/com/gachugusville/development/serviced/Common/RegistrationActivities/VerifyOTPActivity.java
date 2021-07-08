package com.gachugusville.development.serviced.Common.RegistrationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.gachugusville.development.serviced.Main.MainActivity;
import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    ImageView btn_exit;
    MaterialButton btn_verify_code, resendCode;
    TextView phone_to_be_verified, textError, timer;
    PinView pin_view;
    String phone_number_string;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String codeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setEnterTransition(null);
        setContentView(R.layout.activity_verify_o_t_p);

        //hook
        btn_verify_code = findViewById(R.id.btn_verify_code);
        resendCode = findViewById(R.id.resendCode);
        phone_to_be_verified = findViewById(R.id.phone_to_be_verified);
        pin_view = findViewById(R.id.pin_view);
        textError = findViewById(R.id.textError);
        btn_exit = findViewById(R.id.btn_exit);

        btn_exit.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));


        sendVerificationCodeToUser();

        //receive phone number from previous activity
        //TODO remove this line and the buttons
        btn_verify_code.setOnClickListener(v -> verifyCode(pin_view.getText().toString()));

        Intent intent = getIntent();
        phone_number_string = intent.getStringExtra("phone_number");
        phone_to_be_verified.setText(phone_number_string);
        phone_to_be_verified.setTextColor(getResources().getColor(R.color.light_blue));
        phone_to_be_verified.setOnClickListener(v -> VerifyOTPActivity.super.onBackPressed());

        //Disable resend code button for 20 seconds
    }

    private void sendVerificationCodeToUser() {
        phone_number_string = phone_to_be_verified.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone_number_string)     // Phone number to verify
                        .setTimeout(20L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeString = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pin_view.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    textError.setText(e.getMessage());
                    resendCode.setEnabled(false);
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeString, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        nextActivity();
                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            String failed = "Verification failed";
                            textError.setText(failed);
                        }
                    }
                });
    }

    public void nextActivity() {
        String code = Objects.requireNonNull(pin_view.getText()).toString();
        if (!code.isEmpty()) {
            verifyCode(code);
            Intent intent = new Intent(getApplicationContext(), SignUpSecondActivity.class);
            intent.putExtra("phone_number", phone_to_be_verified.getText());
            startActivity(intent);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        resendCode.setVisibility(View.INVISIBLE);
        timer = findViewById(R.id.timer);

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int remainingTime = (int) millisUntilFinished / 1000;
                timer.setText(String.valueOf(remainingTime));
            }

            @Override
            public void onFinish() {
                resendCode.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}