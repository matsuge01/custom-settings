package jp.co.matsuge.customsettings.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

import jp.co.matsuge.customsettings.util.AppUtils;
import jp.co.matsuge.customsettings.util.LogWrapper;

public class SystemReceiver extends BroadcastReceiver{

    private static final String TAG = "SystemReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogWrapper.d(TAG + "#onReceive");

        String action = intent.getAction();
        LogWrapper.d(TAG + "#onReceive action = " + action);

        switch (action) {
            case Intent.ACTION_HEADSET_PLUG:
                doHeadSetPlug(context, intent);
                break;
        }
    }

    private void doHeadSetPlug(Context context, Intent intent) {
        int state = intent.getIntExtra("state", -1);
        if (state > 0) {

            List<ResolveInfo> infos = AppUtils.getTargetPackageList(context, Intent.CATEGORY_APP_MUSIC);
            for (ResolveInfo info : infos) {
                LogWrapper.d("#doHeadSetPlug = " + info.activityInfo.packageName);
            }

            AppUtils.startApplication(context, "com.google.android.music");
        }
    }
}
