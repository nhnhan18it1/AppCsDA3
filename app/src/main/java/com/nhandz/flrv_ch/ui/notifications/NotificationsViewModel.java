package com.nhandz.flrv_ch.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhandz.flrv_ch.DT.AdviseFriends;
import com.nhandz.flrv_ch.DT.Notification;

import java.util.ArrayList;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<ArrayList<AdviseFriends>> mAdviseFriend;
    private MutableLiveData<ArrayList<Notification>> mNotification;



    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mAdviseFriend=new MutableLiveData<>();
        mNotification=new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public void SetDataAdvise(ArrayList<AdviseFriends> adviseFriends){
        if (mAdviseFriend.getValue()!=null&&mAdviseFriend.getValue().size()!=0){
            mAdviseFriend.getValue().removeAll(mAdviseFriend.getValue());
            mAdviseFriend.getValue().addAll(adviseFriends);
        }
        else if (mAdviseFriend.getValue()==null){
            this.mAdviseFriend.setValue(adviseFriends);
            //mAdviseFriend.getValue().addAll(adviseFriends) ;
        }
    }

    public MutableLiveData<ArrayList<Notification>> getmNotification() {
        return mNotification;
    }

    public void setmNotification(ArrayList<Notification> notifications) {
        this.mNotification.setValue(notifications);
    }

    public MutableLiveData<ArrayList<AdviseFriends>> getmAdviseFriend() {
        return mAdviseFriend;
    }
}