package com.sortscript.findnhire.ui.privacypolicy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class PrivacyPolicyFragment extends Fragment {

    private PrivacyPolicyViewModel privacyPolicyViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        privacyPolicyViewModel =
                new ViewModelProvider(this).get(PrivacyPolicyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        return root;
    }
}