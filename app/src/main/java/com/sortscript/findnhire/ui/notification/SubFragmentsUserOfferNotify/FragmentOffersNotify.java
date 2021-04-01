package com.sortscript.findnhire.ui.notification.SubFragmentsUserOfferNotify;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.ui.notification.HolderUserOfferNotify.HolderUserOfferClass;
import com.sortscript.findnhire.ui.notification.ModelUserOfferNotify.ModelUserOfferClass;
import com.sortscript.findnhire.ui.notification.OpenOfferNotifyWorkerProfileActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentOffersNotify extends Fragment {

    View v;
    RecyclerView usersOffersNotifyRv;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<ModelUserOfferClass, HolderUserOfferClass> adapter;
    FirebaseRecyclerOptions<ModelUserOfferClass> options;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    LinearLayoutManager linearLayoutManager;

    public FragmentOffersNotify() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.offers_notify_post_fragment, container, false);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        usersOffersNotifyRv = v.findViewById(R.id.usersOffersNotifyRvId);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<ModelUserOfferClass>()
                .setQuery(mRef.child(mAuth.getCurrentUser().getUid()).child("Offers"), ModelUserOfferClass.class).build();

        adapter = new FirebaseRecyclerAdapter<ModelUserOfferClass, HolderUserOfferClass>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final HolderUserOfferClass holderUserOfferClass, final int i, @NonNull final ModelUserOfferClass modelUserOfferClass) {

                try {

                    holderUserOfferClass.setUserOfferNotifyDetails(getActivity(), modelUserOfferClass.getOfferWorkerImage(), modelUserOfferClass.getOfferWorkerName(),
                            modelUserOfferClass.getOfferWorkerSpecification(), modelUserOfferClass.getOfferWorkerCategory());

                } catch (Exception ignored) {

                }

                holderUserOfferClass.setOnclickListener((view, position) -> {

                    Log.e("sdfdsf", modelUserOfferClass.getOfferWorkerId());

                    Intent intent = new Intent(getActivity(), OpenOfferNotifyWorkerProfileActivity.class);
                    intent.putExtra("openWorkerProfileId", modelUserOfferClass.getOfferWorkerId());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public HolderUserOfferClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_offer_notify_design, parent, false);
                return new HolderUserOfferClass(view);
            }
        };

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        usersOffersNotifyRv.setLayoutManager(linearLayoutManager);
        usersOffersNotifyRv.setAdapter(adapter);
        usersOffersNotifyRv.setHasFixedSize(true);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        mListState = usersOffersNotifyRv.getLayoutManager().onSaveInstanceState();
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
                    usersOffersNotifyRv.getLayoutManager().onRestoreInstanceState(mListState);

                }
            }, 50);
        }

        usersOffersNotifyRv.setLayoutManager(linearLayoutManager);
    }

}