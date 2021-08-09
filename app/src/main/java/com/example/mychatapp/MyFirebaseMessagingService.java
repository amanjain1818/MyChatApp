package com.example.mychatapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.mychatapp.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.internal.Util;

import static android.content.ContentValues.TAG;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private User user = new User();
    SharedPreferences preferences;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title");
            String message = map.get("message");
            //String senderId = map.get("userId");
            String reciveId = map.get("userId");
            Log.d("tag","reciveidrec"+reciveId);
            String userProfilePic = map.get("userprofilepic");
            String  senderId =map.get("senderId");


            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                createOreoNotifiaction(title, message, reciveId, userProfilePic,senderId);
            } else {
                createNotification(title, message,  reciveId,userProfilePic,senderId);
            }

        }
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        Log.d("token", "debug" + s);

        updateToken(s);
        // Toast.makeText(this, "thik ho"+s , Toast.LENGTH_SHORT).show();
    }

    private void updateToken(String token) {

        Log.d("tag", "debug" + auth);
        Log.d("tag", "debug" + auth.getUid());
        SharedPreferences preferences = getSharedPreferences("chatApp", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = preferences.edit();
        myEdit.putString("fcmtoken", token);
        myEdit.apply();

//        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("User").child();
//        Map<String,Object> map= new HashMap<>();
//        map.put("token",token);
//        databaseReference.updateChildren(map);

    }
//@Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        Task<String> refreshedToken = FirebaseMessaging.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//       // sendRegistrationToServer(refreshedToken);
//    }


//    @Override
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//
//        Task<String> newToken = FirebaseMessaging.getInstance().getToken();
//        if(firebaseUser != null){
//            updateToken(newToken);
//        }
//
//    }


    private void createNotification(String title, String message, String reciveId, String userProfilePic, String senderId) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
                .setAutoCancel(true)
                .setSound(uri);
       // Intent intent = new Intent(this, ChatDetailActivity.class);
      //  intent.putExtra("message", message);
       // intent.putExtra("senderId", senderId);
//        intent.putExtra("userId", reciveId);

       // Log.d("tag","userId"+user.getUserid());

      //  intent.putExtra("userprofilepic", userProfilePic);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
      //  builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(85 - 65), builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOreoNotifiaction(String title, String message, String reciveId, String userProfilePic,String senderId) {
        NotificationChannel channel = new NotificationChannel(AllConstants.CHANNEL_ID, "Message", NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setDescription("Message Description");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        //Intent intent = new Intent(this, ChatDetailActivity.class);
        //intent.putExtra("message", message);
      //  intent.putExtra("userId", reciveId);
        //intent.putExtra("userprofilepic", userProfilePic);
       //intent.putExtra("senderId", senderId);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification.Builder(this, AllConstants.CHANNEL_ID)
                .setContentText(message)
                .setContentTitle(title)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null))
          //      .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setAutoCancel(true)
                .build();
        manager.notify(new Random().nextInt(85 - 65), notification);


    }
}
