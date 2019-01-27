package com.lhy.filemanager.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lhy.filemanager.R;
import com.lhy.filemanager.adapter.FileAdapter;
import com.lhy.filemanager.adapter.SearchResultAdapter;
import com.lhy.filemanager.helper.DataCleanManager;
import com.lhy.filemanager.helper.FileHelper;
import com.lhy.filemanager.helper.GetFilesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileFragment extends Fragment implements View.OnClickListener, FileAdapter.ItmeClickLisener {
    public static final int ACTION_ALERT_CREATE = 1;
    public static final int ACTION_ALERT_SEARCH = 2;
    public static final int ACTION_ALERT_CREATDIR = 3;
    private ProgressBar loading;
    private TextView current_files;
    private RelativeLayout scrollView;
    private ImageView close_btn;
    private String baseFile;
    private String creatfilePath;//新建文件的路径
    private AlertDialog dialog;
    private int alerttype = -1;
    private FileAdapter fileAdapter;
    private SearchResultAdapter searchResultAdapter;
    private List<Map<String, Object>> aList = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FileFragment.
     */
    public static FileFragment newInstance() {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseFile = GetFilesUtils.getInstance().getBasePath();
        final EditText et = new EditText(getActivity());

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity()).setTitle("请输入")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String key = et.getText().toString().trim();
                        if (!key.isEmpty()) {
                            dialog.dismiss();
                            if (alerttype == ACTION_ALERT_CREATE) {//创建文件
                                new AsyncTask<Void, Void, Object>() {

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                    }

                                    @Override
                                    protected Object doInBackground(Void... voids) {
                                        FileHelper.newFile(creatfilePath, key);

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Object obj) {
                                        super.onPostExecute(obj);
                                        try {
                                            current_files.setText(creatfilePath);
                                            loadFolderList(creatfilePath);
                                            Toast.makeText(getActivity(), "创建" + key + "成功！", Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.execute();
                            }
                            if (alerttype == ACTION_ALERT_CREATDIR) {
                                if (FileHelper.newDir(current_files.getText().toString(), key) == null) {
                                    Toast.makeText(getActivity(), "创建失败", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    loadFolderList(current_files.getText().toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (alerttype == ACTION_ALERT_SEARCH) {//搜索文件
                                new AsyncTask<Void, Void, String>() {

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        loading.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    protected String doInBackground(Void... voids) {
                                        try {
                                            return FileHelper.searchFile(baseFile,key);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            return "找不到文件!!";
                                        }
                                    }

                                    @Override
                                    protected void onPostExecute(String obj) {
                                        super.onPostExecute(obj);
                                        searchResultAdapter.setResult(obj);
                                        loading.setVisibility(View.GONE);
                                    }
                                }.execute();

                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "请输入内容", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.setCancelable(true);//设置按钮是否可以按返回键取消,false则不可以取消
        dialog = alert.create();// 创建对话框
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        view.findViewById(R.id.findfile).setOnClickListener(this);
        view.findViewById(R.id.searchfile).setOnClickListener(this);
        view.findViewById(R.id.cleanfile).setOnClickListener(this);
        view.findViewById(R.id.back_btn).setOnClickListener(this);
        view.findViewById(R.id.creatdir).setOnClickListener(this);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        current_files = (TextView) view.findViewById(R.id.current_files);
        scrollView = (RelativeLayout) view.findViewById(R.id.scrollView);
        close_btn = (ImageView) view.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fileAdapter = new FileAdapter(getActivity(), this);
        recyclerView.setAdapter(fileAdapter);

        RecyclerView search_result = (RecyclerView) view.findViewById(R.id.search_result);
        search_result.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchResultAdapter = new SearchResultAdapter(getActivity());
        search_result.setAdapter(searchResultAdapter);

        try {
            loadFolderList(baseFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadFolderList(String file) throws IOException {
        List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(file);
        if (list != null) {
            Collections.sort(list, GetFilesUtils.getInstance().defaultOrder());
            aList.clear();
            for (Map<String, Object> map : list) {
                String fileType = (String) map.get(GetFilesUtils.FILE_INFO_TYPE);
                Map<String, Object> gMap = new HashMap<String, Object>();
                if (map.get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(true)) {
                    gMap.put("fIsDir", true);
                    gMap.put("fImg", R.drawable.ic_folder_black_24dp);
                    gMap.put("fInfo", map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS) + "个文件夹和" +
                            map.get(GetFilesUtils.FILE_INFO_NUM_SONFILES) + "个文件");
                } else {
                    gMap.put("fIsDir", false);
                    if (fileType.equals("txt") || fileType.equals("text")) {
                        gMap.put("fImg", R.drawable.ic_attach_file_black_24dp);
                    } else if (fileType.equals("png") || fileType.equals("jpg") || fileType.equals("JPG")) {
                        gMap.put("fImg", R.drawable.ic_image_black_24dp);
                    } else if (fileType.equals("mp4")) {
                        gMap.put("fImg", R.drawable.ic_videocam_black_24dp);
                    } else if (fileType.equals("mp3")) {
                        gMap.put("fImg", R.drawable.ic_music_video_black_24dp);
                    } else {
                        gMap.put("fImg", R.drawable.ic_lightbulb_outline_black_24dp);
                    }
                    gMap.put("fInfo", "文件大小:" + GetFilesUtils.getInstance().getFileSize(map.get(GetFilesUtils.FILE_INFO_PATH).toString()));
                }
                gMap.put("fName", map.get(GetFilesUtils.FILE_INFO_NAME));
                gMap.put("fPath", map.get(GetFilesUtils.FILE_INFO_PATH));
                aList.add(gMap);
            }
        } else {
            aList.clear();
        }
        current_files.setText(file);
        fileAdapter.setaList(aList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findfile:
                try {
                    loadFolderList(baseFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.creatdir:
                alerttype = ACTION_ALERT_CREATDIR;
                dialog.show();
                break;
            case R.id.searchfile:
                alerttype = ACTION_ALERT_SEARCH;
                dialog.show();
                break;
            case R.id.cleanfile:
                DataCleanManager.clearAllCache(getActivity());
                break;
            case R.id.back_btn:
                onbackFile();
                break;
            case R.id.close_btn:
                scrollView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 返回上一级目录
     */
    private void onbackFile() {
        if (!current_files.getText().toString().isEmpty() && !current_files.getText().toString().equals(baseFile)) {
            try {
                String folder = GetFilesUtils.getInstance().getParentPath(current_files.getText().toString());
                if (folder == null) {
                    Toast.makeText(getActivity(), "刷新一下试试", Toast.LENGTH_SHORT).show();
                } else if (folder.equals(current_files.getText().toString())) {
                    Toast.makeText(getActivity(), "刷新一下试试", Toast.LENGTH_SHORT).show();
                } else {
                    loadFolderList(folder);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!current_files.getText().toString().isEmpty() && current_files.getText().toString().equals(baseFile)) {
            Toast.makeText(getActivity(), "已经到根目录了，不能在返回了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(String filepath) {
        current_files.setText(filepath);
        if (!filepath.isEmpty()) {
            try {
                loadFolderList(filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefash() {
        String refashpath;
        if (!current_files.getText().toString().isEmpty()) {
            refashpath = current_files.getText().toString();
        } else {
            refashpath = baseFile;
        }
        try {
            loadFolderList(refashpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建文件
     */
    @Override
    public void onCresteFile(String filePath) {
        this.creatfilePath = filePath;
        alerttype = ACTION_ALERT_CREATE;
        dialog.show();
    }
}
