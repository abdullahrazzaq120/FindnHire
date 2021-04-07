package com.sortscript.findnhire.Worker.WorkerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.Classes.RetrieveWorkerDetail;
import com.sortscript.findnhire.Classes.SetAnimatedScroll;
import com.sortscript.findnhire.Interfaces.SimpleCallback;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.Worker.WorkerActivities.WorkerAvailabilityActivity;
import com.sortscript.findnhire.Worker.WorkerActivities.WorkerProfileActivity;

import org.jetbrains.annotations.NotNull;

public class FragmentHomeWorker extends Fragment {

    View v;
    TextView intentWorkerProfileSettingsTv, intentAvailablityTv, homeWorkerNameTv, homeWorkerProfessionTv,
            homeWorkerContactTv, homeWorkerTimingsTv, homeWorkerSpecificationTv;

    ImageView homeWorkerImage;
    NestedScrollView homeWorkerScroll;
    SetAnimatedScroll setAnimatedScroll;
    RetrieveWorkerDetail retrieveWorkerDetail;
    DatabaseRefs refs;

    public FragmentHomeWorker() {
        retrieveWorkerDetail = new RetrieveWorkerDetail(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_worker_fragment, container, false);

        callIds(v);
        refs = new DatabaseRefs();

        setAnimatedScroll = new SetAnimatedScroll(getActivity(), homeWorkerScroll, homeWorkerImage);
        setAnimatedScroll.ListenScroll();

        refs.referenceWorkersUIDTimings
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            try {
                                String timing = snapshot.child("workerAvailability").getValue().toString();
                                homeWorkerTimingsTv.setText(timing);
                            } catch (Exception ignored) {
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        intentWorkerProfileSettingsTv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WorkerProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        intentAvailablityTv.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WorkerAvailabilityActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        retrieveWorkerDetail.setUtils(new SimpleCallback<String>() {
            @Override
            public void callback(String img, String name, String profession, String contact, String specs) {
                Glide.with(getActivity()).load(img).into(homeWorkerImage);
                homeWorkerNameTv.setText(name);
                homeWorkerProfessionTv.setText(profession);
                homeWorkerContactTv.setText(contact);
                homeWorkerSpecificationTv.setText(specs);
            }
        });

        return v;
    }

    private void callIds(@NotNull View v) {
        intentWorkerProfileSettingsTv = v.findViewById(R.id.intentWorkerProfileSettingsTvId);
        intentAvailablityTv = v.findViewById(R.id.intentAvailablityTvId);
        homeWorkerNameTv = v.findViewById(R.id.homeWorkerNameTvId);
        homeWorkerProfessionTv = v.findViewById(R.id.homeWorkerProfessionTvId);
        homeWorkerContactTv = v.findViewById(R.id.homeWorkerContactTvId);
        homeWorkerTimingsTv = v.findViewById(R.id.homeWorkerTimingsTvId);
        homeWorkerSpecificationTv = v.findViewById(R.id.homeWorkerSpecificationTvId);
        homeWorkerScroll = v.findViewById(R.id.homeWorkerScrollId);
        homeWorkerImage = v.findViewById(R.id.homeWorkerImageId);
    }
}