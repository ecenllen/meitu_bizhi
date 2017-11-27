package com.duoduoapp.meitu.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by dongke.li on 2016/11/24.
 * 权限检查工具
 */

public class PermissionChecker {
    private final Context context;

    public PermissionChecker(Context context) {
        this.context = context.getApplicationContext();
    }
    public boolean lackPermissions(String ...permissions){
        for(String permission:permissions){
            if(lackPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private boolean lackPermission(String permission) {
        return ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_DENIED;
    }
}
