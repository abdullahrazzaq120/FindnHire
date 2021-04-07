package com.sortscript.findnhire.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePassword";
    Button changePasswordBtn;
    TextInputEditText oldPassEt, newPassEt;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseRefs databaseRefs;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");

        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseRefs = new DatabaseRefs();
        oldPassEt = findViewById(R.id.oldPasswordChangePasswordEtId);
        newPassEt = findViewById(R.id.newPasswordChangePasswordEtId);
        changePasswordBtn = findViewById(R.id.changePasswordBtnId);

        databaseRefs
                .referenceUIDUsers
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        stopLoaderDisplay();

                        if (snapshot.exists()) {
                            try {
                                String oldPasswordDB = snapshot.child("userPassword").getValue().toString();

                                changePasswordBtn.setOnClickListener(view -> {

                                    setLoaderDisplay();

                                    String oldPass = oldPassEt.getText().toString();
                                    String newPass = newPassEt.getText().toString();

                                    if (checkValidation(oldPass, newPass)) {
                                        if (!oldPass.equals(oldPasswordDB)) {
                                            stopLoaderDisplay();
                                            oldPassEt.setError("Incorrect Old Password!");
                                        } else {
                                            if (!new AuthenticationActivity().validPassword(newPass)) {
                                                stopLoaderDisplay();
                                                newPassEt.setError("1 uppercase, 1 special character and 1 lowercase required!");
                                            } else {
                                                updatePassword(newPass);
                                            }
                                        }
                                    }
                                });

                            } catch (Exception ignored) {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updatePassword(String newPass) {
        user.updatePassword(newPass)
                .addOnSuccessListener(task -> {
                    databaseRefs
                            .referenceUIDUsers
                            .child("userPassword")
                            .setValue(newPass)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    stopLoaderDisplay();
                                    Toast.makeText(ChangePasswordActivity.this, "Password Reset Successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    stopLoaderDisplay();
                                    Toast.makeText(ChangePasswordActivity.this, "1 " + task1.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "updatePassword: " + task1.toString());
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    stopLoaderDisplay();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "updatePassword: " + e.getMessage());
                });
    }

    private boolean checkValidation(String oldPass, String newPass) {
        if (oldPass.isEmpty()) {
            oldPassEt.setError("Enter Old Password!");
            stopLoaderDisplay();
            return false;
        } else if (newPass.isEmpty()) {
            newPassEt.setError("Enter New Password!");
            stopLoaderDisplay();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLoaderDisplay();
    }

    private void setLoaderDisplay() {
        loader.setMessage("Loading...");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
    }

    private void stopLoaderDisplay() {
        loader.dismiss();
    }

}