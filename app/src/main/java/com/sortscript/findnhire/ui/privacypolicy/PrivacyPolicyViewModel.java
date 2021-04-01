package com.sortscript.findnhire.ui.privacypolicy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PrivacyPolicyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PrivacyPolicyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Privacy Policy fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}