package jp.co.matsuge.customsettings.util;

import android.util.Log;

import jp.co.matsuge.customsettings.BuildConfig;
import jp.co.matsuge.customsettings.common.Common;

public class LogWrapper {

    public static void d(String m) {
        if (BuildConfig.DEBUG) {
            Log.d(Common.TAG, m);
        }
    }

    public static void e(String m) {
        if (BuildConfig.DEBUG) {
            Log.e(Common.TAG, m);
        }
    }
}
