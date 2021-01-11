package com.sortscript.publicworkdeveloper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostForWorker extends AppCompatActivity {

    TextInputEditText et1, et2;
    VideoView vv;
    ImageView uploadVideoBtn;
    Button btnSend;
    String node;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    String user;
    private static final int PICK_VIDEO_REQUEST = 1;
    private Uri videoUri;
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

        node = getIntent().getStringExtra("Node");
        getSupportActionBar().setTitle(node);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaController = new MediaController(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
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


                chooseVideo();
            }
        });

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

    private void openDownloadDialog() {

    }


}