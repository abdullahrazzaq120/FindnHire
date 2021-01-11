package com.sortscript.publicworkdeveloper.ui.helpline;

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

public class HelplineFragment extends Fragment {

    private HelplineViewModel helplineViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helplineViewModel =
                new ViewModelProvider(this).get(HelplineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_helpline, container, false);

        return root;
    }
}