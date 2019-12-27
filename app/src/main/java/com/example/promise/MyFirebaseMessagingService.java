package com.example.promise;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences shared;
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM Message
        Log.e(TAG, remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0){
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            //PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "My:Tag");
            wakeLock.acquire(5000);

            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            sendNotification( title, body );
            handleNow();
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null){
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String getMessage = remoteMessage.getNotification().getBody();
            if(TextUtils.isEmpty(getMessage)) {
                Log.e(TAG, "ERR: Message data is empty...");
            }
            else {
                Map<String, String> mapMessage = new HashMap<>();
                assert getMessage != null;
                mapMessage.put("key", getMessage );
                // Broadcast Data Sending Test
                Intent intent = new Intent("alert_data");
                intent.putExtra("msg", getMessage);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            }
        }
    }
    private void handleNow(){
        Log.d(TAG, "Short lived task is done.");
    }


    /** 새로운 토큰이 생성되는 경우 **/
    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "here ! sendRegistrationToServer! token is " + token);
    }

    private void sendNotification(String title, String body) {
        if (title == null){
            //제목이 없는 payload이면       php에서 보낼때 이미 한번 점검했음.
            title = "공지사항"; //기본제목을 적어 주자.
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setColor( Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);
        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService( Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        Intent intent= new Intent(this, MainActivity.class);

        PendingIntent pending= PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pending);   //PendingIntent 설정

        builder.setAutoCancel(true);         //클릭하면 자동으로 알림 삭제
        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }
}
