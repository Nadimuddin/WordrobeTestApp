package com.example.wardrobetestapp.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by Nadimuddin on 3/11/19.
 */
public class PermissionUtil {
    static PermissionUtil mPermissionUtils;
    private static Activity mActivity;

    public static PermissionUtil getInstance(Activity activity) {
        if(mPermissionUtils == null)
            mPermissionUtils = new PermissionUtil();
        mActivity = activity;
        return mPermissionUtils;
    }

    public boolean isPermissionGranted(String permission) {
        if(permission == null) {
            return false;
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int granted = ContextCompat.checkSelfPermission(mActivity, permission);
        return (granted == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission(Activity activity, int requestCode, String... permissions)
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if(permissions == null)
            return;

        for(String permission : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(mActivity, permission);
            if(checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{permission}, requestCode);
            }
        }
    }
}
