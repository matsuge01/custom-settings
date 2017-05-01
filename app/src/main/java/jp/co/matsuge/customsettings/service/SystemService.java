package jp.co.matsuge.customsettings.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import jp.co.matsuge.customsettings.R;
import jp.co.matsuge.customsettings.receiver.SystemReceiver;
import jp.co.matsuge.customsettings.util.LogWrapper;
import jp.co.matsuge.customsettings.util.NotificationUtils;

public class SystemService extends Service {

    private static final String TAG = "SystemService";
    private SystemReceiver mSystemReceiver = null;

    public static final String ACTION_REGISTER_RECEIVER
            = "jp.co.matsuge.customsettings.action.ACTION_REGISTER_RECEIVER";

    public static final String ACTION_UNREGISTER_RECEIVER
            = "jp.co.matsuge.customsettings.action.ACTION_UNREGISTER_RECEIVER";


    @Override
    public void onCreate() {
        super.onCreate();
        LogWrapper.d(TAG + "#onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogWrapper.d(TAG + "#onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogWrapper.d(TAG + "#onStartCommand");

        if (intent == null) {
            return START_STICKY;
        }

        String action = intent.getAction();
        LogWrapper.d(TAG + "#onStartCommand action = " + action);

        switch (action) {
            case ACTION_REGISTER_RECEIVER:
                registerReceiver(getApplicationContext());
                break;

            case ACTION_UNREGISTER_RECEIVER:
                unregisterReceiver(getApplicationContext());
                break;

            default:
                break;
        }

        return START_STICKY;
    }

    private void registerReceiver(Context context) {
        if (mSystemReceiver == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_HEADSET_PLUG);

            mSystemReceiver = new SystemReceiver();
            context.registerReceiver(mSystemReceiver, filter);

            Notification n = NotificationUtils.getNotificationBuilder(
                    context,
                    getString(R.string.notification_monitor_title),
                    getString(R.string.notification_monitor_text),
                    android.R.drawable.stat_sys_headset);

            startForeground(NotificationUtils.MONITOR_NOTIFICATION_ID, n);
        }
    }

    private void unregisterReceiver(Context context) {
        if (mSystemReceiver != null) {
            context.unregisterReceiver(mSystemReceiver);
            mSystemReceiver = null;
        }
    }


}
