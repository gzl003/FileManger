package com.lhy.filemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhy.filemanager.R;
import com.lhy.filemanager.modle.AppInfo;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by Administrator on 2019/1/25.
 */

public class AppsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AppInfo> appInfos;

    public AppsAdapter(Context mContext, List<AppInfo> appInfos) {
        this.mContext = mContext;
        this.appInfos = appInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        AppInfo appInfo = appInfos.get(position);
        viewHolder.appversion.setText(MessageFormat.format("{0}/{1}", appInfo.versionName, appInfo.versionCode));
        viewHolder.appname.setText(appInfo.appName);
        viewHolder.appicon.setImageDrawable(appInfo.appIcon);
    }

    @Override
    public int getItemCount() {
        return appInfos == null ? 0 : appInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appicon;
        public TextView appname;
        public TextView appversion;

        public ViewHolder(View itemView) {
            super(itemView);
            appicon = itemView.findViewById(R.id.app_icon);
            appname = itemView.findViewById(R.id.app_neme);
            appversion = itemView.findViewById(R.id.app_version);
        }
    }
}
