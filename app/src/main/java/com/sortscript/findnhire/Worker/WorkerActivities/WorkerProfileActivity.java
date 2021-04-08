package com.sortscript.findnhire.Worker.WorkerActivities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.Classes.RetrieveWorkerDetail;
import com.sortscript.findnhire.Interfaces.SimpleCallback;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.Worker.WorkerModels.ModelSetWorker;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class WorkerProfileActivity extends AppCompatActivity {

    private static final String TAG = "WorkerProfileActivity";
    String category;
    ImageView workerImageProfileIv;
    Button workerSelectImageProfileBtn, saveProfileBtn;
    TextInputEditText workerNameProfileEt, workerSpecificationProfileEt;
    Spinner workerCategoryProfileSpinner;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private SpotsDialog progressDialog;
    DatabaseRefs refs;
    FirebaseAuth mAuth;
    RetrieveWorkerDetail retrieveWorkerDetail;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        getSupportActionBar().setTitle("Set Profile");

        setIds();
        mAuth = FirebaseAuth.getInstance();
        number = mAuth.getCurrentUser().getPhoneNumber();
        refs = new DatabaseRefs();

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(WorkerProfileActivity.this,
                R.array.categories, R.layout.spinner_items);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_items);
        workerCategoryProfileSpinner.setAdapter(arrayAdapter);
        workerCategoryProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        retrieveWorkerDetail = new RetrieveWorkerDetail(WorkerProfileActivity.this);

        progressDialog = new SpotsDialog(WorkerProfileActivity.this, R.style.CustomPleaseWait);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        retrieveWorkerDetail.setUtils(new SimpleCallback<String>() {
            @Override
            public void callback(String img, String name, String profession, String contact, String specs) {

                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(WorkerProfileActivity.this,
                        R.array.categories, R.layout.spinner_items);
                arrayAdapter.setDropDownViewResource(R.layout.spinner_items);
                workerCategoryProfileSpinner.setAdapter(arrayAdapter);
                workerCategoryProfileSpinner.setSelection(arrayAdapter.getPosition(profession));
                workerCategoryProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                Glide.with(getApplicationContext()).load(img).into(workerImageProfileIv);
                workerNameProfileEt.setText(name);
                workerSpecificationProfileEt.setText(specs);
            }
        });

        workerSelectImageProfileBtn.setOnClickListener(v -> {
            chooseImage();
        });

        saveProfileBtn.setOnClickListener(v -> {
            String name = workerNameProfileEt.getText().toString();
            String specs = workerSpecificationProfileEt.getText().toString();
            savingProfile(name, specs);
        });

    }

    private void savingProfile(String name, String specs) {
        progressDialog.show();

        refs.referenceUIDWorkers
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            try {
                                if (isValid(name, specs)) {
                                    StorageReference fileReference = refs.mStorageRefImg.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(mImageUri));
                                    fileReference.putFile(mImageUri)
                                            .addOnSuccessListener(taskSnapshot -> {
                                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                                task.addOnSuccessListener(uri -> {
                                                    String imageLink = uri.toString();

                                                    ModelSetWorker modelSetWorker = new ModelSetWorker(name, imageLink, category, number, specs);
                                                    refs.referenceUIDWorkers.setValue(modelSetWorker)
                                                            .addOnSuccessListener(aVoid -> {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(WorkerProfileActivity.this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.d(TAG, "onDataChange: Failure !snapshot " + e.getMessage());
                                                                progressDialog.dismiss();
                                                                Toast.makeText(WorkerProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            });
                                                });
                                            });
                                }
                            } catch (Exception ignored) {
                            }
                        } else {
                            if (mImageUri == null) {
                                try {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("workerName", name);
                                    map.put("workerProfession", category);
                                    map.put("workerSpecification", specs);
                                    refs.referenceUIDWorkers.updateChildren(map)
                                            .addOnSuccessListener(aVoid -> {
                                                progressDialog.dismiss();
                                                Toast.makeText(WorkerProfileActivity.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                Log.d(TAG, "onFailure: snapshot " + e.getMessage());
                                                Toast.makeText(WorkerProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                } catch (Exception ignored) {
                                }
                            } else {
                                try {
                                    StorageReference fileReference = refs.mStorageRefImg.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(mImageUri));
                                    fileReference.putFile(mImageUri)
                                            .addOnSuccessListener(taskSnapshot -> {
                                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                                task.addOnSuccessListener(uri -> {
                                                    String imageLink = uri.toString();

                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("workerImage", imageLink);
                                                    map.put("workerName", name);
                                                    map.put("workerProfession", category);
                                                    map.put("workerSpecification", specs);
                                                    refs.referenceUIDWorkers.updateChildren(map)
                                                            .addOnSuccessListener(aVoid -> {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(WorkerProfileActivity.this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.d(TAG, "onDataChange: snapshot with uri " + e.getMessage());
                                                                progressDialog.dismiss();
                                                                Toast.makeText(WorkerProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            });
                                                });
                                            });
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private boolean isValid(String name, String specs) {
        if (mImageUri == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Select a profile picture", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.isEmpty()) {
            progressDialog.dismiss();
            workerNameProfileEt.setError("Name Required!");
            return false;
        } else if (specs.isEmpty()) {
            progressDialog.dismiss();
            workerSpecificationProfileEt.setError("Details Required!");
            return false;
        } else {
            return true;
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
            Glide.with(this).load(mImageUri).into(workerImageProfileIv);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void setIds() {
        workerCategoryProfileSpinner = findViewById(R.id.workerCategoryProfileSpinnerId);
        workerImageProfileIv = findViewById(R.id.workerImageProfileIvId);
        workerSelectImageProfileBtn = findViewById(R.id.workerSelectImageProfileBtnId);
        saveProfileBtn = findViewById(R.id.saveProfileBtnId);
        workerNameProfileEt = findViewById(R.id.workerNameProfileEtId);
        workerSpecificationProfileEt = findViewById(R.id.workerSpecificationProfileEtId);
    }
}