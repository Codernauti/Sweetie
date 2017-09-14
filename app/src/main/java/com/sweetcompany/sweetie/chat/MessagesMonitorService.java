package com.sweetcompany.sweetie.chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.sweetcompany.sweetie.DashboardActivity;
import com.sweetcompany.sweetie.R;
import com.sweetcompany.sweetie.firebase.FirebaseMsgNotificationController;
import com.sweetcompany.sweetie.model.MsgNotification;
import com.sweetcompany.sweetie.utils.SharedPrefKeys;
import com.sweetcompany.sweetie.utils.Utility;

import java.util.ArrayList;


public class MessagesMonitorService extends Service implements FirebaseMsgNotificationController.MsgNotificationControllerListener {

    private static final String TAG = "MessageMonitorService";

    private static final int NOTIFICATION_ID = 100;

    private FirebaseMsgNotificationController mController;

    private NotificationManager mNotificationManager;
    private ArrayList<String> mChatsNotified = new ArrayList<>();
    private ArrayList<MsgNotification> mMessagesNotified = new ArrayList<>();

    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        String userUid = Utility.getStringPreference(this, SharedPrefKeys.USER_UID);
        mController = new FirebaseMsgNotificationController(userUid);
        mController.setListener(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        mController.attachListener();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.detach();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // methods for ChatActivity

    public void cancelNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public void resetCacheFor(String chatUid) {
        removeChatFromCache(chatUid);
        removeMessagesFromCache(chatUid);
    }


    private void removeMessagesFromCache(String chatUid) {
        for (int i = 0; i < mMessagesNotified.size(); i++) {
            if (mMessagesNotified.get(i).getChatUid().equals(chatUid)) {
                mMessagesNotified.remove(i);
            }
        }
    }

    private void removeChatFromCache(String chatUid) {
        for (int i = 0; i < mChatsNotified.size(); i++) {
            if (mChatsNotified.get(i).equals(chatUid)) {
                mChatsNotified.remove(i);
                return;
            }
        }
    }


    // controller callbacks

    @Override
    public void onMessageArrived(MsgNotification msg) {
        // consume the notification from db
        mController.removeMsgNotification(msg.getUid());

        if (chatInForeground(msg.getChatUid())) {
            return;
        } else if (chatIsSilenced(msg.getChatUid())) {
            return;
        }

        String contentTitle = "";
        String contentText = "";

        if (chatNotAlreadyNotified(msg.getChatUid())) {
            mChatsNotified.add(msg.getChatUid());
        }
        mMessagesNotified.add(msg);

        // init intent for open ChatActivity or DashboardActivity
        Intent intent;
        PendingIntent pendingIntent;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        NotificationCompat.Action replyAction;

        // invariants mChatsNotified > 0 && mMessagesNotified > 0
        // one chat case
        if (mChatsNotified.size() == 1) {
            contentTitle = msg.getChatTitle();
            // TODO: notificationIcon = msg.getNotificationIconUri() -> download from storage

            if (mMessagesNotified.size() == 1) {
                contentText = msg.getText();
            } else {
                contentText = mMessagesNotified.size() + " new messages";
            }

            // start ChatActivity
            intent = new Intent(this, ChatActivity.class)
                    .putExtra(ChatActivity.CHAT_DATABASE_KEY, msg.getChatUid())
                    .putExtra(ChatActivity.ACTION_DATABASE_KEY, msg.getParentActionUid())
                    .putExtra(ChatActivity.CHAT_TITLE, msg.getChatTitle());

            stackBuilder.addParentStack(DashboardActivity.class)    // start MainActivity
                    .addParentStack(ChatActivity.class)         // start DashboardActivity
                    .addNextIntent(intent);

            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                    | PendingIntent.FLAG_ONE_SHOT);

            replyAction = new NotificationCompat.Action(R.drawable.ic_reply_black_24dp, "Reply", pendingIntent);
        }
        // multiple chats case
        else {
            contentTitle = getString(R.string.app_name);
            contentText = mMessagesNotified.size() + " messages from " + mChatsNotified.size() + " chats";

            // start DashboardActivity
            intent = new Intent(this, DashboardActivity.class);

            stackBuilder.addParentStack(DashboardActivity.class)
                    .addNextIntent(intent);

            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            replyAction = new NotificationCompat.Action(R.drawable.ic_reply_black_24dp, "Open", pendingIntent);
        }

        // TODO: apply style big notification
        // https://developer.android.com/guide/topics/ui/notifiers/notifications.html#ApplyStyle
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_msg_notification)
                .setNumber(mMessagesNotified.size())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setColor(ContextCompat.getColor(this, R.color.rosa_sweetie))
                .addAction(replyAction)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private boolean chatIsSilenced(String chatUid) {
        // TODO: implements it
        return false;
    }

    private boolean chatInForeground(String chatUid) {
        return Utility.getStringPreference(this, SharedPrefKeys.CHAT_FOREGROUND_UID).equals(chatUid);
    }

    private boolean chatNotAlreadyNotified(String chatUid) {
        for (int i = 0; i < mChatsNotified.size(); i++) {
            if (mChatsNotified.get(i).equals(chatUid)) {
                return false;
            }
        }
        return true;
    }

    // For communication with ChatActivity
    class LocalBinder extends Binder {
        MessagesMonitorService getServiceInstance() {
            return MessagesMonitorService.this;
        }
    }

}
