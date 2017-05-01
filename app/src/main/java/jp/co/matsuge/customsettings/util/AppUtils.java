package jp.co.matsuge.customsettings.util;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class AppUtils {

    private static final String TAG = "AppUtils";

    public static void startApplication(Context context, String name) {
        LogWrapper.d(TAG + "#startApplication name = " + name);

        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(name);

        try {
            context.startActivity(intent);
        } catch (RuntimeException e) {
            LogWrapper.e("#startApplication e = " + e);
        }
    }

    public static void startInAppService(Context context, Class className, String action) {
        LogWrapper.d(TAG + "#startInAppService className = " + className.getName());
        LogWrapper.d(TAG + "#startInAppService action = " + action);

        if (isServiceRunning(context, className.getName())) {
            LogWrapper.w(TAG + "#startInAppService already running");
            return;
        }

        Intent intent = new Intent(context, className);
        intent.setAction(action);

        try {
            context.startService(intent);
        } catch (RuntimeException e) {
            LogWrapper.e("#startSystemService e = " + e);
        }
    }

    public static void stopInAppService(Context context, Class className) {
        LogWrapper.d(TAG + "#stopInAppService className = " + className.getName());

        if (!isServiceRunning(context, className.getName())) {
            LogWrapper.w(TAG + "#stopInAppService already not running");
            return;
        }

        Intent intent = new Intent(context, className);

        try {
            context.stopService(intent);
        } catch (RuntimeException e) {
            LogWrapper.e("#startSystemService e = " + e);
        }
    }

    public static List<ResolveInfo> getTargetPackageList(Context context, String category) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(category);

        return pm.queryIntentActivities(intent, 0);
    }

    private static boolean isServiceRunning(Context context, String name) {
        boolean ret = false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<RunningServiceInfo> services = am.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo s : services) {
            if (s.service.getClassName().equals(name)) {
                ret = true;
                break;
            }
        }

        LogWrapper.d(TAG + "#isServiceRunning ret = " + ret);
        return ret;
    }
}
