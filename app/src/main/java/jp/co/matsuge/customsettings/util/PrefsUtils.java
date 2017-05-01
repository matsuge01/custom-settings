package jp.co.matsuge.customsettings.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtils {

    /**
     * アプリの情報に関するデータ.
     */
    public static final String APP_PREFS = "app_prefs";
    public static final String MONITOR_MODE = "monitor_mode";


    private static SharedPreferences getSharedPreferencesPrivate(Context c, String f) {
        return c.getSharedPreferences(f, Context.MODE_PRIVATE);
    }

    public static void setAppPrefsParameter(Context c, String f, String n, boolean v) {
        getSharedPreferencesPrivate(c, f).edit().putBoolean(n, v).apply();
    }

    public static boolean getAppPrefsParameter(Context c, String f, String n, boolean d) {
        return getSharedPreferencesPrivate(c, f).getBoolean(n, d);
    }

    public static void setAppPrefsParameter(Context c, String f, String n, int v) {
        getSharedPreferencesPrivate(c, f).edit().putInt(n, v).apply();
    }

    public static void setAppPrefsParameter(Context c, String f, String n, String v) {
        getSharedPreferencesPrivate(c, f).edit().putString(n, v).apply();
    }

    public static void setAppPrefsParameter(Context c, String f, String n, long v) {
        getSharedPreferencesPrivate(c, f).edit().putLong(n, v).apply();
    }

    public static void setAppPrefsParameter(Context c, String f, String n, float v) {
        getSharedPreferencesPrivate(c, f).edit().putFloat(n, v).apply();
    }
}
