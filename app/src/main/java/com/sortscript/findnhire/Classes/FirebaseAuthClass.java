package com.sortscript.findnhire.Classes;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortscript.findnhire.Activities.ModelsActivities.ModelSetUsers;
import com.sortscript.findnhire.Activities.UserMenu;
import com.sortscript.findnhire.Interfaces.FirebaseAuthInterface;

import dmax.dialog.SpotsDialog;

public class FirebaseAuthClass implements FirebaseAuthInterface {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
    Context context;
    SpotsDialog progressDialog;

    public FirebaseAuthClass(Context context, SpotsDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    public void signIn(String emailSignIn, String passwordSignIn) {

        mAuth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "User Sign In Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, UserMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        progressDialog.dismiss();
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
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "User Sign Up Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, UserMenu.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Unable to Save your Data!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Unable to Register the User!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
