package com.sortscript.findnhire.ui.helpline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HelplineFragment extends Fragment {

    private HelplineViewModel helplineViewModel;
    TextInputEditText helplineText;
    ImageView helplineBtn;
    DatabaseRefs refs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helplineViewModel =
                new ViewModelProvider(this).get(HelplineViewModel.class);
        View root = inflater.inflate(R.layout.fragment_helpline, container, false);

        refs = new DatabaseRefs();
        helplineText = root.findViewById(R.id.helplineTextId);
        helplineBtn = root.findViewById(R.id.helplineBtnId);

        helplineBtn.setOnClickListener(v -> {
            String helplinemsg = helplineText.getText().toString();
            if (!helplinemsg.isEmpty()) {
                refs.referenceHelplineMsg
                        .push().child("message")
                        .setValue(helplinemsg)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Message Delivered Successfully!", Toast.LENGTH_SHORT).show();
                            helplineText.setText("");
                        });
            }
        });
        return root;
    }
}