package com.leetcodeapp1.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionUtil {
    public static String parse(String question) {
        return filterQuestionInfo(question);
    }

    private static String filterQuestionInfo(String string) {
        JSONObject json = JSON.parseObject(string);

        JSONObject question = json.getJSONObject("data").getJSONObject("question");

        string = question.getString("translatedContent");

        question.put("translatedContent", filters(string, question));

        return json.toJSONString();
    }

    private static String filters(String string, JSONObject question) {
        string = filterCommon(string, question);
        string = filterImage(string, question);
        string = filterEnter(string, question);
        string = filterA_Tag(string, question);
        string = filterApostrophe(string, question);
        return string;
    }

    private static String filterApostrophe(String string, JSONObject question) {
        try {
            string = string.replaceAll("&#3[9|8];", "'");
        } catch (Exception e) {
        }
        return string;
    }

    private static String filterA_Tag(String string, JSONObject question) {
        try {
            string = string.replaceAll("<a[\\u4e00-\\u9fa5\\=\\\"\\'\\w\\d\\/\\\\\\:\\.\\-\\ \\%\\#\\(\\)\\?]+>", "");
        } catch (Exception e) {
        }

        return string;
    }

    private static String filterEnter(String string, JSONObject question) {
        try {
            string = string.replaceAll("\\n\\n\\n\\n", "\\n\\n");
        } catch (Exception e) {
        }
        return string;
    }

    private static String filterImage(String string, JSONObject question) {

        JSONArray imgs = new JSONArray();
        try {
            Matcher matcher = Pattern.compile("<img[\\u4e00-\\u9fa5\\ \\w\\d\\/\\\\\\=\\\"\\.\\:\\-\\;\\%\\(\\)\\'\\#\\?]+>").matcher(string);
            while (matcher.find()) {
                String group = matcher.group();
                Matcher matcher1 = Pattern.compile("(https|http|ftp|rtsp|mms)[\\:\\/\\w\\d\\-\\.\\\\]+").matcher(group);
                if (matcher1.find()) {
                    imgs.add(matcher1.group());
                }
            }
            string = string.replaceAll("<img[\\u4e00-\\u9fa5\\ \\w\\d\\/\\\\\\=\\\"\\.\\:\\-\\;\\%\\(\\)\\'\\#\\?]+>", "");
            question.put("imgs", imgs);
        } catch (Exception e) {
        }
        return string;
    }

    private static String filterCommon(String string, JSONObject question) {
        try {
            string = string.replaceAll("<\\w+>|<\\/\\w+>|&quot;|&\\w+;", "");
            string = string.replaceAll("\n|\r", "\n");
            return string;
        } catch (Exception e) {
        }
        return string;
    }


}
