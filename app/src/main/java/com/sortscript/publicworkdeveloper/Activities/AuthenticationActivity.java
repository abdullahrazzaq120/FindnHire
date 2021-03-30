package com.sortscript.publicworkdeveloper.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.sortscript.publicworkdeveloper.R;
import com.sortscript.publicworkdeveloper.Worker.WorkerActivities.WorkerLoginActivity;
import com.sortscript.publicworkdeveloper.Worker.WorkerActivities.WorkerMenu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationActivity extends AppCompatActivity {

    Button facebookLoginBtn, googleLoginBtn, workerGoToLoginBtn;
    ProgressDialog loader;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 101;
    CountryCodePicker countryCodePicker;
    TextInputEditText editText;
    Button next;

    //fb
    FirebaseAuth.AuthStateListener authStateListener;

    //fb
    private CallbackManager mCallbackManager;

    //fb
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authentication);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
        loader = new ProgressDialog(this);
        facebookLoginBtn = findViewById(R.id.facebookLoginBtnId);
        googleLoginBtn = findViewById(R.id.googleLoginBtnId);
        countryCodePicker = findViewById(R.id.countryCodeId);
        editText = findViewById(R.id.phoneId);
        next = findViewById(R.id.loginPhone);
        workerGoToLoginBtn = findViewById(R.id.workerGoToLoginBtnId);

        workerGoToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthenticationActivity.this, WorkerLoginActivity.class));
            }
        });

        next.setOnClickListener(v -> {

            try {

                String phone = editText.getText().toString().trim();

                if (phone.isEmpty()) {
                    editText.setError("Please Enter Phone Number");
                } else if (phone.length() < 10) {
                    editText.setError("Invalid Phone Number");
                } else {
                    Log.e("sjdh", phone);
                    Log.e("sjdhsdqw", countryCodePicker.getFullNumber() + phone);

                    String number = countryCodePicker.getFullNumber() + phone;
                    Intent i = new Intent(AuthenticationActivity.this, VerifyNumberActivity.class);
                    i.putExtra("Number", "+" + number);
                    startActivity(i);
                }
            } catch (Exception ignored) {
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //fb
        mCallbackManager = CallbackManager.Factory.create();

        //fb
        facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(AuthenticationActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(AuthenticationActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("erorrrrrr", error.toString());
                        if (error instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                    }
                });
            }
        });

        //fb
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                if (mUser != null) {
                    updateFacebookUI();
                }
            }
        };

        //fb
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mAuth.signOut();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (user.getProviderId().equals("password")) {
                    Intent i = new Intent(getApplicationContext(), WorkerMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), UserMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        } catch (Exception ignored) {
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {

        try {

            loader.setTitle("Logging In with Google...");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                final FirebaseUser user = mAuth.getCurrentUser();

                                new Handler().postDelayed(() -> {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("UserName", user.getDisplayName());
                                    map.put("UserEmail", user.getEmail());
                                    map.put("UserImage", user.getPhotoUrl().toString());
                                    map.put("UserPhone", "");
                                    map.put("UserCity", "");
                                    map.put("UserAddress", "");
                                    map.put("deviceToken", "");

                                    reference.child(mAuth.getCurrentUser().getUid()).setValue(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task1) {
                                                    if (task1.isSuccessful()) {
                                                        loader.dismiss();
                                                        updateUI(user);
                                                    } else {
                                                        loader.dismiss();
                                                        Toast.makeText(AuthenticationActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }, 2000);
                            } else {
                                // If sign in fails, display a message to the user.
                                loader.dismiss();
                                Toast.makeText(AuthenticationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                Log.e("googleee", task.getException().toString());
                                updateUI(null);
                            }
                        }
                    });
        } catch (Exception e) {
            loader.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(AuthenticationActivity.this, UserMenu.class));
        Toast.makeText(AuthenticationActivity.this, "User LogIn Successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //fb
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("firebaseAuthWithGoogle:", account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException ignored) {
            }
        }
    }

    //fb
    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    //fb
    private void handleFacebookAccessToken(AccessToken token) {

        try {

            loader.setTitle("Logging In with Facebook...");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            Log.e("handleFbAccessToken", token.toString());

            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final FirebaseUser userFb = mAuth.getCurrentUser();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("UserName", userFb.getDisplayName());
                                        map.put("UserEmail", userFb.getEmail());
                                        map.put("UserImage", userFb.getPhotoUrl().toString());
                                        map.put("UserPhone", "");
                                        map.put("UserCity", "");
                                        map.put("UserAddress", "");
                                        map.put("deviceToken", "");

                                        reference.child(mAuth.getCurrentUser().getUid()).setValue(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            loader.dismiss();
                                                            updateFacebookUI();
                                                        } else {
                                                            loader.dismiss();
                                                            Toast.makeText(AuthenticationActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }, 2000);
                            } else {

                                loader.dismiss();
                                Toast.makeText(AuthenticationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                Log.e("errorHere", task.getException().toString());

                            }
                        }
                    });
        } catch (Exception e) {
            loader.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //fb
    private void updateFacebookUI() {
        startActivity(new Intent(getApplicationContext(), UserMenu.class));
        Toast.makeText(AuthenticationActivity.this, "User LogIn Successfully", Toast.LENGTH_LONG).show();
        finish();
    }
}