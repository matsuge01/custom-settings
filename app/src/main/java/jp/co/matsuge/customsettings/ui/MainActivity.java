package jp.co.matsuge.customsettings.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;

import java.util.List;

import es.dmoral.toasty.Toasty;
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

        boolean mode = PrefsUtils.getPrefsParameter(getApplicationContext(),
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
        if (view.getId() == R.id.earphone_item) {
            showMusicAppDialog();
        }
    }

    private CompoundButton.OnCheckedChangeListener mMonitorModeListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
            LogWrapper.d(TAG + "#onCheckedChanged check = " + check);
            PrefsUtils.setPrefsParameter(getApplicationContext(),
                    PrefsUtils.APP_PREFS, PrefsUtils.MONITOR_MODE, check);

            if (check) {
                AppUtils.startInAppService(getApplicationContext(), SystemService.class,
                        SystemService.ACTION_REGISTER_RECEIVER);

            } else {
                AppUtils.stopInAppService(getApplicationContext(), SystemService.class);
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
            CharSequence name = app.activityInfo.loadLabel(pm);
            String packageName = app.activityInfo.packageName;
            Drawable icon = app.activityInfo.loadIcon(pm);

            mAdapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(name)
                    .tag(packageName)
                    .icon(icon)
                    .backgroundColor(Color.WHITE)
                    .build());

            LogWrapper.d(TAG + "#onBtnClick name = " + name);
            LogWrapper.d(TAG + "#onBtnClick packageName = " + packageName);
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

                    LogWrapper.d(TAG + "#onMaterialListItemSelected index = " + index);
                    LogWrapper.d(TAG + "#onMaterialListItemSelected Content = " + item.getContent());
                    LogWrapper.d(TAG + "#onMaterialListItemSelected Tag = " + item.getTag());

                    Context context = getApplicationContext();
                    String aName = (String) item.getContent();
                    String pName = (String) item.getTag();

                    PrefsUtils.setPrefsParameter(context, PrefsUtils.APP_PREFS,
                            PrefsUtils.EARPHONE_SETTING_NAME, aName);
                    PrefsUtils.setPrefsParameter(context, PrefsUtils.APP_PREFS,
                            PrefsUtils.EARPHONE_SETTING_PACKAGENAME, pName);

                    String message = aName + getString(R.string.earphone_success_toast);
                    Toasty.success(context, message, Toast.LENGTH_LONG, true).show();

                    dialog.dismiss();
                }
            });
}
