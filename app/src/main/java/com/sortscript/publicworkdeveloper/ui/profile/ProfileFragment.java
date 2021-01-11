package com.sortscript.publicworkdeveloper.ui.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.publicworkdeveloper.R;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextInputLayout p1, p2, p3, p4, p5;
    TextInputEditText fprofileUName, fprofileEmail, fprofilePhone, fprofileCity, fprofileAddress;
    Button fprofileEditBtn, fprofileSaveBtn;
    CircularImageView fprofileImage;
    boolean status = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    StorageReference mStorageRef;
    ProgressDialog loader2;
    FirebaseAuth mAuth;
    String user;
    DatabaseReference mRef;
    private static final String USER_NAME = "UserName";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_PHONE = "UserPhone";
    private static final String USER_CITY = "UserCity";
    private static final String USER_ADDRESS = "UserAddress";
    private static final String USER_IMAGE = "UserImage";
    String checka;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Members").child("Users");
        loader2 = new ProgressDialog(getActivity(), R.style.CustomDialogTheme);

        getReferenceIds(root);

        fprofileImage.setOnClickListener(view -> {
            if (status) {
                chooseImage();
            }
        });

        fprofileEditBtn.setOnClickListener(view -> makeFieldsEnableTrue());

        fprofileSaveBtn.setOnClickListener(view -> {
            getUserDetails();
        });

        checkCredentialsForUser();

        return root;
    }

    private void getReferenceIds(View root) {
        fprofileImage = root.findViewById(R.id.fprofileImageId);

        p1 = root.findViewById(R.id.p1id);
        p2 = root.findViewById(R.id.p2id);
        p3 = root.findViewById(R.id.p3id);
        p4 = root.findViewById(R.id.p4id);
        p5 = root.findViewById(R.id.p5id);

        fprofileUName = root.findViewById(R.id.fprofileUNameId);
        fprofileEmail = root.findViewById(R.id.fprofileEmailId);
        fprofilePhone = root.findViewById(R.id.fprofilePhoneId);
        fprofileCity = root.findViewById(R.id.fprofileCityId);
        fprofileAddress = root.findViewById(R.id.fprofileAddressId);

        fprofileEditBtn = root.findViewById(R.id.fprofileEditBtnId);
        fprofileSaveBtn = root.findViewById(R.id.fprofileSaveBtnId);
    }

    private void getUserDetails() {
        String uName = fprofileUName.getText().toString();
        String uEmail = fprofileEmail.getText().toString();
        String uPhone = fprofilePhone.getText().toString();
        String uCity = fprofileCity.getText().toString();
        String uAddress = fprofileAddress.getText().toString();

        saveDataFunction(uName, uEmail, uPhone, uCity, uAddress);

    }

    private void checkCredentialsForUser() {

        try {

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            String a = dataSnapshot.getKey();
                            Log.e("datas", a);

                            if (a.contains(user)) {

                                Log.e("dasaklalm", a);

                                String nameO = dataSnapshot.child(USER_NAME).getValue(String.class);
                                String emailO = dataSnapshot.child(USER_EMAIL).getValue(String.class);
                                String phoneO = dataSnapshot.child(USER_PHONE).getValue(String.class);
                                String cityO = dataSnapshot.child(USER_CITY).getValue(String.class);
                                String addressO = dataSnapshot.child(USER_ADDRESS).getValue(String.class);
                                String imageO = dataSnapshot.child(USER_IMAGE).getValue(String.class);

                                fprofileUName.setText(nameO);
                                fprofileEmail.setText(emailO);
                                fprofilePhone.setText(phoneO);
                                fprofileCity.setText(cityO);
                                fprofileAddress.setText(addressO);
                                Glide.with(getActivity()).load(imageO).into(fprofileImage);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDataFunction(String uName, String uEmail, String uPhone, String uCity, String uAddress) {

        loader2.setTitle("Setting Profile...");
        loader2.setCanceledOnTouchOutside(false);
        loader2.show();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    checka = dataSnapshot.getKey();
                    Log.e("datasAgain", checka);

                    if (checka.contains(user)) {

                        try {
                            Log.e("secondtime", "2nd Time");
                            notTheFirstTimeFunction(uName, uEmail, uPhone, uCity, uAddress);
                        } catch (Exception e) {
                            Log.e("secondtimeerror", e.getMessage() + "2nd");
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            loader2.dismiss();
                        }
                    } else {
                        try {
                            Log.e("firsttime", "1st Time");
                            firstTimeFunction(uName, uEmail, uPhone, uCity, uAddress);
                        } catch (Exception e) {
                            Log.e("firsttimerror", e.getMessage() + "1st");
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            loader2.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void notTheFirstTimeFunction(String uName, String uEmail, String uPhone, String uCity, String uAddress) {

        if (mImageUri == null) {
            if (uName.isEmpty()) {
                loader2.dismiss();
                fprofileUName.setError("Username required!");
            } else if (uPhone.isEmpty()) {
                loader2.dismiss();
                fprofilePhone.setError("Phone Number required!");
            } else if (uCity.isEmpty()) {
                loader2.dismiss();
                fprofileCity.setError("City Name required!");
            } else if (uAddress.isEmpty()) {
                loader2.dismiss();
                fprofileAddress.setError("Address required!");
            } else {

                Map<String, Object> map = new HashMap<>();
                map.put(USER_NAME, uName);
                map.put(USER_EMAIL, uEmail);
                map.put(USER_PHONE, uPhone);
                map.put(USER_CITY, uCity);
                map.put(USER_ADDRESS, uAddress);

                mRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                try {
                                    loader2.dismiss();
                                    Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_SHORT).show();
                                    makeFieldsEnableFalse();
                                    Log.e("checkStatusForAl", "notthe1sttimewithouturi");
                                } catch (Exception ignored) {

                                }
                            } else {
                                loader2.dismiss();
                                Toast.makeText(getActivity(), task1.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            if (uName.isEmpty()) {
                loader2.dismiss();
                fprofileUName.setError("Username required!");
            } else if (uPhone.isEmpty()) {
                loader2.dismiss();
                fprofilePhone.setError("Phone Number required!");
            } else if (uCity.isEmpty()) {
                loader2.dismiss();
                fprofileCity.setError("City Name required!");
            } else if (uAddress.isEmpty()) {
                loader2.dismiss();
                fprofileAddress.setError("Address required!");
            } else {
                StorageReference fileReference = mStorageRef.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(mImageUri));
                fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(uri -> {
                        String imageLink = uri.toString();

                        Map<String, Object> map = new HashMap<>();
                        map.put(USER_IMAGE, imageLink);
                        map.put(USER_NAME, uName);
                        map.put(USER_EMAIL, uEmail);
                        map.put(USER_PHONE, uPhone);
                        map.put(USER_CITY, uCity);
                        map.put(USER_ADDRESS, uAddress);

                        mRef.child(mAuth.getCurrentUser().getUid()).updateChildren(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        try {
                                            loader2.dismiss();
                                            Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_SHORT).show();
                                            makeFieldsEnableFalse();
                                            Log.e("checkStatusForAl", "notthe1sttimewithuri");
                                        } catch (Exception ignored) {

                                        }
                                    } else {
                                        loader2.dismiss();
                                        Toast.makeText(getActivity(), task1.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    });
                });
            }
        }
    }

    private void firstTimeFunction(String uName, String uEmail, String uPhone, String uCity, String uAddress) {

        if (mImageUri == null) {
            loader2.dismiss();
            Toast.makeText(getActivity(), "Image", Toast.LENGTH_SHORT).show();
        } else if (uName.isEmpty()) {
            loader2.dismiss();
            fprofileUName.setError("Username required!");
        } else if (uPhone.isEmpty()) {
            loader2.dismiss();
            fprofilePhone.setError("Phone Number required!");
        } else if (uCity.isEmpty()) {
            loader2.dismiss();
            fprofileCity.setError("City Name required!");
        } else if (uAddress.isEmpty()) {
            loader2.dismiss();
            fprofileAddress.setError("Address required!");
        } else {
            StorageReference fileReference = mStorageRef.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(uri -> {
                    String imageLink = uri.toString();

                    Map<String, Object> map = new HashMap<>();
                    map.put(USER_IMAGE, imageLink);
                    map.put(USER_NAME, uName);
                    map.put(USER_EMAIL, uEmail);
                    map.put(USER_PHONE, uPhone);
                    map.put(USER_CITY, uCity);
                    map.put(USER_ADDRESS, uAddress);

                    mRef.child(mAuth.getCurrentUser().getUid()).setValue(map)
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    try {
                                        loader2.dismiss();
                                        Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_SHORT).show();
                                        makeFieldsEnableFalse();
                                        Log.e("checkStatusForAl", "1sttimewithuri");
                                    } catch (Exception ignored) {

                                    }
                                } else {
                                    loader2.dismiss();
                                    Toast.makeText(getActivity(), task1.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                });
            });
        }
    }

    private void makeFieldsEnableTrue() {
        status = true;
        p1.setEnabled(true);
        p2.setEnabled(true);
        p3.setEnabled(true);
        p4.setEnabled(true);
        p5.setEnabled(true);
        fprofileSaveBtn.setEnabled(true);
    }

    private void makeFieldsEnableFalse() {
        status = false;
        p1.setEnabled(false);
        p2.setEnabled(false);
        p3.setEnabled(false);
        p4.setEnabled(false);
        p5.setEnabled(false);
        fprofileSaveBtn.setEnabled(false);
    }

    private void chooseImage() {
        //choose the image from internal storage

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(fprofileImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}