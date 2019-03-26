package com.lhy.filemanager.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.lhy.filemanager.R;
import com.lhy.filemanager.adapter.SectionsPagerAdapter;
import com.lhy.filemanager.fragment.AppFragment;
import com.lhy.filemanager.fragment.FileFragment;
import com.lhy.filemanager.widget.HackyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    private HackyViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private List<Integer> tabids = new ArrayList<Integer>();
    private List<String> tabname = new ArrayList<String>();
    private static AppFragment appFragment;
    private FileFragment fileFragment;
    private AppRemoveReceiver appRemoveReceiver;
    private DrawerLayout drawerLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0, false);
                    drawerLayout.closeDrawers();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //打开手势滑动
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1, false);
                    drawerLayout.closeDrawers();
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //禁止手势滑动
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                Toast.makeText(MainActivity.this, "侧滑栏已打开", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
//                Toast.makeText(MainActivity.this, "侧滑栏已关闭", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        findViewById(R.id.findfile).setOnClickListener(this);
        findViewById(R.id.searchfile).setOnClickListener(this);
        findViewById(R.id.cleanfile).setOnClickListener(this);
        findViewById(R.id.creatdir).setOnClickListener(this);

        tabids.add(R.id.navigation_home);
        tabids.add(R.id.navigation_dashboard);
        tabname.add("文件管理");
        tabname.add("应用管理");
        fileFragment = FileFragment.newInstance();
        appFragment = AppFragment.newInstance("", "");
        fragments.add(fileFragment);
        fragments.add(appFragment);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (HackyViewPager) findViewById(R.id.viewpager);
        mViewPager.setScrollEnable(false);//取消左右滑动
        mViewPager.setAdapter(mSectionsPagerAdapter);

        appRemoveReceiver = new AppRemoveReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        registerReceiver(appRemoveReceiver, intentFilter);
        onCallPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(appRemoveReceiver);
    }

    public void onCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//判断当前系统的SDK版本是否大于23
            //如果当前申请的权限没有授权
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findfile://刷新/目录
                fileFragment.refreshFile();
                break;
            case R.id.creatdir://创建文件夹
                fileFragment.creatFileDir();
                break;
            case R.id.searchfile://搜索文件
                fileFragment.searchFile();
                break;
            case R.id.cleanfile://清除缓存
                fileFragment.cleanfile();
                break;
        }
        drawerLayout.closeDrawers();
    }

    public static class AppRemoveReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), Intent.ACTION_PACKAGE_REMOVED)) {
                if (intent.getData() != null && appFragment != null) {
                    String packageName = intent.getData().getSchemeSpecificPart();
                    Log.e("AppRemoveReceiver", packageName);
                    appFragment.removeScuess(packageName);
                }
            }
        }
    }

}
