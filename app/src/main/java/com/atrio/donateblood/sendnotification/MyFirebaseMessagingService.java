package com.atrio.donateblood.sendnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.atrio.donateblood.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arpita Patel on 22-08-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String tittle = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String imageUri = remoteMessage.getData().get("image");
        String msg_id = remoteMessage.getData().get("msg_id");
        String token_id = remoteMessage.getData().get("token_id");
        String body_donor =remoteMessage.getData().get("body");
        String phon_no = remoteMessage.getData().get("pho_no");
        String click_action = remoteMessage.getNotification().getClickAction();
        String current_token = FirebaseInstanceId.getInstance().getToken();
        Log.i("phon_no44",""+phon_no);

        bitmap = getBitmapfromUrl(imageUri);
        if (token_id!=null) {
            if (!token_id.equals(current_token)) {
                sendNotification(tittle, body, bitmap, token_id, msg_id, click_action,body_donor,phon_no);
            }
        }else{
            sendNotification(tittle, body_donor, bitmap, token_id, msg_id, click_action,body_donor,phon_no);
        }
    }

    private void sendNotification(String tittle, String messageBody, Bitmap image, String token_id, String msg_id,String click_action,String body_donor,String phon_no) {
        Intent intent = new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("token_id", token_id);
        intent.putExtra("msg_id", msg_id);
        intent.putExtra("body", body_donor);
        intent.putExtra("phon_no",phon_no);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder .setLargeIcon(image)
            .setSmallIcon(R.drawable.ic_explore_black_24dp)
                    .setContentTitle(tittle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
           notificationBuilder .setLargeIcon(image)
                    .setSmallIcon(R.drawable.ic_explore_black_24dp)
                    .setContentTitle(tittle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }
        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}