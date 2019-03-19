package com.xiaozhanxiang.simplegridview.ui;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.xiaozhanxiang.simplegridview.callback.PermissionResultListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: dai
 * date:2019/3/17
 */
public class BaseActivity extends AppCompatActivity {

    private Map<Integer, PermissionResultListener> mPermissionResultListener;
    private HashMap<String, Boolean> mPermission;

    /**
     * 判断权限是否已同意
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), permission);
        return hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED;
    }



    /** 简化权限申请操作
     * @param permissions
     * @param requestCode
     * @return  false 没有去请求权限， true 请求了权限
     */
    public boolean requestPermissionsd(String[] permissions, int requestCode, PermissionResultListener permissionResultListener) {
        if (mPermissionResultListener == null) {
            mPermissionResultListener = new HashMap<>();
        }
        mPermission = new HashMap<>();
        if (permissionResultListener != null) {
            mPermissionResultListener.put(requestCode,permissionResultListener);
        }
        List<String> listPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermission(permissions[i])){
                listPermissions.add(permissions[i]);
            }else {
                mPermission.put(permissions[i],true);
            }
        }
        if (listPermissions.size() == 0){
            if (mPermissionResultListener.get(requestCode) != null) {
                mPermissionResultListener.get(requestCode).permissionResult(mPermission,true);
            }
            return false;
        }
        String[] strings = listPermissions.toArray(new String[listPermissions.size()]);
        ActivityCompat.requestPermissions(this, strings,requestCode);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermission == null) {
            mPermission = new HashMap<>();
        }
        boolean isAllAgree = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                mPermission.put(permissions[i],true);
            }else {
                mPermission.put(permissions[i],false);
                isAllAgree = false;
            }
        }
        if (mPermissionResultListener.get(requestCode) != null) {
            mPermissionResultListener.get(requestCode).permissionResult(mPermission,isAllAgree);
        }
    }

}
