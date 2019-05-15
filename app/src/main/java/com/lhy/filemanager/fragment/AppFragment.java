package com.lhy.filemanager.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lhy.filemanager.R;
import com.lhy.filemanager.adapter.AppsAdapter;
import com.lhy.filemanager.helper.AppHelper;
import com.lhy.filemanager.modle.AppInfo;

import java.util.List;

/**
 * 应用管理页面
 */
public class AppFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerview;
    private AppsAdapter appsAdapter;
    private ProgressBar progress;
    private List<AppInfo> appInfos;


    public static AppFragment newInstance(String param1, String param2) {
        AppFragment fragment = new AppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 页面初始化是被调用
     * @param inflater 用于布局文件加载
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_app, container, false);//加载布局文件
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);//加载布局文件里面的控件
        progress = (ProgressBar) view.findViewById(R.id.progress);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        initapps();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * 异步 加载手机内的所有应用
     */
    private void initapps() {
        AsyncTask<Void, Void, List<AppInfo>> asyncTask = new AsyncTask<Void, Void, List<AppInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);//准备执行异步加载 展示loading状态
            }

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                return AppHelper.getUserApps(mContext);//异步加载
            }

            @Override
            protected void onPostExecute(List<AppInfo> apps) {
                super.onPostExecute(apps);
                appInfos = apps;//获取到所有的应用
                progress.setVisibility(View.GONE);//隐藏进度条
                appsAdapter = new AppsAdapter(mContext, appInfos);//创建Adapter适配器
                recyclerview.setAdapter(appsAdapter);//设置数据到适配器展示数据
            }
        };
        asyncTask.execute();//开始执行
    }

    /**
     * app卸载成功的回调
     */
    public void removeScuess(String pake) {
        for (int i = 0; i < appInfos.size(); i++) {
            if (pake.equals(appInfos.get(i).packageName)) {
                appInfos.remove(i);
                appsAdapter.setAppInfos(appInfos);//更新页面
                break;
            }
        }
    }
}
