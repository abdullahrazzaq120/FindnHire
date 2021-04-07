package com.sortscript.findnhire.Worker.WorkerActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.sortscript.findnhire.Activities.VerifyNumberActivity;
import com.sortscript.findnhire.R;

public class WorkerLoginActivity extends AppCompatActivity {

    TextInputEditText editText;
    CountryCodePicker countryCodePicker;
    Button btn;
    private static final String TAG = "WorkerLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        getSupportActionBar().hide();

        editText = findViewById(R.id.workerPhoneId);
        btn = findViewById(R.id.workerNextBtnId);
        countryCodePicker = findViewById(R.id.countryCodeId);

        btn.setOnClickListener(view -> {
            String b = editText.getText().toString();

            if (b.isEmpty()) {
                editText.setError("Phone Number Required!");
            } else {
                checkCredentials(b);
            }
        });
    }

    private void checkCredentials(String b) {
        try {
            String number = countryCodePicker.getFullNumber() + b;

            Log.d(TAG, "phone number is: " + number + " " + b + " " + countryCodePicker.getFullNumber());
            Intent i = new Intent(WorkerLoginActivity.this, VerifyNumberActivity.class);
            i.putExtra("Number", "+" + number);
            startActivity(i);
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}