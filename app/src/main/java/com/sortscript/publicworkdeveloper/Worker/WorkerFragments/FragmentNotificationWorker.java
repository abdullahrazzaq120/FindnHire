package com.sortscript.publicworkdeveloper.Worker.WorkerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sortscript.publicworkdeveloper.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentNotificationWorker extends Fragment {

    View v;
    Button call;

    public FragmentNotificationWorker() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.notifications_worker_fragment, container, false);

        return v;

    }


}