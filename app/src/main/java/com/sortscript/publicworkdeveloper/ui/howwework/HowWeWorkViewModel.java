package com.sortscript.publicworkdeveloper.ui.howwework;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HowWeWorkViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HowWeWorkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is HowWeWork fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}