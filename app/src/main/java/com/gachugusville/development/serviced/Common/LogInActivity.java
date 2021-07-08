package com.gachugusville.development.serviced.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.Common.RegistrationActivities.SignUp;
import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;

public class LogInActivity extends AppCompatActivity {

    EditText mail_logIn, password_logIn;
    TextView txt_signUp;
    MaterialButton btn_facebookSignUp, btn_googleSignUp;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_log_in);

        mail_logIn = findViewById(R.id.mail_logIn_field);
        mail_logIn.setBackground(getResources().getDrawable(R.drawable.bg_edit_text_blue_border));

        password_logIn = findViewById(R.id.password_logIn);
        password_logIn.setBackground(getResources().getDrawable(R.drawable.bg_edit_text_blue_border));

        btn_facebookSignUp = findViewById(R.id.btn_facebookSignUp);

        txt_signUp = findViewById(R.id.txt_signUp);
        txt_signUp.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, SignUp.class)));
    }
}