package com.yuntao.zhushou;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by shan on 2017/5/2.
 */
public class AutoDeployTest {

    public static void main(String[] args) {
        try {
            List<String> readLines = FileUtils.readLines(new File("/u01/doublefit/test.json"),"utf-8");
            String json = StringUtils.join(readLines, "");
            JSONObject jsonObject = new JSONObject(json);
            String ref = jsonObject.getString("ref");
            int index = ref.lastIndexOf("/");
            String branch = ref.substring(index + 1);
            System.out.println("branch="+branch);
            System.out.println("ref="+jsonObject.getString("ref"));
            System.out.println("name="+jsonObject.getJSONObject("project").getString("name"));
            System.out.println("url="+jsonObject.getJSONObject("project").getString("url"));
            JSONArray commits = jsonObject.getJSONArray("commits");
            JSONObject commitsJSONObject = commits.getJSONObject(0);
            JSONObject authorJsonObject = commitsJSONObject.getJSONObject("author");
            String userName = authorJsonObject.getString("name");
            String userEmail = authorJsonObject.getString("email");
            System.out.println("userName="+userName+",userEmail="+userEmail);
//            for (String readLine : readLines) {
//                System.out.println(readLine);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
