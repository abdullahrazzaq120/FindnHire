package com.sortscript.findnhire.Worker.WorkerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Activities.HolderActivities.HolderComments;
import com.sortscript.findnhire.Activities.ModelsActivities.ModelComments;
import com.sortscript.findnhire.Activities.WorkerDetailsActivity;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dmax.dialog.SpotsDialog;

public class FragmentNotificationWorker extends Fragment {

    View v;
    RecyclerView workerNotificationRv;
    SpotsDialog progressDialog;
    DatabaseRefs refs;
    TextView notanyreviewyetworkertv;
    FirebaseRecyclerOptions<ModelComments> options;
    FirebaseRecyclerAdapter<ModelComments, HolderComments> adapter;

    public FragmentNotificationWorker() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.notifications_worker_fragment, container, false);

        progressDialog = new SpotsDialog(getActivity(), R.style.CustomPleaseWait);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        refs = new DatabaseRefs();
        notanyreviewyetworkertv = v.findViewById(R.id.notanyreviewyetworkertvid);
        workerNotificationRv = v.findViewById(R.id.workerNotificationRvId);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<ModelComments>().setQuery(refs
                .referenceUIDWorkers
                .child("Reviews"), ModelComments.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelComments, HolderComments>(options) {

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                notanyreviewyetworkertv.setVisibility(View.GONE);
                progressDialog.dismiss();
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

                                Glide.with(getActivity()).load(image).into(holder.commenterUserImageIv);
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

        workerNotificationRv.setAdapter(adapter);
        adapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        workerNotificationRv.setLayoutManager(linearLayoutManager);
        workerNotificationRv.setHasFixedSize(true);
    }

}