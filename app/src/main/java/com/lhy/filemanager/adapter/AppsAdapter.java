package com.lhy.filemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhy.filemanager.R;
import com.lhy.filemanager.helper.AppHelper;
import com.lhy.filemanager.modle.AppInfo;
import com.lhy.filemanager.widget.SwipeMenuLayout;

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

    public void setAppInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final AppInfo appInfo = appInfos.get(position);
        viewHolder.appversion.setText(MessageFormat.format("版本号：{0}", appInfo.versionName));
        viewHolder.appname.setText(appInfo.appName);
        viewHolder.appicon.setImageDrawable(appInfo.appIcon);
        viewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppHelper.startAppWithPackageName(mContext, appInfo.packageName);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppHelper.removeApp(mContext, appInfo.packageName);
                viewHolder.swipeMenu.quickClose();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appInfos == null ? 0 : appInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appicon;
        public TextView appname;
        public TextView appversion;
        public View delete;
        public SwipeMenuLayout swipeMenu;
        public View item_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            appicon = (ImageView) itemView.findViewById(R.id.app_icon);
            appname = (TextView) itemView.findViewById(R.id.app_neme);
            appversion = (TextView) itemView.findViewById(R.id.app_version);
            delete = itemView.findViewById(R.id.delete);
            swipeMenu = (SwipeMenuLayout) itemView.findViewById(R.id.constraintLayout);
            item_layout =  itemView.findViewById(R.id.item_layout);
        }
    }
}
