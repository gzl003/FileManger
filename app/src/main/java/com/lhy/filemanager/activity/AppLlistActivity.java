package com.lhy.filemanager.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.lhy.filemanager.R;
import com.lhy.filemanager.adapter.AppsAdapter;
import com.lhy.filemanager.helper.AppHelper;
import com.lhy.filemanager.modle.AppInfo;

import java.util.List;

public class AppLlistActivity extends AppCompatActivity {

    private RecyclerView recyclerview;
    private AppsAdapter appsAdapter;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_llist);
        recyclerview = findViewById(R.id.recyclerview);
        progress = findViewById(R.id.progress);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        initapps();
        setTitle("应用列表");

    }

    private void initapps() {
        AsyncTask<Void,Void,List<AppInfo>> asyncTask = new AsyncTask<Void,Void,List<AppInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                return AppHelper.getUserApps(AppLlistActivity.this);
            }

            @Override
            protected void onPostExecute(List<AppInfo> appInfos) {
                super.onPostExecute(appInfos);
                progress.setVisibility(View.GONE);
                appsAdapter = new AppsAdapter(AppLlistActivity.this, appInfos);
                recyclerview.setAdapter(appsAdapter);
            }
        };
        asyncTask.execute();
    }
}
