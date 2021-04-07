package com.sortscript.findnhire.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sortscript.findnhire.R;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    TextView changePasswordTv, accountSettingTv1;
    Boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        changePasswordTv = findViewById(R.id.changePasswordTvId);
        accountSettingTv1 = findViewById(R.id.accountSettingTv1Id);

        accountSettingTv1.setOnClickListener(view -> {
            if (!b) {
                expandSettingsBtn1();
            } else {
                enCloseSettingBtn1();
            }
        });

        changePasswordTv.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

    private void enCloseSettingBtn1() {
        b = false;
        changePasswordTv.setVisibility(View.GONE);
        accountSettingTv1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_less_24, 0);
    }

    public void expandSettingsBtn1() {
        b = true;
        changePasswordTv.setVisibility(View.VISIBLE);
        accountSettingTv1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_expand_more_24, 0);
    }

}