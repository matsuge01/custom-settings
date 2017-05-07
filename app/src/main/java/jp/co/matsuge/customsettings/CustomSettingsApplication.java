package jp.co.matsuge.customsettings;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import jp.co.matsuge.customsettings.util.LogWrapper;

public class CustomSettingsApplication extends Application {

    private static final String TAG = "CustomSettingsApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            LogWrapper.d(TAG + "#onCreate not LeakCanary");
            return;
        }

        LeakCanary.install(this);
        LogWrapper.d(TAG + "#onCreate LeakCanary");
    }
}
