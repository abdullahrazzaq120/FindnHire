package com.sortscript.findnhire.ui.changepassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel changePasswordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changePasswordViewModel =
                new ViewModelProvider(this).get(ChangePasswordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);

        return root;
    }
}