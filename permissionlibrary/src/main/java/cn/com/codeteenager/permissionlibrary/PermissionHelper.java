package cn.com.codeteenager.permissionlibrary;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by jiangshuaijie on 2017/12/4.
 */

public class PermissionHelper {
    private Object mObject;
    private int mRequestCode;
    private String[] mRequestPermission;

    private PermissionHelper(Object object) {
        this.mObject = object;
    }

    public static void requestPermission(Activity activity, int requestCode, String[] permissions) {
        PermissionHelper.with(activity).requestCode(requestCode).requestPermissions(permissions).request();
    }

    //传Activity
    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }

    //传Fragment
    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    //添加请求码
    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    //添加请求权限数组
    public PermissionHelper requestPermissions(String... permissions) {
        this.mRequestPermission = permissions;
        return this;
    }

    //发起请求权限
    public void request() {
        //首先判断当前版本是不是6.0及以上
        if (!PermissionUtils.isOverMarshmallow()) {
            //如果不是，那么直接通过反射执行方法
            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
            return;
        }
        //如果是，那么需要判断权限是否授予
        //需要申请的权限中，获取没有授予的权限
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, mRequestPermission);
        //如果授予，则直接执行
        if (deniedPermissions.size() == 0) {
            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
        } else {
            //如果不授予，那么就申请权限
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject), deniedPermissions.toArray(new String[deniedPermissions.size()]), mRequestCode);
        }
    }

    /**
     * 处理申请权限的回调
     *
     * @param mObject
     * @param requestCode
     * @param permissions
     */
    public static void onRequestPermissionsResult(Object mObject, int requestCode, String[] permissions) {
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, permissions);
        if (deniedPermissions.size() == 0) {//权限用户都同意了
            PermissionUtils.executeSuccessMethod(mObject, requestCode);
        } else {
            PermissionUtils.executeFailedMethod(mObject, requestCode);
        }
    }
}
