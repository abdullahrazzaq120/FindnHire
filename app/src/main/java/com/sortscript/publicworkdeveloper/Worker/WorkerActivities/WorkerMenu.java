package com.sortscript.publicworkdeveloper.Worker.WorkerActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sortscript.publicworkdeveloper.View_Pager_Adapters.ViewPagerAdapter;
import com.sortscript.publicworkdeveloper.Activities.AuthenticationActivity;
import com.sortscript.publicworkdeveloper.R;
import com.sortscript.publicworkdeveloper.Worker.WorkerFragments.FragmentChatWorker;
import com.sortscript.publicworkdeveloper.Worker.WorkerFragments.FragmentNotificationWorker;
import com.sortscript.publicworkdeveloper.Worker.WorkerFragments.FragmentRequestsWorker;
import com.sortscript.publicworkdeveloper.ui.notification.NotificationViewModel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WorkerMenu extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private NotificationViewModel notificationViewModel;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_menu);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Members").child("Workers");
        tabLayout = findViewById(R.id.tablayoutWorkerMenu_id);
        viewPager = findViewById(R.id.viewpagerWorkerMenu_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //add fragment here
        adapter.AddFragment(new FragmentRequestsWorker(), "Requests");
        adapter.AddFragment(new FragmentChatWorker(), "Chats");
        adapter.AddFragment(new FragmentNotificationWorker(), "Notifications");

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

            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    startActivity(new Intent(WorkerMenu.this, AuthenticationActivity.class));
                    finish();
                    alertDialog.dismiss();
                }
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

        String tkn = FirebaseInstanceId.getInstance().getToken();
        reference.child(mAuth.getCurrentUser().getUid()).child("deviceToken").setValue(tkn);
    }
}