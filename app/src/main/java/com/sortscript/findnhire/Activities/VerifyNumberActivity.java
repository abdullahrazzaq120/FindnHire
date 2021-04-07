package com.sortscript.findnhire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.Worker.WorkerActivities.WorkerMenu;

import java.util.concurrent.TimeUnit;

public class VerifyNumberActivity extends AppCompatActivity {

    private static final String TAG = "VerifyNumber";
    TextView otp_error;
    EditText userCode1, userCode2, userCode3, userCode4, userCode5, userCode6;
    Button verifyCode, resendCode;
    ProgressBar progressBar;
    String verificationCodeBySystem;
    String phoneNo;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_number);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child("Workers");

        userCode1 = findViewById(R.id.editcode1);
        userCode2 = findViewById(R.id.editcode2);
        userCode3 = findViewById(R.id.editcode3);
        userCode4 = findViewById(R.id.editcode4);
        userCode5 = findViewById(R.id.editcode5);
        userCode6 = findViewById(R.id.editcode6);

        resendCode = findViewById(R.id.resendCode);
        verifyCode = findViewById(R.id.btncode);
        progressBar = findViewById(R.id.progressId);
        otp_error = findViewById(R.id.otp_incorrect);

        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        phoneNo = getIntent().getStringExtra("Number");

        VerificationCode(phoneNo);

        resendCode.setOnClickListener(v -> {
            VerificationCode(phoneNo);
            Toast.makeText(VerifyNumberActivity.this, "New OTP will be sent to your number", Toast.LENGTH_SHORT).show();
        });

        textListners();

        verifyCode.setOnClickListener(v -> {

            String u1 = userCode1.getText().toString();
            String u2 = userCode2.getText().toString();
            String u3 = userCode3.getText().toString();
            String u4 = userCode4.getText().toString();
            String u5 = userCode5.getText().toString();
            String u6 = userCode6.getText().toString();

            String code = u1 + u2 + u3 + u4 + u5 + u6;
            Log.d(TAG, "number is: " + code);

            if (code.length() == 0) {
                otp_error.setText("OTP can't be empty!");
                otp_error.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else if (code.length() != 6) {
                otp_error.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            } else {
                progressBar.setVisibility(View.VISIBLE);
                otp_error.setVisibility(View.INVISIBLE);
                verifyCode(code);
            }
        });
    }

    private void textListners() {

        userCode1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userCode1.getText().toString().length() == 1)     //size as per your requirement
                {
                    userCode2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {

            }

        });

        userCode2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userCode2.getText().toString().length() == 1)     //size as per your requirement
                {
                    userCode3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        userCode3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userCode3.getText().toString().length() == 1)     //size as per your requirement
                {
                    userCode4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        userCode4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userCode4.getText().toString().length() == 1)     //size as per your requirement
                {
                    userCode5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        userCode5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userCode5.getText().toString().length() == 1)     //size as per your requirement
                {
                    userCode6.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        userCode6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userCode5.getText().toString().length() == 1)     //size as per your requirement
                {
                    userCode6.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
    }

    private void VerificationCode(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onVerificationFailed: " + e.getMessage());
        }
    };

    private void verifyCode(String codeByUser) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
            signInByCredentials(credential);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "verifyCode: " + e.getMessage());
        }
    }

    private void signInByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyNumberActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(VerifyNumberActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(VerifyNumberActivity.this, WorkerMenu.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "signInByCredentials: " + task.getException().getMessage());
                        resendCode.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(() -> resendCode.setVisibility(View.VISIBLE), 60000);
    }
}