package com.sortscript.publicworkdeveloper.Worker.WorkerActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sortscript.publicworkdeveloper.R;

public class WorkerLoginActivity extends AppCompatActivity {

    TextInputEditText et1, et2;
    Button btn;
    ProgressDialog loader;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        loader = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        et1 = findViewById(R.id.workerEmailId);
        et2 = findViewById(R.id.workerPasswordId);
        btn = findViewById(R.id.workerLoginBtnId);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = et1.getText().toString();
                String b = et2.getText().toString();

                if (a.isEmpty()) {
                    et1.setError("Email Required!");
                } else if (b.isEmpty()) {
                    et2.setError("Password Required!");
                } else {
                    checkCredentials(a, b);
                }
            }
        });
    }

    private void checkCredentials(String a, String b) {
        try {
            loader.setTitle("Please Wait While We Check Your Information...");
            loader.setCanceledOnTouchOutside(false);
            loader.setCancelable(false);
            loader.show();

            firebaseAuth.signInWithEmailAndPassword(a, b)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loader.dismiss();
                                Toast.makeText(WorkerLoginActivity.this, "Worker LogIn Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(WorkerLoginActivity.this, WorkerMenu.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                loader.dismiss();
                                Toast.makeText(WorkerLoginActivity.this, "Worker LogIn Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {
            loader.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}