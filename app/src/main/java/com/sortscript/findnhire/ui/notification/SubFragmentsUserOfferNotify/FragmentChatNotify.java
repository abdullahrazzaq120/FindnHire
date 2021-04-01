package com.sortscript.findnhire.ui.notification.SubFragmentsUserOfferNotify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentChatNotify extends Fragment {

    View v;
    Button call;

    public FragmentChatNotify() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.chat_notify_post_fragment, container, false);

        return v;

    }


}
