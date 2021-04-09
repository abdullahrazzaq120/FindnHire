package com.sortscript.findnhire.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.sortscript.findnhire.Classes.DatabaseRefs;
import com.sortscript.findnhire.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class FeedbackFragment extends Fragment {

    private FeedbackViewModel feedbackViewModel;
    DatabaseRefs ref;
    TextInputEditText feedbackEt;
    Button sendFeedbackBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        feedbackViewModel =
                new ViewModelProvider(this).get(FeedbackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        ref = new DatabaseRefs();
        feedbackEt = root.findViewById(R.id.feedbackEtId);
        sendFeedbackBtn = root.findViewById(R.id.sendFeedbackBtnId);

        sendFeedbackBtn.setOnClickListener(v -> {
            String feedback = feedbackEt.getText().toString();
            if (!feedback.isEmpty()) {
                ref.referenceFeedback
                        .child("userFeedback")
                        .setValue(feedback)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Feedback Sent Successfully!", Toast.LENGTH_SHORT).show();
                            feedbackEt.setText("");
                        });
            }
        });

        return root;
    }
}