package com.yuntao.zhushou.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtils {

    public static String getPrintStackTrace(Throwable t) {
        if (t == null) {
            return "";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printwriter = new PrintStream(baos);
        t.printStackTrace(printwriter);
        String str = new String(baos.toByteArray());
        try {
            return str;
        } finally {
            IOUtils.closeQuietly(printwriter);
            IOUtils.closeQuietly(baos);
        }
    }

    public static void main(String[] args) {
        try {
            if (true) {
                throw new Exception("12321312");
            }
        } catch (Exception e) {
            System.out.println(getPrintStackTrace(e));
        }
    }
}
