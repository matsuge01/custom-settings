package jp.co.matsuge.customsettings.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import jp.co.matsuge.customsettings.R;
import jp.co.matsuge.customsettings.service.SystemService;
import jp.co.matsuge.customsettings.util.AppUtils;
import jp.co.matsuge.customsettings.util.LogWrapper;
import jp.co.matsuge.customsettings.util.PrefsUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Switch mMonitorSwitch;
    private ImageView mMusicIcon;
    private LinearLayout mEarphoneItem;
    private LinearLayout mPocketWifiItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogWrapper.d(TAG + "#onCreate");

        setContentView(R.layout.activity_main);

        mEarphoneItem = (LinearLayout) findViewById(R.id.earphone_item);
        mPocketWifiItem = (LinearLayout) findViewById(R.id.pocket_wifi_item);

        mMonitorSwitch = (Switch) findViewById(R.id.monitor_mode);
        mMusicIcon = (ImageView) findViewById(R.id.music_icon);

        PrefsUtils.getSharedPreferences(getApplication(), PrefsUtils.APP_PREFS)
                .registerOnSharedPreferenceChangeListener(mPrefsListener);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = null;

                // リクエストオブジェクトを作って
                Request request = new Request.Builder()
                        .url("http://192.168.128.1/goform/goform_get_cmd_process?cmd=battery_value")
                        .header("Referer", "http://192.168.128.1/index.html")
                        .get()
                        .build();

                // クライアントオブジェクトを作って
                OkHttpClient client = new OkHttpClient();

                // リクエストして結果を受け取って
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 返す
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.d(TAG, result);
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogWrapper.d(TAG + "#onResume");

        updateUiMonitorSwitch();
        updateUiMusicIcon();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogWrapper.d(TAG + "#onDestroy");

        PrefsUtils.getSharedPreferences(getApplication(), PrefsUtils.APP_PREFS)
                .unregisterOnSharedPreferenceChangeListener(mPrefsListener);
    }


    private void updateUiMonitorSwitch() {
        boolean mode = PrefsUtils.getParameter(getApplicationContext(),
                PrefsUtils.APP_PREFS, PrefsUtils.MONITOR_MODE, false);

        LogWrapper.d(TAG + "#updateUiMonitorSwitch mode = " + mode);

        mMonitorSwitch.setChecked(mode);
        mMonitorSwitch.setOnCheckedChangeListener(mMonitorModeListener);

        updateUiItemArea(mode);
    }

    private void updateUiItemArea(boolean mode) {
        mEarphoneItem.setVisibility(mode ? View.VISIBLE : View.GONE);
        mPocketWifiItem.setVisibility(mode ? View.VISIBLE : View.GONE);
    }

    private void updateUiMusicIcon() {
        String packageName = PrefsUtils.getParameter(getApplication(),
                PrefsUtils.APP_PREFS, PrefsUtils.EARPHONE_SETTING_PACKAGE_NAME, null);

        LogWrapper.d(TAG + "#updateUiMusicIcon packageName = " + packageName);

        if (TextUtils.isEmpty(packageName)) {
            mMusicIcon.setBackgroundResource(R.drawable.ic_music_not_set);
            mMusicIcon.refreshDrawableState();

        } else {
            Drawable icon = AppUtils.getPackageNameIcon(getApplicationContext(), packageName);
            mMusicIcon.setBackground(icon);
            mMusicIcon.refreshDrawableState();
        }
    }

    public void onBtnClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.earphone_item:
                showMusicAppDialog();
                break;

            case R.id.pocket_wifi_item:
                break;

            default:
                break;
        }
    }

    private CompoundButton.OnCheckedChangeListener mMonitorModeListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
            LogWrapper.d(TAG + "#onCheckedChanged check = " + check);

            PrefsUtils.setParameter(getApplicationContext(),
                    PrefsUtils.APP_PREFS, PrefsUtils.MONITOR_MODE, check);

            updateUiItemArea(check);

            if (check) {
                AppUtils.startInAppService(getApplicationContext(), SystemService.class,
                        SystemService.ACTION_REGISTER_RECEIVER);

            } else {
                AppUtils.stopInAppService(getApplicationContext(), SystemService.class);
            }
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {

            LogWrapper.d(TAG + "#onSharedPreferenceChanged key = " + key);

            if (key.equals(PrefsUtils.EARPHONE_SETTING_PACKAGE_NAME)) {
                updateUiMusicIcon();
            }
        }
    };


    private void showMusicAppDialog() {
        List<ResolveInfo> appList = AppUtils.getTargetPackageList(
                getApplicationContext(), Intent.CATEGORY_APP_MUSIC);

        PackageManager pm = getPackageManager();
        mAdapter.clear();

        mAdapter.add(new MaterialSimpleListItem.Builder(this)
                .content("なし")
                .tag("")
                .icon(R.drawable.ic_music_not_set)
                .backgroundColor(Color.WHITE)
                .build());

        for (ResolveInfo app : appList) {
            CharSequence appName = app.activityInfo.loadLabel(pm);
            String packageName = app.activityInfo.packageName;
            Drawable icon = app.activityInfo.loadIcon(pm);

            mAdapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(appName)
                    .tag(packageName)
                    .icon(icon)
                    .backgroundColor(Color.WHITE)
                    .build());
        }

        new MaterialDialog.Builder(this)
                .title(R.string.earphone_app_select_dialog_title)
                .adapter(mAdapter, null)
                .show();
    }

    final MaterialSimpleListAdapter mAdapter = new MaterialSimpleListAdapter(
            new MaterialSimpleListAdapter.Callback() {

                @Override
                public void onMaterialListItemSelected(MaterialDialog dialog, int index,
                                                       MaterialSimpleListItem item) {
                    Context context = getApplicationContext();
                    String appName = (String) item.getContent();
                    String packageName = (String) item.getTag();

                    LogWrapper.d(TAG + "#onMaterialListItemSelected appName = " + appName);
                    LogWrapper.d(TAG + "#onMaterialListItemSelected packageName = " + packageName);

                    PrefsUtils.setParameter(context, PrefsUtils.APP_PREFS,
                            PrefsUtils.EARPHONE_SETTING_APP_NAME, appName);
                    PrefsUtils.setParameter(context, PrefsUtils.APP_PREFS,
                            PrefsUtils.EARPHONE_SETTING_PACKAGE_NAME, packageName);

                    String message = appName + getString(R.string.earphone_success_toast);
                    Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();

                    dialog.dismiss();
                }
            });
}
