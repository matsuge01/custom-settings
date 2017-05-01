package jp.co.matsuge.customsettings.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.matsuge.customsettings.R;
import jp.co.matsuge.customsettings.data.AppData;
import jp.co.matsuge.customsettings.util.LogWrapper;
import jp.co.matsuge.customsettings.util.PrefsUtils;

public class AppListDialog extends DialogFragment {

    private static final String TAG = "AppListDialog";

    private static final String KEY_MUSIC_APP_DATA = "key_music_app_data";

    public static void showAppDialog(Activity activity, ArrayList<AppData> data) {
        final Bundle args = new Bundle();
        args.putSerializable(KEY_MUSIC_APP_DATA, data);

        final AppListDialog dialogFragment = new AppListDialog();
        dialogFragment.setArguments(args);
        dialogFragment.show(activity.getFragmentManager(), null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments() == null ? Bundle.EMPTY : getArguments();
        if (args == null) {
            return null;
        }

        final ArrayList<AppData> data = (ArrayList<AppData>) args.getSerializable(KEY_MUSIC_APP_DATA);
        CustomAdapter adapter = new CustomAdapter(getContext(), 0, data);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LogWrapper.d(TAG + "#onCreateDialog onClick which = " + which);

                AppData d = data.get(which);
                CharSequence name = d.getName();
                String packageName = d.getPackageName();

                LogWrapper.d(TAG + "#onCreateDialog onClick name = " + name);
                LogWrapper.d(TAG + "#onCreateDialog onClick packageName = " + packageName);

                PrefsUtils.setPrefsParameter(getContext(),
                        PrefsUtils.APP_PREFS, PrefsUtils.EARPHONE_SETTING_NAME, (String) name);
                PrefsUtils.setPrefsParameter(getContext(),
                        PrefsUtils.APP_PREFS, PrefsUtils.EARPHONE_SETTING_PACKAGENAME, packageName);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public class CustomAdapter extends ArrayAdapter<AppData> {
        private LayoutInflater inflater;

        public CustomAdapter(Context context, int resource, List<AppData> objects) {
            super(context, resource, objects);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            AppData item = (AppData) getItem(position);
            if (v == null) {
                v = inflater.inflate(R.layout.app_list_data, null);
            }

            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            //icon.setBackground(item.getIcon());

            TextView name = (TextView) v.findViewById(R.id.app_name);
            name.setText(item.getName());

            return v;
        }
    }

}
