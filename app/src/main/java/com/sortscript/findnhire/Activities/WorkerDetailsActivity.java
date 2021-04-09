package com.sortscript.findnhire.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Activities.HolderActivities.HolderComments;
import com.sortscript.findnhire.Activities.ModelsActivities.ModelComments;
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
            workerDetailsSpecificationTv, noCommentsYetTv;
    TextInputEditText workerCommentsDetailEt;
    NestedScrollView workerDetailsScrollViewSv;
    CircleButton workerDetailSendBtn;
    SetAnimatedScroll setAnimatedScroll;
    SpotsDialog progressDialog;
    FirebaseRecyclerOptions<ModelComments> options;
    FirebaseRecyclerAdapter<ModelComments, HolderComments> adapter;

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

                                    if (!comment.isEmpty()) {
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
                                    }
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
        noCommentsYetTv = findViewById(R.id.noCommentsYetTvId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<ModelComments>().setQuery(refs
                .referenceWorkers
                .child(workerKEY)
                .child("Reviews"), ModelComments.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelComments, HolderComments>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                noCommentsYetTv.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull HolderComments holder, int position, @NonNull ModelComments model) {
                holder.commentsTextUserTv.setText(model.getComment());
                refs.referenceUsers
                        .child(model.getCommenterUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("userName").getValue().toString();
                                String image = snapshot.child("userImage").getValue().toString();

                                Glide.with(getApplicationContext()).load(image).into(holder.commenterUserImageIv);
                                holder.commenterUserNameTv.setText(name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }

            @NonNull
            @Override
            public HolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comments_designs, parent, false);
                return new HolderComments(view);
            }
        };

        workerDetailsRv.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        workerDetailsRv.setLayoutManager(linearLayoutManager);
        workerDetailsRv.setHasFixedSize(true);
    }
}