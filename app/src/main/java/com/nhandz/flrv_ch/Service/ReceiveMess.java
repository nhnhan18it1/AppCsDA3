package com.nhandz.flrv_ch.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nhandz.flrv_ch.ChatActivity;
import com.nhandz.flrv_ch.MainActivity;
import com.nhandz.flrv_ch.MessengerListActivity;
import com.nhandz.flrv_ch.R;
import com.nhandz.flrv_ch.ReceiveCallActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ReceiveMess extends Service {
    private String TAG = getClass().getSimpleName();
    public static final String CHANNEL_1_ID="Channel 1";
    public static final String CHANNEL_2_ID="Channel 2";
    public static Socket socket;
    public ReceiveMess() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public void onCreate() {
        createNotificationChannels();
        super.onCreate();
        if (MainActivity.mSocket==null){
            try {
                socket= IO.socket("https://nhandz.herokuapp.com/");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            socket.connect();
            Log.e("remess_service", "onCreate: new socket "+socket.connected() );
        }
        else {
            socket=MainActivity.mSocket;
            Log.e("remess_service", "onCreate: local socket" );
        }

        Log.e("remess_service", "onCreate: startService" );
        startForeground(134,new Notification.Builder(getApplicationContext(),CHANNEL_1_ID).build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);

        try {
            Log.e("remess_service", "onStartCommand: startService "+socket.connected() );
            int count=0;

            while (count<10){
                count++;
                Thread.sleep(100);
                Log.e("remess_service", "onStartCommand: startService "+socket.connected() );
            }



            Intent trsnIntent=new Intent(ReceiveMess.this, MessengerListActivity.class);
            trsnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            final PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,trsnIntent,PendingIntent.FLAG_UPDATE_CURRENT);



            socket.on("Server-send-messagePP", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e("remess_service", "call: "+args[0]);

                    try {
                        JSONArray jsonArray=new JSONArray(args);

                        JSONObject jsonObject= (JSONObject) jsonArray.get(0);
                        Notification notification= new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)//Notification(getApplicationContext(),5);
                                .setSmallIcon(R.drawable.logo3)
                                .setContentTitle("Tin nhắn : "+jsonObject.get("Name"))
                                .setContentText(""+jsonObject.get("Content"))
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent)
                                .build();
                        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(2,notification);
                        //notificationManager.cancel(2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            socket.on("s_request", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        //data {IDS,Avt,name}



                        JSONArray jsonArray=new JSONArray(args);
                        Log.e(TAG, "call: s_request "+jsonArray.toString() );
                        JSONObject jsonObject=(JSONObject) jsonArray.get(0);
                        Intent intent1=new Intent(ReceiveMess.this, ReceiveCallActivity.class);
                        intent1.putExtra("data",jsonObject.toString());
                        intent1.putExtra("Type","R");
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            socket.on("notifyCMT", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONArray jsonArray=new JSONArray(args);
                        JSONObject jsonObject= (JSONObject) jsonArray.get(0);



                        Notification notification= new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)//Notification(getApplicationContext(),5);
                                .setSmallIcon(R.drawable.logo3)
                                .setContentTitle("Binh luan : "+jsonObject.get("Name"))
                                .setContentText(""+jsonObject.get("Content"))
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent)
                                .build();
                        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(3,notification);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return Service.START_STICKY;
        }catch (Exception e){
            e.printStackTrace();
            return Service.START_STICKY;
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel1.setDescription("This is channel 2");


            NotificationManager manager = this.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
