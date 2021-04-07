package com.sortscript.findnhire.Worker.WorkerActivities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

import dmax.dialog.SpotsDialog;

public class WorkerAvailabilityActivity extends AppCompatActivity {

    private static final String TAG = "FragmentAvailability";
    TextInputEditText mondayTimeEt, tuesdayTimeEt, wednesdayTimeEt, thursdayTimeEt, fridayTimeEt,
            saturdayTimeEt, sundayTimeEt;
    String totalTime = "";
    Button availabilityBtn;
    DatabaseRefs refs;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_availability);

        getSupportActionBar().setTitle("Adjust Timings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refs = new DatabaseRefs();
        mondayTimeEt = findViewById(R.id.mondayTimeEtId);
        tuesdayTimeEt = findViewById(R.id.tuesdayTimeEtId);
        wednesdayTimeEt = findViewById(R.id.wednesdayTimeEtId);
        thursdayTimeEt = findViewById(R.id.thursdayTimeEtId);
        fridayTimeEt = findViewById(R.id.fridayTimeEtId);
        saturdayTimeEt = findViewById(R.id.saturdayTimeEtId);
        sundayTimeEt = findViewById(R.id.sundayTimeEtId);
        availabilityBtn = findViewById(R.id.availabilityBtnId);

        availabilityBtn.setOnClickListener(v -> {

            progressDialog = new SpotsDialog(WorkerAvailabilityActivity.this, R.style.CustomPleaseWait);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            String monday = mondayTimeEt.getText().toString();
            String tuesday = tuesdayTimeEt.getText().toString();
            String wednesday = wednesdayTimeEt.getText().toString();
            String thursday = thursdayTimeEt.getText().toString();
            String friday = fridayTimeEt.getText().toString();
            String saturday = saturdayTimeEt.getText().toString();
            String sunday = sundayTimeEt.getText().toString();

            if (!monday.isEmpty()) {
                totalTime += "Monday : " + monday + "\n";
            }
            if (!tuesday.isEmpty()) {
                totalTime += "Tuesday : " + tuesday + "\n";
            }
            if (!wednesday.isEmpty()) {
                totalTime += "Wednesday : " + wednesday + "\n";
            }
            if (!thursday.isEmpty()) {
                totalTime += "Thursday : " + thursday + "\n";
            }
            if (!friday.isEmpty()) {
                totalTime += "Friday : " + friday + "\n";
            }
            if (!saturday.isEmpty()) {
                totalTime += "Saturday : " + saturday + "\n";
            }
            if (!sunday.isEmpty()) {
                totalTime += "Sunday : " + sunday + "\n";
            }

            refs.referenceWorkersUIDTimings
                    .child("workerAvailability")
                    .setValue(totalTime)
                    .addOnSuccessListener(aVoid -> {
                        new Handler().postDelayed(() -> {
                            Toast.makeText(WorkerAvailabilityActivity.this, "Timings Saved Successfully!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onCreateView: Success" + totalTime);
                            progressDialog.dismiss();
                            finish();
                        }, 2000);
                    })
                    .addOnFailureListener(e -> {
                        new Handler().postDelayed(() -> {
                            Toast.makeText(WorkerAvailabilityActivity.this, "Fail To Save Timings!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onCreateView: Failure" + totalTime);
                            progressDialog.dismiss();
                            finish();
                        }, 2000);
                    });

        });
    }
}