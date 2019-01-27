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
 * A simple {@link Fragment} subclass.
 */
public class AppFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerview;
    private AppsAdapter appsAdapter;
    private ProgressBar progress;
    private List<AppInfo> appInfos;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppFragment newInstance(String param1, String param2) {
        AppFragment fragment = new AppFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app, container, false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
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

    private void initapps() {
        AsyncTask<Void, Void, List<AppInfo>> asyncTask = new AsyncTask<Void, Void, List<AppInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                return AppHelper.getUserApps(mContext);
            }

            @Override
            protected void onPostExecute(List<AppInfo> apps) {
                super.onPostExecute(apps);
                appInfos = apps;
                progress.setVisibility(View.GONE);
                appsAdapter = new AppsAdapter(mContext, appInfos);
                recyclerview.setAdapter(appsAdapter);
            }
        };
        asyncTask.execute();
    }

    /**
     * app卸载成功的回调
     */
    public void removeScuess(String pake) {
        for (int i = 0; i < appInfos.size(); i++) {
            if (pake.equals(appInfos.get(i).packageName)) {
                appInfos.remove(i);
                appsAdapter.setAppInfos(appInfos);
                break;
            }
        }
    }
}
