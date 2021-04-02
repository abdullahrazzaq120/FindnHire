package com.sortscript.findnhire.ui.profile;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.findnhire.Activities.ModelSetUsers;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextInputLayout p1, p2, p3, p5;
    TextInputEditText fprofileUName, fprofileEmail, fprofilePhone, fprofileAddress;
    Button fprofileEditBtn, fprofileSaveBtn;
    CircularImageView fprofileImage;
    boolean status = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    StorageReference mStorageRef;
    ProgressDialog loader2;
    FirebaseAuth mAuth;
    ModelSetUsers modelSetUsers;
    private static final String TAG = "ProfileFragment";
    DatabaseRefs databaseRefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Members").child("Users");
        loader2 = new ProgressDialog(getActivity(), R.style.CustomDialogTheme);
        modelSetUsers = new ModelSetUsers();
        databaseRefs = new DatabaseRefs();

        getReferenceIds(root);

        fprofileImage.setOnClickListener(view -> {
            if (status) {
                chooseImage();
            }
        });

        fprofileEditBtn.setOnClickListener(view -> {
            setManageFields(true);
//            makeFieldsEnableTrue();
        });

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
        p5 = root.findViewById(R.id.p5id);

        fprofileUName = root.findViewById(R.id.fprofileUNameId);
        fprofileEmail = root.findViewById(R.id.fprofileEmailId);
        fprofilePhone = root.findViewById(R.id.fprofilePhoneId);
        fprofileAddress = root.findViewById(R.id.fprofileAddressId);

        fprofileEditBtn = root.findViewById(R.id.fprofileEditBtnId);
        fprofileSaveBtn = root.findViewById(R.id.fprofileSaveBtnId);
    }

    private void getUserDetails() {

        loader2.setTitle("Setting Profile...");
        loader2.setCanceledOnTouchOutside(false);
        loader2.show();

        String uName = fprofileUName.getText().toString();
        String uEmail = fprofileEmail.getText().toString();
        String uPhone = fprofilePhone.getText().toString();
        String uAddress = fprofileAddress.getText().toString();

        if (uName.isEmpty()) {
            loader2.dismiss();
            fprofileUName.setError("Username Required!");
        } else if (uEmail.isEmpty()) {
            loader2.dismiss();
            fprofileEmail.setError("Email Required!");
        } else if (uAddress.isEmpty()) {
            loader2.dismiss();
            fprofileAddress.setError("Address Required!");
        } else {
            saveDataFunction(uName, uEmail, uPhone, uAddress);
        }
    }

    private void checkCredentialsForUser() {

        try {

            databaseRefs.referenceUIDUsers
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                try {
                                    String nameO = dataSnapshot.child("userName").getValue(String.class);
                                    String emailO = dataSnapshot.child("userEmail").getValue(String.class);
                                    String phoneO = dataSnapshot.child("userPhone").getValue(String.class);
                                    String addressO = dataSnapshot.child("userAddress").getValue(String.class);
                                    String imageO = dataSnapshot.child("userImage").getValue(String.class);

                                    fprofileUName.setText(nameO);
                                    fprofileEmail.setText(emailO);
                                    fprofilePhone.setText(phoneO);
                                    fprofileAddress.setText(addressO);
                                    Glide.with(getActivity()).load(imageO).into(fprofileImage);
                                } catch (Exception ignored) {
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

    private void saveDataFunction(String uName, String uEmail, String uPhone, String uAddress) {

        notTheFirstTimeFunction(uName, uEmail, uPhone, uAddress);
    }

    private void notTheFirstTimeFunction(String uName, String uEmail, String uPhone, String uAddress) {

        try {
            if (mImageUri == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("userName", uName);
                map.put("userEmail", uEmail);
                map.put("userPhone", uPhone);
                map.put("userAddress", uAddress);

                databaseRefs.referenceUIDUsers
                        .updateChildren(map)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                try {
                                    loader2.dismiss();
                                    Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_SHORT).show();
                                    setManageFields(false);
                                    Log.e("checkStatusForAl", "notthe1sttimewithouturi");
                                } catch (Exception ignored) {

                                }
                            } else {
                                loader2.dismiss();
                                Toast.makeText(getActivity(), task1.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {

                StorageReference fileReference = mStorageRef.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(mImageUri));
                fileReference.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(uri -> {

                        String imageLink = uri.toString();

                        Map<String, Object> map = new HashMap<>();
                        map.put("userImage", imageLink);
                        map.put("userName", uName);
                        map.put("userEmail", uEmail);
                        map.put("userPhone", uPhone);
                        map.put("userAddress", uAddress);

                        databaseRefs.referenceUIDUsers
                                .updateChildren(map)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        try {
                                            loader2.dismiss();
                                            Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_SHORT).show();
                                            setManageFields(false);
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
        } catch (Exception e) {
            loader2.dismiss();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    private void makeFieldsEnableTrue() {
//        status = true;
//        p1.setEnabled(true);
//        p2.setEnabled(true);
//        p3.setEnabled(true);
//        p5.setEnabled(true);
//        fprofileSaveBtn.setEnabled(true);
//    }
//
//    private void makeFieldsEnableFalse() {
//        status = false;
//        p1.setEnabled(false);
//        p2.setEnabled(false);
//        p3.setEnabled(false);
//        p5.setEnabled(false);
//        fprofileSaveBtn.setEnabled(false);
//    }

    private void setManageFields(boolean b) {
        if (b) {
            status = true;
            p1.setEnabled(true);
            p2.setEnabled(true);
            p3.setEnabled(true);
            p5.setEnabled(true);
            fprofileSaveBtn.setEnabled(true);
            fprofileImage.setAlpha((float) 1.0);

        } if (!b){
            status = false;
            p1.setEnabled(false);
            p2.setEnabled(false);
            p3.setEnabled(false);
            p5.setEnabled(false);
            fprofileSaveBtn.setEnabled(false);
            fprofileImage.setAlpha((float) 0.5);
        }
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