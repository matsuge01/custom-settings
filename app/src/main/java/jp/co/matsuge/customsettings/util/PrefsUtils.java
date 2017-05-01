package jp.co.matsuge.customsettings.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsUtils {

    /**
     * アプリの情報に関するデータ.
     */
    public static final String APP_PREFS = "app_prefs";
    public static final String MONITOR_MODE = "monitor_mode";

    public static final String EARPHONE_SETTING_NAME = "earphone_setting_name";
    public static final String EARPHONE_SETTING_PACKAGENAME = "earphone_setting_packagename";


    private static SharedPreferences getSharedPreferencesPrivate(Context c, String f) {
        return c.getSharedPreferences(f, Context.MODE_PRIVATE);
    }

    public static void setPrefsParameter(Context c, String f, String n, boolean v) {
        getSharedPreferencesPrivate(c, f).edit().putBoolean(n, v).apply();
    }

    public static boolean getPrefsParameter(Context c, String f, String n, boolean d) {
        return getSharedPreferencesPrivate(c, f).getBoolean(n, d);
    }

    public static void setPrefsParameter(Context c, String f, String n, int v) {
        getSharedPreferencesPrivate(c, f).edit().putInt(n, v).apply();
    }
    public static int getPrefsParameter(Context c, String f, String n, int d) {
        return getSharedPreferencesPrivate(c, f).getInt(n, d);
    }

    public static void setPrefsParameter(Context c, String f, String n, String v) {
        getSharedPreferencesPrivate(c, f).edit().putString(n, v).apply();
    }
    public static String getPrefsParameter(Context c, String f, String n, String d) {
        return getSharedPreferencesPrivate(c, f).getString(n, d);
    }

    public static void setPrefsParameter(Context c, String f, String n, long v) {
        getSharedPreferencesPrivate(c, f).edit().putLong(n, v).apply();
    }

    public static void setPrefsParameter(Context c, String f, String n, float v) {
        getSharedPreferencesPrivate(c, f).edit().putFloat(n, v).apply();
    }
}
