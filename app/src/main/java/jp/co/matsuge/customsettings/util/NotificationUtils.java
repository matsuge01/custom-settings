package jp.co.matsuge.customsettings.util;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import jp.co.matsuge.customsettings.R;

public class NotificationUtils {

    public static final int MONITOR_NOTIFICATION_ID = 1;

    public static Notification getNotificationBuilder(
            Context context, @NonNull String title, String text, @NonNull int icon,
            int priority) {

        NotificationCompat.Builder n = new NotificationCompat.Builder(context);

        n.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        n.setContentTitle(title);
        n.setSmallIcon(icon);
        n.setPriority(priority);

        if (!TextUtils.isEmpty(text)) {
            n.setContentText(text);
        }

        return n.build();
    }
}
