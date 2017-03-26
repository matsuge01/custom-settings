package jp.co.matsuge.customsettings.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.co.matsuge.customsettings.R;
import jp.co.matsuge.customsettings.service.SystemService;
import jp.co.matsuge.customsettings.util.AppUtils;
import jp.co.matsuge.customsettings.util.LogWrapper;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogWrapper.d(TAG + "#onCreate");

        setContentView(R.layout.activity_main);

        AppUtils.startInAppService(getApplicationContext(),
                SystemService.class, SystemService.ACTION_REGISTER_RECEIVER);
    }
}
