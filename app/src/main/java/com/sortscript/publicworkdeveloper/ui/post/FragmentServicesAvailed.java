package com.sortscript.publicworkdeveloper.ui.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sortscript.publicworkdeveloper.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentServicesAvailed extends Fragment {

    View v;
    RecyclerView serviceAvailedRv;

    public FragmentServicesAvailed() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.services_availed_post_fragment, container, false);

        serviceAvailedRv = v.findViewById(R.id.rv_service_availed_id);
        return v;
    }
}
