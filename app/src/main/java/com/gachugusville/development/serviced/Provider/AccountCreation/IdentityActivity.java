package com.gachugusville.development.serviced.Provider.AccountCreation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gachugusville.development.serviced.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

public class IdentityActivity extends AppCompatActivity {

    ChipGroup chip_group;
    RelativeLayout brand_layout, name_layout;
    EditText edt_first_name, edt_last_name, edt_brand_name;
    TextView first_name_error, last_name_error, txt_brand_name_error;
    String errors = "Field cannot be empty";
    MaterialButton btn_nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        //Hooks
        chip_group = findViewById(R.id.chip_group);
        brand_layout = findViewById(R.id.brand_layout);
        name_layout = findViewById(R.id.name_layout);
        edt_first_name = findViewById(R.id.edt_first_name);
        edt_last_name = findViewById(R.id.edt_last_name);
        edt_brand_name = findViewById(R.id.edt_brand_name);
        first_name_error = findViewById(R.id.first_name_error);
        last_name_error = findViewById(R.id.last_name_error);
        txt_brand_name_error = findViewById(R.id.txt_brand_name_error);
        btn_nextActivity = findViewById(R.id.btn_nextActivity);

        //Set views according to which Chip was selected
        chip_group.setOnCheckedChangeListener((group, checkedId) -> {
            String chipError = "Identity not selected";
            if (checkedId == R.id.chip_Brand) {
                name_layout.setVisibility(View.GONE);
                brand_layout.setVisibility(View.VISIBLE);
                btn_nextActivity.setOnClickListener(v -> brandSetUp());
            } else if (checkedId == R.id.chip_individual) {
                name_layout.setVisibility(View.VISIBLE);
                brand_layout.setVisibility(View.GONE);
                btn_nextActivity.setOnClickListener(v -> IndividualSetUp());
            } else if (checkedId == 0) Toast.makeText(this, chipError, Toast.LENGTH_SHORT).show();
        });

    }

    private void brandSetUp() {
        if (edt_brand_name.getText().toString().trim().equals("")) setError(txt_brand_name_error);
        else passBrandValues();
    }

    private void setError(TextView target_text_view) {
        new TextView(this);
        // add textView on some Layout
        target_text_view.setText(errors);
        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                target_text_view.setText("");
            }
        }.start();
    }

    private void passBrandValues() {
        Intent intent = new Intent(this, ServiceSetUpActivity.class);
        edt_first_name.setText("");
        intent.putExtra("first_name", edt_first_name.getText().toString());
        intent.putExtra("brand_name", edt_brand_name.getText().toString());
        startActivity(intent);
    }

    private void IndividualSetUp() {
        if (edt_first_name.getText().toString().trim().equals("")) setError(first_name_error);
        else if (edt_last_name.getText().toString().trim().equals("")) setError(last_name_error);
        else passIndividualValues();
    }

    private void passIndividualValues() {
        Intent intent = new Intent(this, ServiceSetUpActivity.class);
        edt_brand_name.setText("");
        intent.putExtra("first_name", edt_first_name.getText().toString());
        intent.putExtra("last_name", edt_last_name.getText().toString());
        startActivity(intent);
    }

}
