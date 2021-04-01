package com.sortscript.findnhire.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sortscript.findnhire.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PostForWorker extends AppCompatActivity {

    TextInputEditText et1, et2;
    VideoView vv;
    ImageView uploadVideoBtn;
    Button btnSend;
    String node;
    FirebaseAuth mAuth;
    DatabaseReference mRef, mRef2;
    String user;
    ProgressDialog loader;
    private static final int PICK_VIDEO_REQUEST = 1;
    private static int CAM_VIDEO_REQUEST = 101;
    private Uri videoUri;
    StorageReference mStorageRef;
    MediaController mediaController;
    private static final String USER_NAME = "UserName";
    private static final String USER_EMAIL = "UserEmail";
    private static final String USER_PHONE = "UserPhone";
    private static final String USER_CITY = "UserCity";
    private static final String USER_ADDRESS = "UserAddress";
    private static final String USER_IMAGE = "UserImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_worker);

        loader = new ProgressDialog(this);
        node = getIntent().getStringExtra("Node");
        getSupportActionBar().setTitle(node);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaController = new MediaController(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
        mRef2 = FirebaseDatabase.getInstance().getReference().child("Posts").child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Posts").child("Users").child(user);
        et1 = findViewById(R.id.postForWorkerAddressEtId);
        et2 = findViewById(R.id.postForWorkerDescriptionEtId);
        vv = findViewById(R.id.postForWorkerVideoVvId);
        uploadVideoBtn = findViewById(R.id.uploadVideoBtnId);
        btnSend = findViewById(R.id.postForWorkerSendBtnId);

        vv.setMediaController(mediaController);
        mediaController.setAnchorView(vv);
        vv.start();

        mRef.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String address = snapshot.child(USER_ADDRESS).getValue().toString();
                    et1.setText(address);

                } catch (Exception ignored) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog;
                AlertDialog.Builder dialog = new AlertDialog.Builder(PostForWorker.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.select_camera_dialog, null);
                dialog.setView(v);
                alertDialog = dialog.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
                ImageButton make = v.findViewById(R.id.make_video_id);
                ImageButton select = v.findViewById(R.id.select_video_id);
                make.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        camVideo();
                        alertDialog.dismiss();

                    }
                });

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        chooseVideo();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRef.child(user).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {

                            loader.setTitle("Publishing Post...");
                            loader.setCanceledOnTouchOutside(false);
                            loader.setCancelable(false);
                            loader.show();

                            String address = et1.getText().toString();
                            String description = et2.getText().toString();

                            String name = snapshot.child(USER_NAME).getValue().toString();
                            String city = snapshot.child(USER_CITY).getValue().toString();
                            String image = snapshot.child(USER_IMAGE).getValue().toString();

                            if (name.isEmpty() || city.isEmpty()) {
                                loader.dismiss();
                                Toast.makeText(PostForWorker.this, "Enter required data in your profile ", Toast.LENGTH_SHORT).show();
                            } else if (address.isEmpty()) {
                                loader.dismiss();
                                et1.setError("Address Required!");
                            } else if (videoUri == null) {
                                loader.dismiss();
                                Toast.makeText(PostForWorker.this, "Video Required!", Toast.LENGTH_SHORT).show();
                            } else {


                                //todays date
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                String formattedDate = df.format(c);

                                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(videoUri));
                                fileReference.putFile(videoUri).addOnSuccessListener(taskSnapshot -> {
                                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    task.addOnSuccessListener(uri -> {

                                        String videoLink = uri.toString();

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("UserId", mAuth.getCurrentUser().getUid());
                                        map.put("UserVideo", videoLink);
                                        map.put("CurrentDate", formattedDate);
                                        map.put("WorkerCategory", node);
                                        map.put("UserDescription", description);
                                        map.put(USER_ADDRESS, address);
                                        map.put(USER_NAME, name);
                                        map.put(USER_IMAGE, image);
                                        map.put(USER_CITY, city);

                                        mRef2.push().setValue(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            loader.dismiss();
                                                            Toast.makeText(PostForWorker.this, "Post Publish Successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {
                                                            loader.dismiss();
                                                            Toast.makeText(PostForWorker.this, "Post Publish Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    });
                                });
                            }

                        } catch (Exception e) {
                            loader.dismiss();
                            Toast.makeText(PostForWorker.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void camVideo() {
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(videoIntent, CAM_VIDEO_REQUEST);
        }
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("android.intent.extra.durationLimit", 60);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            videoUri = data.getData();
            vv.setVideoURI(videoUri);
        }

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            videoUri = data.getData();
            vv.setVideoURI(videoUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}