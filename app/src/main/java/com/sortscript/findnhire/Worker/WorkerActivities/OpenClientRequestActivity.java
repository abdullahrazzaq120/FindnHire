package com.sortscript.findnhire.Worker.WorkerActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.AuthFailureError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.Classes.MySingleton;
import com.sortscript.findnhire.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OpenClientRequestActivity extends AppCompatActivity {

    TextView openRequestName, openRequestDescription;
    VideoView openRequestVideo;
    Button openRequestSendOfferBtn;
    String keyRequest, clientIdRequest;
    DatabaseReference reference, reference2, myRef;
    MediaController mediaController;
    FirebaseAuth mAuth;
    String workerImg, workerName, workerSpecifications, workerCategory;
    ProgressDialog loader;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey;
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    private static final String WORKER_NAME = "WorkerName";
    private static final String WORKER_EMAIL = "WorkerEmail";
    private static final String WORKER_ADDRESS = "WorkerAddress";
    private static final String WORKER_IMAGE = "WorkerImage";
    private static final String WORKER_CATEGORY = "WorkerCategory";
    private static final String WORKER_GENDER = "WorkerGender";
    private static final String WORKER_SPECIFICATION = "WorkerSpecification";
    String TOPIC;
    String tkn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_client_request);

        serverKey = "key=" + getString(R.string.firebase_server_key);
        mediaController = new MediaController(this);
        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Posts").child("Users");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Members").child("Users");
        myRef = FirebaseDatabase.getInstance().getReference().child("Members").child("Workers");

        openRequestName = findViewById(R.id.openRequestNameId);
        openRequestDescription = findViewById(R.id.openRequestDescriptionId);
        openRequestVideo = findViewById(R.id.openRequestVideoId);
        openRequestSendOfferBtn = findViewById(R.id.openRequestSendOfferBtnId);

        keyRequest = getIntent().getStringExtra("requestKey");
        clientIdRequest = getIntent().getStringExtra("requestUserId");

        myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    workerName = snapshot.child(WORKER_NAME).getValue().toString();
                    workerSpecifications = snapshot.child(WORKER_SPECIFICATION).getValue().toString();
                    workerImg = snapshot.child(WORKER_IMAGE).getValue().toString();
                    workerCategory = snapshot.child(WORKER_CATEGORY).getValue().toString();
                } catch (Exception ignored) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference2.child(clientIdRequest).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tkn = snapshot.child("deviceToken").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(keyRequest).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {

                    String name = snapshot.child("UserName").getValue().toString();
                    String description = snapshot.child("UserDescription").getValue().toString();
                    String video = snapshot.child("UserVideo").getValue().toString();

                    openRequestName.setText(name);
                    openRequestDescription.setText(description);

                    openRequestVideo.setMediaController(mediaController);
                    mediaController.setAnchorView(openRequestVideo);
                    openRequestVideo.start();

                    Uri uri = Uri.parse(video);
                    openRequestVideo.setVideoURI(uri);

                    openRequestSendOfferBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog alertDialog;
                            AlertDialog.Builder dialog = new AlertDialog.Builder(OpenClientRequestActivity.this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v1 = inflater.inflate(R.layout.confirm_send_offer_dialog, null);
                            dialog.setView(v1);
                            alertDialog = dialog.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertDialog.show();

                            TextView tv1 = v1.findViewById(R.id.yesSendOfferBtnId);
                            TextView tv2 = v1.findViewById(R.id.noSendOfferBtnId);

                            tv1.setOnClickListener(view1 -> {

                                loader.setMessage("Sending Offer");
                                loader.setCanceledOnTouchOutside(false);
                                loader.show();

                                Map<String, Object> map = new HashMap<>();
                                map.put("OfferWorkerName", workerName);
                                map.put("OfferWorkerSpecification", workerSpecifications);
                                map.put("OfferWorkerImage", workerImg);
                                map.put("OfferWorkerCategory", workerCategory);
                                map.put("OfferWorkerId", mAuth.getCurrentUser().getUid());

                                reference2.child(clientIdRequest).child("Offers").push().setValue(map)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {

                                                TOPIC = tkn; //topic must match with what the receiver subscribed to

                                                try {
                                                    Log.e("sjdbkajs", TOPIC);
                                                } catch (Exception e) {
                                                    Log.e("maslakiay", e.getMessage());
                                                }

                                                NOTIFICATION_TITLE = workerName;
                                                NOTIFICATION_MESSAGE = workerSpecifications;

                                                JSONObject notification = new JSONObject();
                                                JSONObject notifcationBody = new JSONObject();
                                                try {
                                                    notifcationBody.put("title", NOTIFICATION_TITLE);
                                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                                                    notification.put("to", TOPIC);
                                                    notification.put("data", notifcationBody);
                                                } catch (JSONException e) {
                                                    Log.e(TAG, "onCreate: " + e.getMessage());
                                                }
                                                sendNotification(notification);

                                                loader.dismiss();
                                                Toast.makeText(OpenClientRequestActivity.this, "Offer Send Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                loader.dismiss();
                                                Toast.makeText(OpenClientRequestActivity.this, task.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                alertDialog.dismiss();
                            });

                            tv2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(OpenClientRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
//                        edtTitle.setText("");
//                        edtMessage.setText("");
                },
                error -> {
                    Toast.makeText(OpenClientRequestActivity.this, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}