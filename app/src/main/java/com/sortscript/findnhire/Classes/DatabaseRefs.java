package com.sortscript.findnhire.Classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DatabaseRefs {

    public DatabaseRefs() {
    }

    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public String uid = firebaseAuth.getCurrentUser().getUid();

    public DatabaseReference mainReference = FirebaseDatabase.getInstance().getReference();
    public DatabaseReference referenceWorkers = mainReference.child("Members").child("Workers");
    public DatabaseReference referenceUsers = mainReference.child("Members").child("Users");
    public DatabaseReference referenceUIDWorkers = mainReference.child("Members").child("Workers").child(uid);
    public DatabaseReference referenceWorkersTimings = mainReference.child("Timings");
    public DatabaseReference referenceWorkersUIDTimings = mainReference.child("Timings").child(uid);
    public DatabaseReference referenceUIDUsers = mainReference.child("Members").child("Users").child(uid);
    public StorageReference mStorageRefImg = FirebaseStorage.getInstance().getReference().child("Members").child("Workers");
}
