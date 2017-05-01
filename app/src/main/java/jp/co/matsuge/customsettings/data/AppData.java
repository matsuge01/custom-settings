package jp.co.matsuge.customsettings.data;

import java.io.Serializable;

public class AppData implements Serializable {

    private CharSequence mName;
    private String mPackageName;

    public AppData(CharSequence name, String packageName) {
        mName = name;
        mPackageName = packageName;
    }

    public CharSequence getName() {
        return mName;
    }

    public String getPackageName() {
        return mPackageName;
    }

}
