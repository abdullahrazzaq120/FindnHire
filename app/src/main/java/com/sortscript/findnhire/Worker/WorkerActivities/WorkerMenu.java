package com.sortscript.findnhire.Worker.WorkerActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sortscript.findnhire.Activities.AuthenticationActivity;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;
import com.sortscript.findnhire.View_Pager_Adapters.ViewPagerAdapter;
import com.sortscript.findnhire.Worker.WorkerFragments.FragmentHomeWorker;
import com.sortscript.findnhire.Worker.WorkerFragments.FragmentNotificationWorker;

import dmax.dialog.SpotsDialog;

public class WorkerMenu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    FirebaseAuth mAuth;
    DatabaseRefs refs;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_menu);

        progressDialog = new SpotsDialog(WorkerMenu.this, R.style.Custom);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        refs = new DatabaseRefs();

        refs.referenceUIDWorkers
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            try {
                                String nameW = snapshot.child("workerName").getValue().toString();
                                String catW = snapshot.child("workerCategory").getValue().toString();
                                getSupportActionBar().setTitle(nameW + " : " + catW);
                            } catch (Exception ignored) {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        tabLayout = findViewById(R.id.tablayoutWorkerMenu_id);
        viewPager = findViewById(R.id.viewpagerWorkerMenu_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //add fragment here
        adapter.AddFragment(new FragmentHomeWorker(), "Availability");
        adapter.AddFragment(new FragmentNotificationWorker(), "Reviews");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.worker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.worker_logout) {
            AlertDialog alertDialog;
            AlertDialog.Builder dialog = new AlertDialog.Builder(WorkerMenu.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v2 = inflater.inflate(R.layout.logout_dialog, null);
            dialog.setView(v2);
            alertDialog = dialog.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            TextView tv1 = v2.findViewById(R.id.yesLogoutBtnId);
            TextView tv2 = v2.findViewById(R.id.cancelLogoutBtnId);

            tv1.setOnClickListener(view -> {
                mAuth.signOut();
                startActivity(new Intent(WorkerMenu.this, AuthenticationActivity.class));
                finish();
                alertDialog.dismiss();
            });

            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        refs.referenceUIDWorkers
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            try {
                                String tkn = FirebaseInstanceId.getInstance().getToken();
                                refs.referenceUIDWorkers.child("deviceToken").setValue(tkn);
                            } catch (Exception ignored) {
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);

    }
}