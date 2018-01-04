package cn.com.codeteenager.permissionlibrary;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangshuaijie on 2017/12/4.
 *
 * @description 处理权限请求的工具类
 */

class PermissionUtils {
    private PermissionUtils() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 判断是不是6.0及其以上的版本
     *
     * @return
     */
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 执行成功的方法
     *
     * @param object
     * @param mRequestCode
     */
    public static void executeSuccessMethod(Object object, int mRequestCode) {
        //获取class的所有的方法
        Method[] methods = object.getClass().getDeclaredMethods();
        //遍历找了注解的方法
        for (Method method : methods) {
            PermissionSuccess permissionSuccess = method.getAnnotation(PermissionSuccess.class);
            if (permissionSuccess != null) {
                int methodCode = permissionSuccess.requestCode();
                //判断请求码是否一样
                if (methodCode == mRequestCode) {
                    //反射执行方法
                    executeMethod(object, method);
                }
            }
        }


    }

    /**
     * 反射执行该方法
     *
     * @param object
     * @param method
     */
    private static void executeMethod(Object object, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(object, new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     *
     * @param mObject
     * @param mRequestPermission
     * @return
     */
    public static List<String> getDeniedPermissions(Object mObject, String[] mRequestPermission) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String requestPermission : mRequestPermission) {
            if (ContextCompat.checkSelfPermission(getActivity(mObject), requestPermission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions;
    }

    /**
     * 获取Activity
     *
     * @param mObject
     * @return
     */
    public static Activity getActivity(Object mObject) {
        if (mObject instanceof Activity) {
            return (Activity) mObject;
        }
        if (mObject instanceof Fragment) {
            return ((Fragment) mObject).getActivity();
        }
        return null;
    }

    /**
     * 执行失败的方法
     *
     * @param object
     * @param requestCode
     */
    public static void executeFailedMethod(Object object, int requestCode) {
        //获取class的所有的方法
        Method[] methods = object.getClass().getDeclaredMethods();
        //遍历找了注解的方法
        for (Method method : methods) {
            PermissionFail permissionSuccess = method.getAnnotation(PermissionFail.class);
            if (permissionSuccess != null) {
                int methodCode = permissionSuccess.requestCode();
                //判断请求码是否一样
                if (methodCode == requestCode) {
                    //反射执行方法
                    executeMethod(object, method);
                }
            }
        }
    }
}
