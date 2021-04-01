package com.sortscript.findnhire.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sortscript.findnhire.R;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    String chatterKey;
    DatabaseReference reference;
    String name;
    EditText textEt;
    ImageView btnSend;
    LinearLayout vewLayoutll;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt_room);

        chatterKey = getIntent().getStringExtra("chatterId");
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child("Workers");
        arrayList = new ArrayList<>();
        vewLayoutll = findViewById(R.id.vewLayoutId);
        btnSend = findViewById(R.id.btnSendId);
        textEt = findViewById(R.id.textId);

        reference.child(chatterKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("WorkerName").getValue().toString();
                getSupportActionBar().setTitle(name);

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String msg = textEt.getText().toString();

                        

                        addViewFunction(msg);
                        textEt.setText("");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addViewFunction(String msg) {
        View addV = getLayoutInflater().inflate(R.layout.new_view, null, false);
        TextView addMsgTv = addV.findViewById(R.id.addMyMsgTvId);
        addMsgTv.setText(msg);

        vewLayoutll.addView(addV);
        arrayList.add(addMsgTv.getText().toString());

    }

    private void removeView(View view) {
        vewLayoutll.removeView(view);
    }

}