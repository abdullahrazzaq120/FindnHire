package com.sortscript.findnhire.ui.howwework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HowWeWorkFragment extends Fragment {

    private HowWeWorkViewModel howWeWorkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        howWeWorkViewModel =
                new ViewModelProvider(this).get(HowWeWorkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_howwework, container, false);

        return root;
    }
}