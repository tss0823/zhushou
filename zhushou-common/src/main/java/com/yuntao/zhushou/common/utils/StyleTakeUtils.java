package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2016-09-08 后台要动态获取，对于js响应的，还是从前端插件获取
 * 获取css 样式
 * Created by shan on 2016/8/14.
 */
public class StyleTakeUtils {

    private static final String agent          = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2194.2 Safari/537.36";

    private final static String TAG = "tag";
    private final static String ID = "id";
    private final static String CLS = "cls";

    private static String domain = "https://wx.qq.com";
    /**
     * 解决Https请求,返回404错误
     */
    private static void trustEveryone() {
        System.setProperty("jsse.enableSNIExtension", "false");
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Map<String, LinkedHashSet<String>> parseHtmlBlock(String htmlBlock) {
        Map<String, LinkedHashSet<String>> map = new HashMap<>();
        map.put(TAG, new LinkedHashSet<String>());
        map.put(ID, new LinkedHashSet<String>());
        map.put(CLS, new LinkedHashSet<String>());
        Document doc = Jsoup.parse(htmlBlock);
        pareseEles(map, doc);
        //parse img tag
        Elements elements = doc.select("img[src]");
        if (elements != null) {
            for (Element ele : elements) {
                String imgUrl  = ele.attr("src");
                if(!StringUtils.startsWith(imgUrl,"http")){
                    imgUrl = domain + imgUrl;
                }
                System.out.println("imgUrl="+imgUrl);
            }
        }
        return map;
    }

    private static void pareseEles(Map<String, LinkedHashSet<String>> map, Element ele) {
        Elements elements = ele.children();
        LinkedHashSet<String> tagList = map.get(TAG);
        LinkedHashSet<String> idList = map.get(ID);
        LinkedHashSet<String> clsList = map.get(CLS);
        for (Element element : elements) {
            //tagName
            String tagName = element.tagName();
            tagList.add(tagName);
            //id
            String id = element.attr("id");
            if (StringUtils.isNotEmpty(id)) {
                idList.add(id);
            }
            //class
            String cls = element.attr("class");
            String[] clsArray = cls.split("\\s+");
            for (String clsStr : clsArray) {
                if (StringUtils.isNotEmpty(clsStr)) {
                    clsList.add(clsStr);
                }
            }
            //continue to parse
            if (element.childNodeSize() > 0) {
                pareseEles(map, element);
            }
        }
    }

    public static List<String> takeCss(Map<String, LinkedHashSet<String>> map, String fileData) {
        Pattern pattern = Pattern.compile("[^\\{;]*\\{[^\\}]*\\}");
        Matcher matcher = pattern.matcher(fileData);
        LinkedHashSet<String> tagList = map.get(TAG);
        LinkedHashSet<String> idList = map.get(ID);
        LinkedHashSet<String> clsList = map.get(CLS);

        Pattern childIdPattern = Pattern.compile("#[^#\\s>:]+");
        Pattern childClsPattern = Pattern.compile("\\.[^\\.\\s>:]+");
        List<String> cssList = new ArrayList<>();
        while (matcher.find()) {
            String str = matcher.group();
//            if(str.indexOf("nav-pills") != -1){
//                System.out.println("");
//            }
            String sel = str.substring(0, str.indexOf("{"));
            Matcher childIdMatcher =  childIdPattern.matcher(sel);
            boolean match = false;
            while (childIdMatcher.find()){
                String childStr = childIdMatcher.group();
//                System.out.println("id="+childStr);
                String id = childStr.substring(1);
                if (idList.contains(id)) {
                    cssList.add(str);
                    match = true;
                    break;
                }
            }
            if (match) {
                continue;
            }
            Matcher childClsMatcher =  childClsPattern.matcher(sel);
//            if(sel.indexOf(".f-close:before") != -1){
//                System.out.printf("str="+str);
//            }
            while (childClsMatcher.find()){
                String childStr = childClsMatcher.group();
//                System.out.println("class="+childStr);
                String cls = childStr.substring(1);
                if (clsList.contains(cls)) {
                    cssList.add(str);
                    match = true;
                    break;
                }
            }
            if (match) {
                continue;
            }
            //tag TODO
        }

        return cssList;
    }

    public static List<String> takeCssFile(String url){
//        List<String> styleUrlList = new ArrayList<>();
        List<String> styleFileContentList = new ArrayList<>();
        try {
            String cookie = "h_uid=H54561043; webwxuvid=dbb70cbe5839ac073617dfc5135e448f8a1b8af7b3777df10fce6ae26f33ab39943672b6569eeb61b12e7d35111e58ad; _ga=GA1.2.1758403198.1436248568; _gscu_661903259=527362742rskyo15; ptui_loginuin=583697470; pac_uid=1_583697470; has_show_ilike=1; tvfe_boss_uuid=e338dccb52d3998b; ptcz=e0e0ec2f1c6e5f1322df452df74603c16e5f8069e16fbdb6ebf7136523b84f44; pgv_pvi=7573849088; RK=GD/yYe+3Ps; pt2gguin=o0583697470; o_cookie=583697470; pgv_si=s4422395904; pgv_info=ssid=s1147699607; pgv_pvid=1224891262; MM_WX_NOTIFY_STATE=1; MM_WX_SOUND_STATE=1; mm_lang=zh_CN; wxloadtime=1473300616_expired; wxpluginkey=1473294961; wxuin=146111295; wxsid=O6p9SVufTXL34TQ3; webwx_data_ticket=gSeIBKi3LtmcqxKSjCPa3JaN";
//            final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
//            final HtmlPage page = webClient.getPage(url);
//            webClient.getOptions().setCssEnabled(false);
//            webClient.getOptions().setJavaScriptEnabled(true);
//            CookieManager cookieManager = new CookieManager();
//            cookieManager.setCookiesEnabled(true);
//            StringTokenizer st = new StringTokenizer(cookie,";");
//            while(st.hasMoreElements()){
//                String ele = st.nextElement().toString();
//                String key = ele.split("=")[0];
//                String value = ele.split("=")[1];
//                Cookie cookie1 = new Cookie("qq.com", key, value);
//                cookieManager.addCookie(cookie1);
//            }
//            webClient.setCookieManager(cookieManager);
//            String xml = page.asXml();
            Document document = Jsoup.connect(url).userAgent(agent).header("cookie",cookie).timeout(10000).get();
//            Document document = Jsoup.parse(xml);
            //css
            Elements eles = document.select("link[href]");
            if (CollectionUtils.isNotEmpty(eles)) {
                for (Element ele : eles) {
                    String href = ele.attr("href");
                    if(href.startsWith("//")){
                       href = "http:"+href;
                    }
                    System.out.println("herf="+href);
                    if (!StringUtils.endsWith(href,"css")) {
                       continue;
                    }
                    String cssBody = Jsoup.connect(href).userAgent(agent).timeout(10000).execute().body();
                    styleFileContentList.add(cssBody);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return styleFileContentList;

    }



    public static void main(String[] args) {
        try {
            trustEveryone();
            List<String> styleDataList = takeCssFile("https://wx.qq.com/");
            String htmlBlock = FileUtils.readFileToString(new File("C:\\Users\\shan\\Desktop\\htmlBlock.txt"), "utf-8");
            StringBuilder sb = new StringBuilder();
            System.out.println("css file size="+styleDataList.size());
            int index = 1;
            for (String styleData : styleDataList) {
                Map<String, LinkedHashSet<String>> map = parseHtmlBlock(htmlBlock);
                List<String> cssList = takeCss(map, styleData);
//                System.err.println();
                System.out.println("解析 index="+index);
                index++;
                for (String css : cssList) {
                    sb.append(css);
//                    sb.append("\r\n");
                }
            }
            FileUtils.write(new File("C:\\Users\\shan\\Desktop\\my.css"),sb.toString(),"utf-8");

            StringBuilder resultSb = new StringBuilder();
            resultSb.append("<!DOCTYPE html>");
            resultSb.append("<html lang=\"cn\">");
            resultSb.append("<head>");
            resultSb.append("<meta charset=\"utf-8\"/>");
//            resultSb.append("<style>");
//            resultSb.append(sb.toString());
//            resultSb.append("</style>");
            resultSb.append("</head>");
            resultSb.append("<body>");
            resultSb.append(htmlBlock);
            resultSb.append("</body>");
            resultSb.append("</html>");


            FileUtils.write(new File("C:\\Users\\shan\\Desktop\\a.html"),resultSb.toString(),"utf-8");



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
