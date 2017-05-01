package jp.co.matsuge.customsettings.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import jp.co.matsuge.customsettings.R;
import jp.co.matsuge.customsettings.service.SystemService;
import jp.co.matsuge.customsettings.util.AppUtils;
import jp.co.matsuge.customsettings.util.LogWrapper;
import jp.co.matsuge.customsettings.util.PrefsUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogWrapper.d(TAG + "#onCreate");

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogWrapper.d(TAG + "#onResume");

        Switch monitorMode = (Switch) findViewById(R.id.monitor_mode);

        boolean mode = PrefsUtils.getAppPrefsParameter(getApplicationContext(),
                PrefsUtils.APP_PREFS, PrefsUtils.MONITOR_MODE, false);
        LogWrapper.d(TAG + "#onResume mode = " + mode);

        monitorMode.setChecked(mode);
        monitorMode.setOnCheckedChangeListener(mMonitorModeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogWrapper.d(TAG + "#onDestroy");
    }

    public void onBtnClick(View view) {

        if (view.getId() == R.id.music_btn) {

        }
    }

    private CompoundButton.OnCheckedChangeListener mMonitorModeListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
            LogWrapper.d(TAG + "#onCheckedChanged check = " + check);
            PrefsUtils.setAppPrefsParameter(getApplicationContext(),
                    PrefsUtils.APP_PREFS, PrefsUtils.MONITOR_MODE, check);

            if (check) {
                AppUtils.startInAppService(getApplicationContext(), SystemService.class,
                        SystemService.ACTION_REGISTER_RECEIVER);

            } else {
                AppUtils.stopInAppService(getApplicationContext(), SystemService.class);
            }
        }
    };
}
