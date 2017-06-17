package jp.co.matsuge.customsettings.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
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

            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                doNetworkStateChange(context, intent);
                break;

            default:
                break;
        }
    }

    private void doHeadSetPlug(Context context, Intent intent) {
        int state = intent.getIntExtra("state", -1);
        if (state > 0) {
            String packageName = PrefsUtils.getParameter(context,
                    PrefsUtils.APP_PREFS, PrefsUtils.EARPHONE_SETTING_PACKAGE_NAME, null);

            if (!TextUtils.isEmpty(packageName)) {
                AppUtils.startApplication(context, packageName);
            }
        }
    }

    private void doNetworkStateChange(Context context, Intent intent) {
        NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        NetworkInfo.State state = netInfo.getState();

        String name = netInfo.getExtraInfo();

        LogWrapper.d(TAG + "#onReceive doNetworkStateChange state = " + netInfo.toString());
        LogWrapper.d(TAG + "#onReceive doNetworkStateChange name = " + name);


    }
}
