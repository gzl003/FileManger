package com.lhy.filemanager.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.lhy.filemanager.modle.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/24.
 * 应用的帮助类
 */
public class AppHelper {
    /**
     * 获取所有的app
     *
     * @param mContext
     * @return
     */
    public static List<AppInfo> getAppInfos(Context mContext) {

        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据
        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(packageManager);
            appList.add(tmpInfo);
        }
        return appList;
    }

    /**
     * 获取飞系统应用
     *
     * @param mContext
     * @return
     */
    public static List<AppInfo> getUserApps(Context mContext) {

        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(packageManager);
            //Only display the non-system app info
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);//如果非系统应用，则添加至appList
            }

        }
        return appList;
    }


}
