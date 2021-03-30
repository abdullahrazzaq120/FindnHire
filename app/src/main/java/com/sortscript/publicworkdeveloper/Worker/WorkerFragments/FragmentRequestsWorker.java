package com.sortscript.publicworkdeveloper.Worker.WorkerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.publicworkdeveloper.R;
import com.sortscript.publicworkdeveloper.Worker.WorkerActivities.OpenClientRequestActivity;
import com.sortscript.publicworkdeveloper.Worker.WorkerHolders.HolderWorkerClass;
import com.sortscript.publicworkdeveloper.Worker.WorkerModels.ModelWorkerClass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentRequestsWorker extends Fragment {

    View v;
    RecyclerView requestWorkerRv;
    DatabaseReference mRef, mRef2;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<ModelWorkerClass, HolderWorkerClass> adapter;
    FirebaseRecyclerOptions<ModelWorkerClass> options;
    String category, address;
    LinearLayoutManager linearLayoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    public FragmentRequestsWorker() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.requests_worker_fragment, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        requestWorkerRv = v.findViewById(R.id.requestWorkerRvId);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Posts").child("Users");
        mRef2 = FirebaseDatabase.getInstance().getReference().child("Members").child("Workers");

        return v;

    }


    @Override
    public void onStart() {
        super.onStart();

        mRef2.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                category = snapshot.child("WorkerCategory").getValue().toString();
                address = snapshot.child("WorkerAddress").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        options = new FirebaseRecyclerOptions.Builder<ModelWorkerClass>()
                .setQuery(mRef.orderByChild("WorkerCategory").equalTo(category), ModelWorkerClass.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelWorkerClass, HolderWorkerClass>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final HolderWorkerClass holderWorkerClass, final int i, @NonNull final ModelWorkerClass modelWorkerClass) {

                try {

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String addressWorker = dataSnapshot.child("UserAddress").getValue(String.class);
                                if (addressWorker.toLowerCase().contains(address.toLowerCase())) {

                                    holderWorkerClass.setRequestsDetails(getActivity(), modelWorkerClass.getUserName(), modelWorkerClass.getUserDescription(),
                                            modelWorkerClass.getCurrentDate(), modelWorkerClass.getUserImage());

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception ignored) {

                }

                holderWorkerClass.setOnclickListener((view, position) -> {
                    String postId = getRef(i).getKey();
                    Log.e("clientNodeKey", postId);

                    Intent intent = new Intent(getActivity(), OpenClientRequestActivity.class);
                    intent.putExtra("requestKey", postId);
                    intent.putExtra("requestUserId", modelWorkerClass.getUserId());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HolderWorkerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_requests_design, parent, false);
                return new HolderWorkerClass(view);
            }
        };

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        requestWorkerRv.setLayoutManager(linearLayoutManager);
        requestWorkerRv.setAdapter(adapter);
        requestWorkerRv.setHasFixedSize(true);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        mListState = requestWorkerRv.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    requestWorkerRv.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }

        requestWorkerRv.setLayoutManager(linearLayoutManager);
    }
}