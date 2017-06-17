package jp.co.matsuge.customsettings.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtils {

    /**
     * アプリの情報に関するデータ.
     */
    public static final String APP_PREFS = "app_prefs";
    public static final String MONITOR_MODE = "monitor_mode";

    public static final String EARPHONE_SETTING_APP_NAME = "earphone_setting_app_name";
    public static final String EARPHONE_SETTING_PACKAGE_NAME = "earphone_setting_package_name";

    public static final String SSID_NAME = "ssid_name";

    public static SharedPreferences getSharedPreferences(Context c, String f) {
        return c.getSharedPreferences(f, Context.MODE_PRIVATE);
    }

    public static void setParameter(Context c, String f, String n, boolean v) {
        getSharedPreferences(c, f).edit().putBoolean(n, v).commit();
    }

    public static boolean getParameter(Context c, String f, String n, boolean d) {
        return getSharedPreferences(c, f).getBoolean(n, d);
    }

    public static void setParameter(Context c, String f, String n, int v) {
        getSharedPreferences(c, f).edit().putInt(n, v).commit();
    }

    public static int getParameter(Context c, String f, String n, int d) {
        return getSharedPreferences(c, f).getInt(n, d);
    }

    public static void setParameter(Context c, String f, String n, String v) {
        getSharedPreferences(c, f).edit().putString(n, v).commit();
    }

    public static String getParameter(Context c, String f, String n, String d) {
        return getSharedPreferences(c, f).getString(n, d);
    }

    public static void setParameter(Context c, String f, String n, long v) {
        getSharedPreferences(c, f).edit().putLong(n, v).commit();
    }

    public static void setParameter(Context c, String f, String n, float v) {
        getSharedPreferences(c, f).edit().putFloat(n, v).commit();
    }
}
