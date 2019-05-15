package com.lhy.filemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhy.filemanager.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2019/1/27.
 * 搜索结果的数据展示适配器
 */
public class SearchResultAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> reslist = new ArrayList<>();

    public SearchResultAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setResult(String result) {
        if (!result.isEmpty()) {
            reslist = Arrays.asList(result.split("\n"));
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHoder viewHoder = (ViewHoder) holder;
        if (position == 0) {
            viewHoder.search_icon.setVisibility(View.INVISIBLE);
            viewHoder.search_file.setGravity(Gravity.CENTER);
            viewHoder.search_file.setText(getItemCount() - 1 + "条 " + reslist.get(position));
        } else {
            viewHoder.search_icon.setVisibility(View.VISIBLE);
            viewHoder.search_file.setGravity(Gravity.LEFT);
            viewHoder.search_file.setText(reslist.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return reslist == null ? 0 : reslist.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        TextView search_file;
        ImageView search_icon;

        public ViewHoder(View itemView) {
            super(itemView);
            search_file = (TextView) itemView.findViewById(R.id.search_file);
            search_icon = (ImageView) itemView.findViewById(R.id.search_icon);
        }
    }
}
