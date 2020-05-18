package com.nhandz.flrv_ch.Service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.nhandz.flrv_ch.R;

import static com.nhandz.flrv_ch.Service.Autosend.CHANNEL_1_ID;
import static com.nhandz.flrv_ch.Service.Autosend.CHANNEL_2_ID;

public class ReceiveMess extends Service {
    Notification notification;
    public ReceiveMess() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);

        try {

            notification=new Notification.Builder(getApplicationContext(),CHANNEL_2_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("New messenger")
                    .setContentText("Tin nhan tu ....")
                    .setCategory(NotificationCompat.CATEGORY_PROMO)
                    .build();
            startForeground(134,notification);
            return Service.START_STICKY;
        }catch (Exception e){
            e.printStackTrace();
            return Service.START_STICKY;
        }
    }
}
