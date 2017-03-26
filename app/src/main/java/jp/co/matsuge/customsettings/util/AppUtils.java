package jp.co.matsuge.customsettings.util;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

public class AppUtils {

    public static void startApplication(Context context, String name) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(name);

        try {
            context.startActivity(intent);
        } catch (RuntimeException e) {
            LogWrapper.e("#startApplication e = " + e);
        }
    }

    public static void startInAppService(Context context, Class className, String action) {
        Intent intent = new Intent(context, className);
        intent.setAction(action);

        try {
            context.startService(intent);
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
}