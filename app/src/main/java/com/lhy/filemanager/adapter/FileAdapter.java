package com.lhy.filemanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lhy.filemanager.R;
import com.lhy.filemanager.helper.AppHelper;
import com.lhy.filemanager.helper.FileHelper;
import com.lhy.filemanager.modle.AppInfo;
import com.lhy.filemanager.widget.EasyPopup;
import com.lhy.filemanager.widget.SwipeMenuLayout;
import com.lhy.filemanager.widget.XGravity;
import com.lhy.filemanager.widget.YGravity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/26.
 * 文件夹的adapter
 */
public class FileAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Map<String, Object>> aList;
    private ItmeClickLisener ClickLisener;
    private ProgressBar progress;
    private EasyPopup mCirclePop;
    private Tempfile tempfile;

    public String filePath;//原路径
    public String newDirPath;//目标路径


    public FileAdapter(final Context mContext, ItmeClickLisener itmeClickLisener) {
        this.mContext = mContext;
        this.ClickLisener = itmeClickLisener;
        progress = new ProgressBar(mContext);
        mCirclePop = EasyPopup.create()
                .setContentView(mContext, R.layout.pop_menu_layout)
                .setAnimationStyle(R.style.TopPopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                .apply();
        mCirclePop.findViewById(R.id.copy_tv).setOnClickListener(new View.OnClickListener() {
            /**
             * 复制
             * @param v
             */
            @Override
            public void onClick(View v) {
                tempfile.movieType = Tempfile.TYPE_COPY;
                tempfile.filePath = filePath;
                Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
                mCirclePop.dismiss();
            }
        });
        mCirclePop.findViewById(R.id.cutting_tv).setOnClickListener(new View.OnClickListener() {
            /**
             * 剪切
             */
            @Override
            public void onClick(View v) {
                tempfile.movieType = Tempfile.TYPE_CUTTING;
                tempfile.filePath = filePath;
                Toast.makeText(mContext, "已剪切", Toast.LENGTH_SHORT).show();
                mCirclePop.dismiss();
            }
        });
        mCirclePop.findViewById(R.id.paste_tv).setOnClickListener(new View.OnClickListener() {
            /**
             * 粘贴
             */
            @Override
            public void onClick(View v) {
                if (tempfile.movieType == Tempfile.TYPE_COPY) {//复制
                    tempfile.newDirPath = newDirPath;
                    new AsyncTask<Void, Void, Object>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected Object doInBackground(Void... voids) {
                            FileHelper.copyDir(tempfile.filePath, tempfile.newDirPath);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object obj) {
                            super.onPostExecute(obj);
                            progress.setVisibility(View.GONE);
                            if (ClickLisener != null) {
                                ClickLisener.onRefash();
                            }
                            Toast.makeText(mContext, "粘贴成功", Toast.LENGTH_SHORT).show();
                        }
                    }.execute();
                }
                if (tempfile.movieType == Tempfile.TYPE_CUTTING) {//剪切
                    tempfile.newDirPath = newDirPath;
                    new AsyncTask<Void, Void, Object>() {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progress.setVisibility(View.VISIBLE);
                        }

                        @Override
                        protected Object doInBackground(Void... voids) {
                            FileHelper.moveDir(tempfile.filePath, tempfile.newDirPath);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object obj) {
                            super.onPostExecute(obj);
                            progress.setVisibility(View.GONE);
                            if (ClickLisener != null) {
                                ClickLisener.onRefash();
                            }
                            Toast.makeText(mContext, "粘贴成功", Toast.LENGTH_SHORT).show();
                        }
                    }.execute();
                }
                mCirclePop.dismiss();
            }
        });
    }

    public void setaList(List<Map<String, Object>> aList) {
        this.aList = aList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoder(LayoutInflater.from(mContext).inflate(R.layout.adapter_file_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHoder viewHoder = (ViewHoder) holder;
        viewHoder.filesname.setText(aList.get(position).get("fName").toString());
        viewHoder.filescount.setText(aList.get(position).get("fInfo").toString());
        viewHoder.folter_img.setImageResource((Integer) aList.get(position).get("fImg"));
        viewHoder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickLisener != null && Boolean.parseBoolean(aList.get(position).get("fIsDir").toString())) {
                    ClickLisener.onItemClick(aList.get(position).get("fPath").toString());
                }
            }
        });
        viewHoder.item_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (tempfile == null) {
                    tempfile = new Tempfile();
                }
                newDirPath = aList.get(position).get("fPath").toString();
                filePath = aList.get(position).get("fPath").toString();
                /**
                 * 相对anchor view显示，适用 宽高不为match_parent
                 *
                 * @param anchor
                 * @param yGravity  垂直方向的对齐方式
                 * @param xGravity  水平方向的对齐方式
                 * @param x            水平方向的偏移
                 * @param y            垂直方向的偏移
                 */
                mCirclePop.showAtAnchorView(v, YGravity.ABOVE, XGravity.CENTER, 0, 0);

                return false;
            }
        });
        viewHoder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClickLisener != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("有的文件不能删除哦");
                    builder.setMessage("你确定要删除" + aList.get(position).get("fPath").toString() + "吗？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewHoder.swipeMenu.quickClose();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewHoder.swipeMenu.quickClose();
                            new AsyncTask<Void, Void, Object>() {

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progress.setVisibility(View.VISIBLE);
                                }

                                @Override
                                protected Object doInBackground(Void... voids) {
                                    if (Boolean.parseBoolean(aList.get(position).get("fIsDir").toString())) {
                                        FileHelper.removeFile(mContext, aList.get(position).get("fPath").toString());//删除文件夹
                                    } else {
                                        FileHelper.removeFile(mContext, aList.get(position).get("fPath").toString());//删除文件
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Object obj) {
                                    super.onPostExecute(obj);
                                    progress.setVisibility(View.GONE);
                                    if (ClickLisener != null) {
                                        ClickLisener.onRefash();
                                    }
                                    Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                                }
                            }.execute();
                        }
                    });
                    builder.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return aList == null ? 0 : aList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView filesname;
        TextView filescount;
        ImageView folter_img;
        public View delete;
        public SwipeMenuLayout swipeMenu;
        public View item_layout;

        public ViewHoder(View itemView) {
            super(itemView);
            filesname = (TextView) itemView.findViewById(R.id.filesname);
            filescount = (TextView) itemView.findViewById(R.id.filescount);
            folter_img = (ImageView) itemView.findViewById(R.id.folter_img);
            delete = itemView.findViewById(R.id.delete);
            swipeMenu = (SwipeMenuLayout) itemView.findViewById(R.id.constraintLayout);
            item_layout = itemView.findViewById(R.id.item_layout);

        }
    }

    public interface ItmeClickLisener {
        void onItemClick(String filepath);

        void onRefash();
    }

    public class Tempfile {
        public static final int TYPE_COPY = 1;
        public static final int TYPE_CUTTING = 2;

        public String filePath;//原路径
        public String newDirPath;//目标路径
        public int movieType = -1;//用于区分是复制还是剪切
    }
}
