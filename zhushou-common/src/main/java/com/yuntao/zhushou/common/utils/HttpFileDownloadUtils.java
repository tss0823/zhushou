package com.yuntao.zhushou.common.utils;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;


public class HttpFileDownloadUtils {

    static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static void main(String args []){

    }



    /**
     * 文件下载
     *
     * @param content
     * @param fileName
     * @param response
     */
    public static void download(byte[] content, String fileName, HttpServletResponse response) {
        try {
            // 清空response
            response.reset();

            // 编码处理，对于linux 操作系统 （ linux默认 utf-8,windows 默认 GBK)
            String defaultEncoding = System.getProperty("file.encoding");
            if (defaultEncoding != null && defaultEncoding.equals("UTF-8")) {

                response.addHeader("Content-Disposition", "attachment;filename="
                        + new String(fileName.getBytes("GBK"), "iso-8859-1"));
            } else {
                response.addHeader("Content-Disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "iso-8859-1"));
            }

            // 设置response的Header
            response.addHeader("Content-Length", "" + content.length);
            response.setContentType("application/octet-stream");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(content);
            toClient.flush();
            toClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}