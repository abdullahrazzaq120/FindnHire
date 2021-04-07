package com.sortscript.findnhire.Classes;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Interfaces.SimpleCallback;

public class RetrieveWorkerDetail {

    Context context;
    DatabaseRefs refs;
    String img, name, profession, contact, availability, specification;
    private static final String TAG = "RetrieveWorkerDetail";

    public RetrieveWorkerDetail(Context context) {
        this.context = context;
        refs = new DatabaseRefs();
    }

    public void setUtils(@NonNull SimpleCallback<String> finishedCallback) {
        refs.referenceUIDWorkers
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            if (snapshot.exists()) {
                                img = snapshot.child("workerImage").getValue().toString();
                                name = snapshot.child("workerName").getValue().toString();
                                profession = snapshot.child("workerProfession").getValue().toString();
                                contact = snapshot.child("workerContact").getValue().toString();
                                specification = snapshot.child("workerSpecification").getValue().toString();
                                finishedCallback.callback(img, name, profession, contact, specification);
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

}
