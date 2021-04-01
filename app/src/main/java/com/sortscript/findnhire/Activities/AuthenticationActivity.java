package com.sortscript.findnhire.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.sortscript.findnhire.Classes.FirebaseAuthClass;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.Worker.WorkerActivities.WorkerLoginActivity;
import com.sortscript.findnhire.Worker.WorkerActivities.WorkerMenu;

import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";
    TextView workerGoToLoginTv;
    TextView clickSignUpTv, clickSignInTv;
    EditText emailSignIn, passwordSignIn;
    Spinner userGenderSignUpSpinner;
    EditText userNameSignUpEt, userEmailSignUpEt, userPasswordSignUpEt, userCPasswordSignUpEt, userAddressSignUpEt,
            userDaySignUpEt, userMonthSignUpEt, userYearSignUpEt;
    CheckBox userCheckedCb;
    Button userSignUpBtn, userSignInBtn;
    String genderLink;
    FirebaseAuthClass firebaseAuthClass;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        getSupportActionBar().hide();

        loader = new ProgressDialog(AuthenticationActivity.this);
        workerGoToLoginTv = findViewById(R.id.workerGoToLoginBtnId);
        clickSignUpTv = findViewById(R.id.clickSignUpTvId);
        clickSignInTv = findViewById(R.id.clickSignInTvId);

        View vSignUp = findViewById(R.id.setLayoutSignUpId);
        callingReferencesSignUp(vSignUp);

        View vSignIn = findViewById(R.id.setLayoutSignInId);
        callingReferencesSignIn(vSignIn);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(AuthenticationActivity.this,
                R.array.genders, R.layout.spinner_items);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_items);
        userGenderSignUpSpinner.setAdapter(arrayAdapter);
        userGenderSignUpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderLink = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "genderInside: " + genderLink);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Log.d(TAG, "genderOutside: " + genderLink);

        clickSignUpTv.setOnClickListener(view -> {
            vSignUp.setVisibility(View.VISIBLE);
            vSignIn.setVisibility(View.GONE);
            clickSignUpTv.setBackgroundResource(R.drawable.border2);
            clickSignInTv.setBackground(getResources().getDrawable(R.color.darkblue));
        });

        clickSignInTv.setOnClickListener(view -> {
            vSignIn.setVisibility(View.VISIBLE);
            vSignUp.setVisibility(View.GONE);
            clickSignInTv.setBackgroundResource(R.drawable.border2);
            clickSignUpTv.setBackground(getResources().getDrawable(R.color.darkblue));
        });

        listenerDate();

        userSignInBtn.setOnClickListener(view -> {

            loader.setMessage("Signing In...");
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            String emailSI = emailSignIn.getText().toString().trim();
            String passwordSI = passwordSignIn.getText().toString().trim();
            firebaseAuthClass = new FirebaseAuthClass(getApplicationContext(), loader);
            if (emailSI.isEmpty()) {
                loader.dismiss();
                emailSignIn.setError("Email Required!");
            } else if (passwordSI.isEmpty()) {
                loader.dismiss();
                passwordSignIn.setError("Password Required!");
            } else {
                firebaseAuthClass.signIn(emailSI, passwordSI);
            }
        });

        userSignUpBtn.setOnClickListener(view -> {
            loader.setMessage("Signing Up...");
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            register(loader);

        });

        workerGoToLoginTv.setOnClickListener(view ->
                startActivity(new Intent(AuthenticationActivity.this, WorkerLoginActivity.class)));
    }

    private void listenerDate() {
        userDaySignUpEt.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (userDaySignUpEt.getText().toString().length() == 2)     //size as per your requirement
                {
                    userMonthSignUpEt.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        userMonthSignUpEt.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (userMonthSignUpEt.getText().toString().length() == 2)     //size as per your requirement
                {
                    userYearSignUpEt.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        userYearSignUpEt.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (userYearSignUpEt.getText().toString().length() == 4)     //size as per your requirement
                {
                    userYearSignUpEt.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
    }

    private void callingReferencesSignIn(View vSignIn) {
        emailSignIn = vSignIn.findViewById(R.id.userEmailSignInEtId);
        passwordSignIn = vSignIn.findViewById(R.id.userPasswordSignInEtId);
        userSignInBtn = vSignIn.findViewById(R.id.userSignInBtnId);
    }

    private void callingReferencesSignUp(View vSignUp) {
        userGenderSignUpSpinner = vSignUp.findViewById(R.id.userGenderSignUpSpinnerId);
        userNameSignUpEt = vSignUp.findViewById(R.id.userNameSignUpEtId);
        userEmailSignUpEt = vSignUp.findViewById(R.id.userEmailSignUpEtId);
        userPasswordSignUpEt = vSignUp.findViewById(R.id.userPasswordSignUpEtId);
        userCPasswordSignUpEt = vSignUp.findViewById(R.id.userCPasswordSignUpEtId);
        userAddressSignUpEt = vSignUp.findViewById(R.id.userAddressSignUpEtId);
        userCheckedCb = vSignUp.findViewById(R.id.userCheckedCbId);
        userDaySignUpEt = vSignUp.findViewById(R.id.userDaySignUpEtId);
        userMonthSignUpEt = vSignUp.findViewById(R.id.userMonthSignUpEtId);
        userYearSignUpEt = vSignUp.findViewById(R.id.userYearSignUpEtId);
        userSignUpBtn = vSignUp.findViewById(R.id.userSignUpBtnId);
    }

    public void register(ProgressDialog loader2) {

        String userNameSU = userNameSignUpEt.getText().toString();
        String emailSU = userEmailSignUpEt.getText().toString().trim();
        String passwordSU = userPasswordSignUpEt.getText().toString().trim();
        String cPasswordSU = userCPasswordSignUpEt.getText().toString().trim();
        String addressSU = userAddressSignUpEt.getText().toString();
        String dayOB = userDaySignUpEt.getText().toString();
        String monthOB = userMonthSignUpEt.getText().toString();
        String yearOB = userYearSignUpEt.getText().toString();

        if (userNameSU.isEmpty()) {
            loader2.dismiss();
            userNameSignUpEt.setError("Username Required!");
        } else if (emailSU.isEmpty()) {
            loader2.dismiss();
            userEmailSignUpEt.setError("Email Required!");
        } else if (passwordSU.isEmpty()) {
            loader2.dismiss();
            userPasswordSignUpEt.setError("Password Required!");
        } else if (cPasswordSU.isEmpty()) {
            loader2.dismiss();
            userCPasswordSignUpEt.setError("Confirm Password Required!");
        } else if (!passwordSU.equals(cPasswordSU)) {
            loader2.dismiss();
            userCPasswordSignUpEt.setError("Password don't match!");
        } else if (addressSU.isEmpty()) {
            loader2.dismiss();
            userAddressSignUpEt.setError("Address Required!");
        } else if (dayOB.isEmpty()) {
            loader2.dismiss();
            Toast.makeText(this, "Enter Correct Date!", Toast.LENGTH_SHORT).show();
        } else if (monthOB.isEmpty()) {
            loader2.dismiss();
            Toast.makeText(this, "Enter Correct Date!", Toast.LENGTH_SHORT).show();
        } else if (yearOB.isEmpty()) {
            loader2.dismiss();
            Toast.makeText(this, "Enter Correct Date!", Toast.LENGTH_SHORT).show();
        } else if (!userCheckedCb.isChecked()) {
            loader2.dismiss();
            Toast.makeText(this, "Check the Terms & Condition", Toast.LENGTH_SHORT).show();
        } else {
            String dob = dayOB + "-" + monthOB + "-" + yearOB;
            firebaseAuthClass = new FirebaseAuthClass(getApplicationContext(), loader);
            firebaseAuthClass.signUp(userNameSU, emailSU, passwordSU, addressSU, dob, genderLink);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (user.getProviderId().equals("password")) {
                    Intent i = new Intent(getApplicationContext(), UserMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), WorkerMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        } catch (Exception ignored) {
        }

    }
}