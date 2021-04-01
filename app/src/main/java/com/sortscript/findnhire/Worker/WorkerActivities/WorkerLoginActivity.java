package com.sortscript.findnhire.Worker.WorkerActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;
import com.sortscript.findnhire.Activities.VerifyNumberActivity;
import com.sortscript.findnhire.R;

import androidx.appcompat.app.AppCompatActivity;

public class WorkerLoginActivity extends AppCompatActivity {

    TextInputEditText et1, editText;
    CountryCodePicker countryCodePicker;
    Button btn;
    private static final String TAG = "WorkerLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        getSupportActionBar().hide();

        et1 = findViewById(R.id.workerNameId);
        editText = findViewById(R.id.workerPhoneId);
        btn = findViewById(R.id.workerNextBtnId);
        countryCodePicker = findViewById(R.id.countryCodeId);

        btn.setOnClickListener(view -> {
            String a = et1.getText().toString();
            String b = editText.getText().toString();

            if (a.isEmpty()) {
                et1.setError("Name Required!");
            } else if (b.isEmpty()) {
                editText.setError("Phone Number Required!");
            } else {
                checkCredentials(a, b);
            }
        });
    }

    private void checkCredentials(String a, String b) {
        try {
            String number = countryCodePicker.getFullNumber() + b;

            Log.d(TAG, "phone number is: " + number + " " + b + " " + countryCodePicker.getFullNumber());
            Intent i = new Intent(WorkerLoginActivity.this, VerifyNumberActivity.class);
            i.putExtra("WorkerName", a);
            i.putExtra("Number", "+" + number);
            startActivity(i);
            overridePendingTransition(R.anim.fadeout, R.anim.fadein);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}