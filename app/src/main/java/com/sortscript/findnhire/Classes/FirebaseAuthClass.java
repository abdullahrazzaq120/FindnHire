package com.sortscript.findnhire.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortscript.findnhire.Activities.ModelSetUsers;
import com.sortscript.findnhire.Activities.UserMenu;
import com.sortscript.findnhire.Interfaces.FirebaseAuthInterface;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import androidx.annotation.NonNull;

public class FirebaseAuthClass implements FirebaseAuthInterface {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
    Context context;
    ProgressDialog loader;

    public FirebaseAuthClass(Context context, ProgressDialog loader) {
        this.context = context;
        this.loader = loader;
    }

    @Override
    public void signIn(String emailSignIn, String passwordSignIn) {

        mAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loader.dismiss();
                        Toast.makeText(context, "User Sign In Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, UserMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        loader.dismiss();
                        Toast.makeText(context, "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void signUp(String uNameSignUp, String emailSignUp, String passwordSignUp, String addressSignUp,
                       String dobSignUp, String genderSignUp) {
        mAuth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ModelSetUsers modelSetUsers = new ModelSetUsers(uNameSignUp, emailSignUp, passwordSignUp, addressSignUp,
                                dobSignUp, genderSignUp, "", "");

                        ref.child(mAuth.getCurrentUser().getUid())
                                .setValue(modelSetUsers)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        loader.dismiss();
                                        Toast.makeText(context, "User Sign Up Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, UserMenu.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    } else {
                                        loader.dismiss();
                                        Toast.makeText(context, "Unable to Save your Data!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        loader.dismiss();
                        Toast.makeText(context, "Unable to Register the User!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}