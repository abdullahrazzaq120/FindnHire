package com.sortscript.publicworkdeveloper.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sortscript.publicworkdeveloper.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentOffersNotify extends Fragment {

    View v;
    Button contact;

    public FragmentOffersNotify() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.offers_notify_post_fragment, container, false);

        return v;
    }
}
