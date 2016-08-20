package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取css 样式
 * Created by shan on 2016/8/14.
 */
public class StyleTakeUtils {

    private static final String agent          = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2194.2 Safari/537.36";

    private final static String TAG = "tag";
    private final static String ID = "id";
    private final static String CLS = "cls";

    public static Map<String, LinkedHashSet<String>> parseHtmlBlock(String htmlBlock) {
        Map<String, LinkedHashSet<String>> map = new HashMap<>();
        map.put(TAG, new LinkedHashSet<String>());
        map.put(ID, new LinkedHashSet<String>());
        map.put(CLS, new LinkedHashSet<String>());
        Document doc = Jsoup.parse(htmlBlock);
        pareseEles(map, doc);
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
            if(str.indexOf("nav-pills") != -1){
                System.out.println("");
            }
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
            if(sel.indexOf(".f-close:before") != -1){
                System.out.printf("str="+str);
            }
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
            Document document = Jsoup.connect(url).userAgent(agent).timeout(10000).get();
            //css
            Elements eles = document.select("link[href]");
            if (CollectionUtils.isNotEmpty(eles)) {
                for (Element ele : eles) {
                    String href = ele.attr("href");
                    if(href.startsWith("//")){
                       href = "http:"+href;
                    }
                    System.out.println("herf="+href);
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
            List<String> styleDataList = takeCssFile("http://flight.qunar.com/");
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
            resultSb.append("<style>");
            resultSb.append(sb.toString());
            resultSb.append("</style>");
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
