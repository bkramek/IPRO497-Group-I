package com.example.ipro497_group_i;

import androidx.databinding.BaseObservable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

public class DataBaseViewModal extends ViewModel {

    private Long myUserId;


    public Long getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(Long myUserId) {
        this.myUserId = myUserId;
    }
}
