package com.sortscript.publicworkdeveloper.ui.howwework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sortscript.publicworkdeveloper.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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