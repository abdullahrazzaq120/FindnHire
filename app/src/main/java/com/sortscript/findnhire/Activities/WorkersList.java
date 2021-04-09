package com.sortscript.findnhire.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.sortscript.findnhire.Activities.HolderActivities.HolderWorkList;
import com.sortscript.findnhire.Activities.ModelsActivities.ModelWorkersList;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

import dmax.dialog.SpotsDialog;

public class WorkersList extends AppCompatActivity {

    String node;
    SpotsDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseRefs refs;
    FirebaseRecyclerOptions<ModelWorkersList> options;
    FirebaseRecyclerAdapter<ModelWorkersList, HolderWorkList> adapter;
    RecyclerView homeUserRv;
    TextView notWorkerYetTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_list);

        mAuth = FirebaseAuth.getInstance();
        refs = new DatabaseRefs();

        progressDialog = new SpotsDialog(WorkersList.this, R.style.Custom);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        notWorkerYetTv = findViewById(R.id.notWorkerYetTvId);
        homeUserRv = findViewById(R.id.homeUserRvId);
        node = getIntent().getStringExtra("Node");

        getSupportActionBar().setTitle(node);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        options = new FirebaseRecyclerOptions.Builder<ModelWorkersList>()
                .setQuery(refs.referenceWorkers
                        .orderByChild("workerProfession")
                        .equalTo(node), ModelWorkersList.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelWorkersList, HolderWorkList>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressDialog.dismiss();
            }

            @Override
            public int getItemCount() {

                if (super.getItemCount() > 0) {
                    notWorkerYetTv.setVisibility(View.GONE);
                }

                return super.getItemCount();
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull HolderWorkList holder, int position, @NonNull ModelWorkersList model) {

                Glide.with(getApplicationContext()).load(model.getWorkerImage()).into(holder.workerListImageIv);
                holder.workerListNameTv.setText(model.getWorkerName());
                holder.workerListProfession.setText("Work as : " + model.getWorkerProfession());
                holder.workerListSpecificationTv.setText(model.getWorkerSpecification());

                holder.setOnclickListener(new HolderWorkList.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(WorkersList.this, WorkerDetailsActivity.class);
                        intent.putExtra("workerKey", getRef(position).getKey());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public HolderWorkList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workers_list_design, parent, false);
                return new HolderWorkList(view);
            }
        };

        homeUserRv.setAdapter(adapter);
        adapter.startListening();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        homeUserRv.setLayoutManager(gridLayoutManager);
        homeUserRv.setHasFixedSize(true);
    }
}