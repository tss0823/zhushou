/*
 * Copyright 2010-2011 ESunny.com All right reserved. This software is the confidential and proprietary information of
 * ESunny.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with ESunny.com.
 */
package com.yuntao.zhushou.common.utils;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 类FileUtils.java的实现描述：TODO 类实现描述
 * 
 * @author shengshang.tang 2014年3月28日 上午10:15:19
 */
public class ZipUtils {
    /**
     * 文件解压
     * 
     * @param zipFilePath 解压文件名
     * @param targetPath 目标文件目录
     */
    public static void unzipFile(String zipFilePath, String targetPath) {
        try {
            File zipFile = new File(zipFilePath);
            InputStream is = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                String zipPath = entry.getName();
                try {

                    if (entry.isDirectory()) {
                        File zipFolder = new File(targetPath + File.separator + zipPath);
                        if (!zipFolder.exists()) {
                            zipFolder.mkdirs();
                        }
                    } else {
                        File file = new File(targetPath + File.separator + zipPath);
                        if (!file.exists()) {
                            File pathDir = file.getParentFile();
                            pathDir.mkdirs();
                            file.createNewFile();
                        }

                        FileOutputStream fos = new FileOutputStream(file);
                        int bread;
                        while ((bread = zis.read()) != -1) {
                            fos.write(bread);
                        }
                        fos.close();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            zis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzipFile(byte[] content, String targetPath) {
        try {
            InputStream is = new ByteArrayInputStream(content);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                String zipPath = entry.getName();
                try {

                    if (entry.isDirectory()) {
                        File zipFolder = new File(targetPath + File.separator + zipPath);
                        if (!zipFolder.exists()) {
                            zipFolder.mkdirs();
                        }
                    } else {
                        File file = new File(targetPath + File.separator + zipPath);
                        if (!file.exists()) {
                            File pathDir = file.getParentFile();
                            pathDir.mkdirs();
                            file.createNewFile();
                        }

                        FileOutputStream fos = new FileOutputStream(file);
                        int bread;
                        while ((bread = zis.read()) != -1) {
                            fos.write(bread);
                        }
                        fos.close();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            zis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件压缩
     * 
     * @param srcFilePath 源文件目录
     * @param zipFilePath 压缩文件名
     * @param filterList 过滤文件名
     */
    public static void zipFile(String srcFilePath, String zipFilePath, String[] filterList) {
        if (StringUtils.isEmpty(srcFilePath) || StringUtils.isEmpty(zipFilePath)) {
            throw new IllegalArgumentException("资源文件 或 zip文件不能为空");
        }
        File file = new File(srcFilePath);
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("找不到文件路径: " + srcFilePath);
        }
        int BUFFER = 1024;// 缓存大小
        try {
            List fileList = getSubFiles(new File(srcFilePath));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
            ZipEntry ze = null;
            byte[] buf = new byte[BUFFER];
            int readLen = 0;
            for (int i = 0; i < fileList.size(); i++) {
                File f = (File) fileList.get(i);

                // 过滤处理
                boolean flag = true;
                if (filterList != null && filterList.length > 0) {
                    for (String filterName : filterList) {
                        if (f.getPath().indexOf(filterName) != -1) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (!flag) {
                    continue;
                }

                ze = new ZipEntry(getAbsFileName(srcFilePath, f));
                // ze.setSize(f.length());
                // ze.setTime(f.lastModified());
                zos.putNextEntry(ze);
                InputStream is = new BufferedInputStream(new FileInputStream(f));
                while ((readLen = is.read(buf, 0, BUFFER)) != -1) {
                    zos.write(buf, 0, readLen);
                }
                is.close();
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List getSubFiles(File baseDir) {
        List ret = new ArrayList();
        File[] tmp = baseDir.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile()) {
                ret.add(tmp[i]);
            }else if (tmp[i].isDirectory()) {
                ret.addAll(getSubFiles(tmp[i]));
            }
        }
        return ret;
    }

    private static String getAbsFileName(String baseDir, File realFileName) {
        File real = realFileName;
        File base = new File(baseDir);
        String ret = real.getName();
        while (true) {
            real = real.getParentFile();
            if (real == null) break;
            if (real.equals(base)) break;
            else ret = real.getName() + "/" + ret;
        }
        return ret;
    }

    public static void main(String[] args) {
        // zipFile("D:\\template\\authority_template", "D:\\template\\authority_template.zip", null);
        // unzipFile("D:\\template\\authority_template.zip", "D:\\template\\authority_template2");
    }
}
