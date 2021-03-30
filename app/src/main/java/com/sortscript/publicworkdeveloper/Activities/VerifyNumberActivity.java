package com.sortscript.publicworkdeveloper.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortscript.publicworkdeveloper.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VerifyNumberActivity extends AppCompatActivity {

    TextView otp_error;
    EditText userCode;
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

        Toast.makeText(this, "Please wait while we send OTP to your number!", Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");

        getSupportActionBar().hide();

        userCode = findViewById(R.id.editcode);
        resendCode = findViewById(R.id.resendCode);
        verifyCode = findViewById(R.id.btncode);
        progressBar = findViewById(R.id.progressId);
        otp_error = findViewById(R.id.otp_incorrect);

        progressBar.setVisibility(View.GONE);

        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        phoneNo = getIntent().getStringExtra("Number");
        VerificationCode(phoneNo);

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificationCode(phoneNo);
                Toast.makeText(VerifyNumberActivity.this, "New OTP will be sent to your number", Toast.LENGTH_SHORT).show();
            }
        });

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = userCode.getText().toString().trim();

                if (code.length() < 4 || code.length() > 6) {
                    otp_error.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    otp_error.setVisibility(View.INVISIBLE);
                    verifyCode(code);
                }
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
        }
    };

    private void verifyCode(String codeByUser) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
            signInByCredentials(credential);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void signInByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyNumberActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("UserImage", "");
                                    map.put("UserPhone", mAuth.getCurrentUser().getPhoneNumber());
                                    map.put("UserEmail", "");
                                    map.put("UserCity", "");
                                    map.put("UserAddress", "");
                                    map.put("UserName", "");
                                    map.put("deviceToken", "");

                                    reference.child(mAuth.getCurrentUser().getUid()).setValue(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(VerifyNumberActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(VerifyNumberActivity.this, UserMenu.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(VerifyNumberActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            }, 2000);

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            resendCode.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resendCode.setVisibility(View.VISIBLE);
            }
        }, 60000);
    }
}