package com.sortscript.publicworkdeveloper.ui.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sortscript.publicworkdeveloper.Activities.ChatRoomActivity;
import com.sortscript.publicworkdeveloper.R;

public class OpenOfferNotifyWorkerProfileActivity extends AppCompatActivity {

    DatabaseReference reference;
    String profileKey;
    TextView openWorkerProfileNameTv, openWorkerProfileAddressTv, openWorkerProfileCategoryTv, openWorkerProfileGenderTv,
            openWorkerProfileEmailTv, openWorkerProfileSpecificationTv;
    CircularImageView openWorkerProfileImageIv;
    Button openWorkerProfileChatBtn;
    private static final String WORKER_NAME = "WorkerName";
    private static final String WORKER_EMAIL = "WorkerEmail";
    private static final String WORKER_ADDRESS = "WorkerAddress";
    private static final String WORKER_IMAGE = "WorkerImage";
    private static final String WORKER_CATEGORY = "WorkerCategory";
    private static final String WORKER_GENDER = "WorkerGender";
    private static final String WORKER_SPECIFICATION = "WorkerSpecification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_offer_notify_worker_profile);

        openWorkerProfileChatBtn = findViewById(R.id.openWorkerProfileChatBtnId);
        openWorkerProfileNameTv = findViewById(R.id.openWorkerProfileNameTvId);
        openWorkerProfileAddressTv = findViewById(R.id.openWorkerProfileAddressTvId);
        openWorkerProfileCategoryTv = findViewById(R.id.openWorkerProfileCategoryTvId);
        openWorkerProfileGenderTv = findViewById(R.id.openWorkerProfileGenderTvId);
        openWorkerProfileEmailTv = findViewById(R.id.openWorkerProfileEmailTvId);
        openWorkerProfileSpecificationTv = findViewById(R.id.openWorkerProfileSpecificationTvId);
        openWorkerProfileImageIv = findViewById(R.id.openWorkerProfileImageIvId);

        profileKey = getIntent().getStringExtra("openWorkerProfileId");

        reference = FirebaseDatabase.getInstance().getReference().child("Members").child("Workers");

        reference.child(profileKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child(WORKER_NAME).getValue().toString();
                String address = snapshot.child(WORKER_ADDRESS).getValue().toString();
                String gender = snapshot.child(WORKER_GENDER).getValue().toString();
                String specification = snapshot.child(WORKER_SPECIFICATION).getValue().toString();
                String category = snapshot.child(WORKER_CATEGORY).getValue().toString();
                String email = snapshot.child(WORKER_EMAIL).getValue().toString();
                String image = snapshot.child(WORKER_IMAGE).getValue().toString();

                Glide.with(OpenOfferNotifyWorkerProfileActivity.this).load(image).into(openWorkerProfileImageIv);
                openWorkerProfileEmailTv.setText(email);
                openWorkerProfileCategoryTv.setText(category);
                openWorkerProfileSpecificationTv.setText(specification);
                openWorkerProfileGenderTv.setText(gender);
                openWorkerProfileAddressTv.setText(address);
                openWorkerProfileNameTv.setText(name);

                openWorkerProfileChatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                        intent.putExtra("chatterId", profileKey);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}