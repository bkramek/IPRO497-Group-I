package com.example.ipro497_group_i.ui.checkinout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckInOutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CheckInOutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Check In/Out fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}