package com.gachugusville.development.serviced.Common.RegistrationActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;

public class SignUpSecondActivity extends AppCompatActivity {

    EditText edt_first_name, edt_last_name;
    TextView txt_first_name_error, txt_last_name_error;
    MaterialButton btn_nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second);

        //Hooks
        edt_first_name = findViewById(R.id.edt_first_name);
        edt_last_name = findViewById(R.id.edt_last_name);
        txt_first_name_error = findViewById(R.id.first_name_error);
        txt_last_name_error = findViewById(R.id.last_name_error);
        btn_nextActivity = findViewById(R.id.btn_nextActivity);

        String error = "Field cannot be empty";

        btn_nextActivity.setOnClickListener(v -> {
            if (edt_first_name
                    .getText().toString()
                    .trim()
                    .equals("")) txt_first_name_error.setText(error);

            else if (edt_last_name
                    .getText()
                    .toString()
                    .trim()
                    .equals("")) txt_last_name_error.setText(error);
            else {
                Intent toEmail = new Intent(getApplicationContext(), SignUpThirdActivity.class);
                toEmail.putExtra("phone_number", getIntent().getStringExtra("phone_number"));
                toEmail.putExtra("first_name", edt_first_name.getText().toString());
                toEmail.putExtra("last_name", edt_last_name.getText().toString());
                startActivity(toEmail);
            }
        });


    }

}