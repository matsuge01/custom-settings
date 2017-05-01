package jp.co.matsuge.customsettings.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import jp.co.matsuge.customsettings.util.AppUtils;
import jp.co.matsuge.customsettings.util.LogWrapper;
import jp.co.matsuge.customsettings.util.PrefsUtils;

public class SystemReceiver extends BroadcastReceiver {

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
            String packageName = PrefsUtils.getPrefsParameter(context,
                    PrefsUtils.APP_PREFS, PrefsUtils.EARPHONE_SETTING_PACKAGENAME, null);

            if (!TextUtils.isEmpty(packageName)) {
                AppUtils.startApplication(context, packageName);
            }
        }
    }
}
