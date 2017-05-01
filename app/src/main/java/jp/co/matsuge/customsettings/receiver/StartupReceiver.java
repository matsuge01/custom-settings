package jp.co.matsuge.customsettings.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import jp.co.matsuge.customsettings.service.SystemService;
import jp.co.matsuge.customsettings.util.AppUtils;
import jp.co.matsuge.customsettings.util.LogWrapper;
import jp.co.matsuge.customsettings.util.PrefsUtils;

public class StartupReceiver extends BroadcastReceiver {

    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogWrapper.d(TAG + "#onReceive");

        String action = intent.getAction();
        LogWrapper.d(TAG + "#onReceive action = " + action);

        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
            case Intent.ACTION_MY_PACKAGE_REPLACED:
                boolean mode = PrefsUtils.getPrefsParameter(context,
                        PrefsUtils.APP_PREFS, PrefsUtils.MONITOR_MODE, false);

                LogWrapper.d(TAG + "#onReceive mode = " + mode);
                if (mode) {
                    AppUtils.startInAppService(context, SystemService.class,
                            SystemService.ACTION_REGISTER_RECEIVER);
                }
                break;

            default:
                break;
        }
    }
}
