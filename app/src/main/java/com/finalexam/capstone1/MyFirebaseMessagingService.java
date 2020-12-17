package com.finalexam.capstone1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;

import static com.finalexam.capstone1.LoginActivity.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String id = "my_channel_02";
    CharSequence name = "fcm_nt";
    String description = "push";
    int importance = NotificationManager.IMPORTANCE_LOW;

    /**
     * 구글 토큰을 얻는 값입니다.
     * 아래 토큰은 앱이 설치된 디바이스에 대한 고유값으로 푸시를 보낼때 사용됩니다.
     * **/
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("Firebase", "FirebaseInstanceIDService : " + s);
    }


    /**
     * 메세지를 받았을 경우 그 메세지에 대하여 구현하는 부분입니다.
     * **/
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            Log.d("FCM Log", "알림메시지: " + remoteMessage.getNotification().getBody());
            String meessageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(messageTitle)
                            .setContentText(meessageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);


            //알림 카테고리
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }

        /*if(remoteMessage.getNotification()!=null){//포그라운드
            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
        }
        else if(remoteMessage.getData().size()>0){//백그라운드
            sendNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"));
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String messageBody, String messageTitle){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        mChannel.setDescription(description);
        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int notifyID=2;

        String CHANNEL_ID="my_channel_02";

        try{
            Notification notification = new Notification.Builder(MyFirebaseMessagingService.this)
                    .setContentText(URLDecoder.decode(messageTitle, "UTF-8"))
                    .setContentText(URLDecoder.decode(messageBody,"UTF-8"))
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build();

            mNotificationManager.notify(notifyID, notification);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
