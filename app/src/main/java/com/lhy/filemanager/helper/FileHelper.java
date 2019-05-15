package com.lhy.filemanager.helper;


import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2019/1/26.
 * <p>
 * 文件管理帮助类
 * https://www.jianshu.com/p/8dc08476261a
 */
public class FileHelper {
    /**
     * 创建文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File newFile(String filePath, String fileName) {
        if (filePath == null || filePath.length() == 0
                || fileName == null || fileName.length() == 0) {
            return null;
        }
        try {
            //判断目录是否存在，如果不存在，递归创建目录
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //组织文件路径
            StringBuilder sbFile = new StringBuilder(filePath);
            if (!filePath.endsWith("/")) {
                sbFile.append("/");
            }
            sbFile.append(fileName);

            //创建文件并返回文件对象
            File file = new File(sbFile.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File newDir(String filePath, String fileName) {
        if (filePath == null || filePath.length() == 0
                || fileName == null || fileName.length() == 0) {
            return null;
        }
        try {
            //判断目录是否存在，如果不存在，递归创建目录
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //组织文件路径
            StringBuilder sbFile = new StringBuilder(filePath);
            if (!filePath.endsWith("/")) {
                sbFile.append("/");
            }
            sbFile.append(fileName);

            //创建文件并返回文件对象
            File file = new File(sbFile.toString());
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定目录
     *
     * @param filePath
     */
    public static void removeFile(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                removeFile(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void removeFile(File file) {
        //如果是文件直接删除
        if (file.isFile()) {
            file.delete();
            return;
        }
        //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                removeFile(f);
            }
            file.delete();
        }
    }


    /**
     * 删除文件或者文件夹
     *
     * @param filePath
     */
    public static void removeFile(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
//            Toast.makeText(context, "删除文件失败:" + filePath + "不存在！", Toast.LENGTH_SHORT).show();

        } else {
            if (file.isFile()) {
                deleteSingleFile(context, filePath);
            } else {
                deleteDirectory(context, filePath);
            }

        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(Context context, String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
//                Toast.makeText(context, "删除单个文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
//            Toast.makeText(context, "删除单个文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(Context context, String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
//            Toast.makeText(context, "删除目录失败：" + filePath + "不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(context, file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(context, file.getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
//            Toast.makeText(context, "删除目录失败！", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
//            Toast.makeText(context, "删除" + filePath + "成功！", Toast.LENGTH_SHORT).show();
            return true;
        } else {
//            Toast.makeText(context, "删除目录：" + filePath + "失败！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 获取文件夹的大小
     */
    public static float getFileSize(String filePath) {
        float size = 0;
        if (filePath == null || filePath.length() == 0) {
            return 0;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                size = 0;
                return getSize(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取文件的大小
     *
     * @param file
     * @return
     */
    public static float getSize(File file) {
        float size = 0;
        try {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                for (File f : children) {
                    size += getSize(f);
                }
                return size;
            }
            //如果是文件则直接返回其大小
            else {
                return (float) file.length();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return size;
    }

    /**
     * 拷贝文件
     *
     * @param filePath
     * @param newDirPath
     */
    public static void copyFile(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            //判断目录是否存在，如果不存在，则创建
            File newDir = new File(newDirPath);
            if (!newDir.exists()) {
                newDir.mkdirs();
            }
            //创建目标文件
            File newFile = newFile(newDirPath, file.getName());
            InputStream is = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[4096];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝目录
     *
     * @param dirPath
     * @param newDirPath
     */
    public static void copyDir(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            File file = new File(dirPath);
            if (!file.exists() && !file.isDirectory()) {
                return;
            }
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return;
            }
            File newFile = new File(newDirPath);
            newFile.mkdirs();
            for (File fileTemp : childFile) {
                if (fileTemp.isDirectory()) {
                    copyDir(fileTemp.getPath(), newDirPath + "/" + fileTemp.getName());
                } else {
                    copyFile(fileTemp.getPath(), newDirPath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 剪切文件
     *
     * @param filePath
     * @param newDirPath
     */
    public static void moveFile(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            //拷贝文件
            copyFile(filePath, newDirPath);
            //删除原文件
            removeFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 剪切目录
     *
     * @param dirPath
     * @param newDirPath
     */
    public static void moveDir(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            //拷贝目录
            copyDir(dirPath, newDirPath);
            //删除目录
            removeFile(dirPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索文件
     *
     * @param keyword
     * @retur
     */
    public static String searchFile(String searchpath, String keyword) throws IOException {
        StringBuilder result = new StringBuilder();
        File[] files = new File(searchpath).listFiles();

        if (files.length > 0) {
            result.append("搜索结果如下").append("\n");
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory() && !files[i].isHidden()) {
                    File[] childfiles = new File(files[i].getPath()).listFiles();
                    if (childfiles.length > 0) {
                        for (int j = 0; j < childfiles.length; j++) {
                            if (childfiles[j].isDirectory() && !childfiles[j].isHidden()) {
                                File[] child = new File(childfiles[j].getPath()).listFiles();
                                if (child.length > 0) {
                                    for (int k = 0; k < child.length; k++) {
                                        if (child[k].getName().contains(keyword)) {
                                            result.append(child[k].getPath()).append("\n");
                                        }
                                    }
                                }
                            } else {
                                if (childfiles[j].getName().contains(keyword)) {
                                    result.append(childfiles[j].getPath()).append("\n");
                                }
                            }
                        }
                    }
                } else {
                    if (files[i].getName().contains(keyword)) {
                        result.append(files[i].getPath()).append("\n");
                    }
                }
            }
            if (result.toString().equals("")) {
                result = new StringBuilder("找不到文件!!");
            }
        }

        return result.toString();
    }


}
