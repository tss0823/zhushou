package com.yuntao.zhushou.common.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.yuntao.zhushou.common.exception.BizException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class QiNiuTools {

//    private static String QINIU_DOMAIN = "http://oimf6ds63.bkt.clouddn.com/";
    public static String QINIU_DOMAIN = "http://res.usefullc.cn/";
//    private static String QINIU_LIVE_DOMAIN = "usefullc.cn";
    private static String ACCESS_KEY = "hbGyL_mu7H57-9sl-MOgZ8l9JhK0Rp58wDD5HJ5v";
    private static String SECRET_KEY = "q9qljYVhjc-ggoarTB8eRTZGU3u_0lOGMi9OQLxM" ;
    private static String bucket = "fitness";
//    private static String hubName = "fitness";  //直播空间名称i

    private static Auth auth = Auth.create(ACCESS_KEY,SECRET_KEY);

//    private static Client cli;



//    /**
//     * 初始化,配置参数
//     * @param initMaps
//     */
//    public static void init(Map<String,String> initMaps){
//        QINIU_DOMAIN = initMaps.get("qiniu.domain");
//        ACCESS_KEY = initMaps.get("qiniu.accessKey");
//        SECRET_KEY = initMaps.get("qiniu.secretKey");
//        bucket = initMaps.get("qiniu.bucket");
//
//        auth = Auth.create(ACCESS_KEY, SECRET_KEY);
//
//
//
//
//    }


    public static String getToken() {
        String token = auth.uploadToken(bucket);
        return token;
    }

    public static String getOverToken(String key) {
        String token = auth.uploadToken(bucket, key);
        return token;
    }


    /**
     * 上传给定名称的文件
     * @param data
     * @param fileName
     * @return
     */
    public static String uploadFileFixName(byte[] data, String fileName) {
        String token = getOverToken(fileName);
        UploadManager uploadManager = new UploadManager();
        try {
            Response res = uploadManager.put(data, fileName, token);
            if (res.isOK()) {
                return QINIU_DOMAIN + fileName;
            }
        } catch (QiniuException e) {
            throw new BizException(e.getMessage(), e);
        }
        return "";
    }


    /**
     * 上传文件
     * @param data
     * @return
     */
    public static String uploadFile(byte data[]) {
        return uploadFile(data, "");
    }



    private static String uploadFile(byte[] data, String fileName) {
        String token = getToken();
        UploadManager uploadManager = new UploadManager();
        try {
            String key =
                    DigestUtils.md5Hex(new String(data)).toUpperCase();
            String subffix = getFileSuffix(fileName);
            if (StringUtils.isNotBlank(subffix)) {
                key = key + "." + subffix;
            }
            Response res = uploadManager.put(data, key, token);
            if (res.isOK()) {
                return QINIU_DOMAIN + key;
            }
        } catch (QiniuException e) {
            throw new BizException(e.getMessage(), e);
        }
        return "";
    }
    private static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        int index = StringUtils.lastIndexOf(fileName, ".");
        return StringUtils.substring(fileName, index + 1, fileName.length());
    }

    public static void main(String[] args) {
        try {
//           System.out.println(new ResourceFacadeImpl().uploadFile(FileUtils.readFileToByteArray(new File("D:\\Artboard 5.png"))));
            //new ResourceFacadeImpl().uploadRecorder();
            byte data[] = FileUtils.readFileToByteArray(new File("/u01/aa.txt"));
//            String url = QiNiuTools.uploadFile(data);
            String url = QiNiuTools.uploadFileFixName(data,"aa.txt");
            System.out.printf("url="+url);
//            System.out.printf("url=" + url);
//            Map<String,String> dataMap = new HashMap<>();
//            dataMap.put("qiniu.domain","http://ohweag500.bkt.clouddn.com/");
//            dataMap.put("qiniu.live.domain","usefullc.cn");
//            dataMap.put("qiniu.accessKey","JqyUtCXLT_ub7gNb0JCf4bBi_qgQ3hShs8eMe3SV");
//            dataMap.put("qiniu.secretKey","gyHaG_6NIEBpje1pJDvy_nOkEHg1pUr5d-7_fSLn");
//            dataMap.put("qiniu.bucket","chinamons");
//            dataMap.put("qiniu.hubName","chinamons");
//            init(dataMap);
//            LiveObject liveObject = crateLiveStream("ttest111222446666");
//            System.out.printf("liveObject="+liveObject);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
