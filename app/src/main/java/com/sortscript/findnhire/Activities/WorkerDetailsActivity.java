package com.sortscript.findnhire.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.Classes.SetAnimatedScroll;
import com.sortscript.findnhire.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import at.markushi.ui.CircleButton;
import dmax.dialog.SpotsDialog;

public class WorkerDetailsActivity extends AppCompatActivity {

    String workerKEY;
    FirebaseAuth mAUth;
    DatabaseRefs refs;
    RecyclerView workerDetailsRv;
    ImageView workerDetailsImageIv;
    TextView workerDetailsNameTv, workerDetailsProfessionTv, workerDetailsContactTv, workerDetailsAvailabilityTv,
            workerDetailsSpecificationTv;
    TextInputEditText workerCommentsDetailEt;
    NestedScrollView workerDetailsScrollViewSv;
    CircleButton workerDetailSendBtn;
    SetAnimatedScroll setAnimatedScroll;
    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        progressDialog = new SpotsDialog(WorkerDetailsActivity.this, R.style.CustomPleaseWait);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        getIds();
        mAUth = FirebaseAuth.getInstance();
        refs = new DatabaseRefs();
        workerKEY = getIntent().getStringExtra("workerKey");

        setAnimatedScroll = new SetAnimatedScroll(getApplicationContext(), workerDetailsScrollViewSv, workerDetailsImageIv);
        setAnimatedScroll.ListenScroll();

        refs.referenceWorkersTimings
                .child(workerKEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String time = Objects.requireNonNull(snapshot.child("workerAvailability").getValue()).toString();
                        workerDetailsAvailabilityTv.setText(time);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        refs.referenceWorkers
                .child(workerKEY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            try {
                                new Handler().postDelayed(() -> progressDialog.dismiss(), 2000);

                                String imgW = snapshot.child("workerImage").getValue().toString();
                                String nameW = snapshot.child("workerName").getValue().toString();
                                String professionW = snapshot.child("workerProfession").getValue().toString();
                                String contactW = snapshot.child("workerContact").getValue().toString();
                                String tokenW = snapshot.child("deviceToken").getValue().toString();
                                String specificationW = snapshot.child("workerSpecification").getValue().toString();

                                Glide.with(getApplicationContext()).load(imgW).into(workerDetailsImageIv);
                                workerDetailsNameTv.setText(nameW);
                                workerDetailsProfessionTv.setText(professionW);
                                workerDetailsContactTv.setText(contactW);
                                workerDetailsSpecificationTv.setText(specificationW);

                                workerDetailSendBtn.setOnClickListener(v -> {
                                    String comment = workerCommentsDetailEt.getText().toString();

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("comment", comment);
                                    map.put("commenterUid", mAUth.getCurrentUser().getUid());

                                    refs.referenceWorkers
                                            .child(workerKEY)
                                            .child("Reviews")
                                            .push()
                                            .setValue(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    workerCommentsDetailEt.setText("");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    workerCommentsDetailEt.setText("");
                                                    Toast.makeText(WorkerDetailsActivity.this, "Failed to comment", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                });

                            } catch (Exception ignored) {
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getIds() {
        workerDetailsScrollViewSv = findViewById(R.id.workerDetailsScrollViewSvId);
        workerDetailsImageIv = findViewById(R.id.workerDetailsImageIvId);
        workerDetailsRv = findViewById(R.id.workerDetailsRvId);
        workerDetailsNameTv = findViewById(R.id.workerDetailsNameTvId);
        workerDetailsProfessionTv = findViewById(R.id.workerDetailsProfessionTvId);
        workerDetailsContactTv = findViewById(R.id.workerDetailsContactTvId);
        workerDetailsAvailabilityTv = findViewById(R.id.workerDetailsAvailabilityTvId);
        workerDetailsSpecificationTv = findViewById(R.id.workerDetailsSpecificationTvId);
        workerCommentsDetailEt = findViewById(R.id.workerCommentsDetailEtId);
        workerDetailSendBtn = findViewById(R.id.workerDetailSendBtnId);
    }
}