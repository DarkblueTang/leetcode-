package com.leetcodeapp1.leetcode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.react.bridge.ReactApplicationContext;
import com.leetcodeapp1.utils.QuestionUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Actions {
    private ReactApplicationContext context = null;
    private String csrftoken = null;
    private String LEETCODE_SESSION = null;
    public JSONArray translations = null;
    private MyCookieJar myCookieJar = null;

    Integer num_total;
    Integer ac_medium;
    Integer frequency_mid;
    Integer frequency_high;
    String user_name;
    Integer ac_easy;
    String category_slug;
    Integer num_solved;
    Integer ac_hard;
    JSONArray stat_status_pairs;
    public JSONArray categories = null;

    public Actions(ReactApplicationContext context) {
        this.context = context;
    }

    public MyCookieJar login(String username, String password) throws IOException {
        String url = "https://leetcode-cn.com/accounts/login/";
        String boundary = "----WebKitFormBoundaryhG2vKxp7y2GAwhPX";

        Map<String, String> cookies = Jsoup.connect(url).execute().method(Connection.Method.GET).cookies();
        csrftoken = cookies.get("csrftoken");
        MediaType mediaType = MediaType.parse("Content-Type: multipart/form-data; boundary=" + boundary);

        String postData = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"login\"" + "\r\n\r\n"
                + username + "\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"password\"" + "\r\n\r\n"
                + password
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"csrfmiddlewaretoken\"" + "\r\n\r\n"
                + csrftoken + "\r\n"
                + "--" + boundary + "--";
        RequestBody requestBody = RequestBody.create(mediaType, postData);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                .addHeader("Host", "leetcode-cn.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Origin", "https://leetcode-cn.com")
//                .addHeader("X-CSRFToken", csrftoken)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Referer", "https://leetcode-cn.com/problemset/all/")
                .addHeader("Cookie", "csrftoken=" + csrftoken)
                .post(requestBody)
                .url(url)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().followRedirects(false)
                .followSslRedirects(false).build();
        Response response = okHttpClient.newCall(request).execute();

        Headers headers = response.headers();

        myCookieJar = new MyCookieJar();
        for (int i = 0; i < headers.size(); i++) {
            if (headers.name(i).toLowerCase().equals("set-cookie")) {
                myCookieJar.addAll(new MyCookieJar(headers.value(i)));
            }
        }
        this.LEETCODE_SESSION = myCookieJar.get("LEETCODE_SESSION");
        return myCookieJar;
    }

    public void getAllQuestion() throws IOException {
        getTranslationsQuestion();
        getOriginQuestion();
        parseQuestion();
    }

    private void parseQuestion() {
        JSONArray temp = new JSONArray();
        for (int i = 0; i < this.stat_status_pairs.size(); i++) {
            JSONObject json = (JSONObject) this.stat_status_pairs.get(i);
            int index = json.getJSONObject("stat").getInteger("question_id") - 1;
            if (index < this.translations.size()) {
                JSONObject json_temp = (JSONObject) this.translations.get(index);
                json_temp.put("originQuestion", json);
            } else {
                temp.add(json);
            }
        }
        this.translations.addAll(temp);
    }

    private void getOriginQuestion() throws IOException {

        Request request = new Request.Builder()
                .url("https://leetcode-cn.com/api/problems/all/")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("Cookie", myCookieJar.toString())
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        ResponseBody responseBody = okHttpClient.newCall(request).execute().body();

        if (responseBody != null) {
            JSONObject jsonObject = JSON.parseObject(responseBody.string());
            num_total = jsonObject.getInteger("num_total");
            ac_medium = jsonObject.getInteger("ac_medium");
            frequency_mid = jsonObject.getInteger("frequency_mid");
            frequency_high = jsonObject.getInteger("frequency_high");
            user_name = jsonObject.getString("user_name");
            ac_easy = jsonObject.getInteger("ac_easy");
            category_slug = jsonObject.getString("category_slug");
            num_solved = jsonObject.getInteger("num_solved");
            ac_hard = jsonObject.getInteger("ac_hard");
            stat_status_pairs = jsonObject.getJSONArray("stat_status_pairs");
        }
    }

    private void getTranslationsQuestion() throws IOException {
        MediaType mediaType = MediaType.parse("content-type: application/json");
        RequestBody requestBody = RequestBody.create(mediaType, "" +
                "{\"operationName\":\"getQuestionTranslation\",\"variables\":{},\"query\":\"query getQuestionTranslation($lang: String) {\\n  translations: allAppliedQuestionTranslations(lang: $lang) {\\n    title\\n    question {\\n      questionId\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}" +
                "");
        Request request = new Request.Builder()
                .url("https://leetcode-cn.com/graphql")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-CSRFToken", csrftoken)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        ResponseBody responseBody = response.body();

        if (responseBody != null) {
            JSONObject jsonObject = JSON.parseObject(responseBody.string());
            try {
                translations = jsonObject.getJSONObject("data").getJSONArray("translations");
            } catch (Exception e) {
            }
        }
    }

    public String getQuestion(String title) throws IOException {

        title = title.replaceAll(" ", "-");

        String queryData = "{\"operationName\":\"questionData\"," +
                "\"variables\":{\"titleSlug\":\"" + title + "\"}," +
                "\"query\":\"query questionData($titleSlug: String!) {\\n  question(titleSlug: $titleSlug) {\\n    questionId\\n    questionFrontendId\\n    boundTopicId\\n    title\\n    titleSlug\\n    content\\n    translatedTitle\\n    translatedContent\\n    isPaidOnly\\n    difficulty\\n    likes\\n    dislikes\\n    isLiked\\n    similarQuestions\\n    contributors {\\n      username\\n      profileUrl\\n      avatarUrl\\n      __typename\\n    }\\n    langToValidPlayground\\n    topicTags {\\n      name\\n      slug\\n      translatedName\\n      __typename\\n    }\\n    companyTagStats\\n    codeSnippets {\\n      lang\\n      langSlug\\n      code\\n      __typename\\n    }\\n    stats\\n    hints\\n    solution {\\n      id\\n      canSeeDetail\\n      __typename\\n    }\\n    status\\n    sampleTestCase\\n    metaData\\n    judgerAvailable\\n    judgeType\\n    mysqlSchemas\\n    enableRunCode\\n    enableTestMode\\n    envInfo\\n    __typename\\n  }\\n}\\n\"}";

        MediaType mediaType = MediaType.parse("content-type: application/json");
        RequestBody requestBody = RequestBody.create(mediaType, queryData);

        Request request = new Request.Builder()
                .url("https://leetcode-cn.com/graphql")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-CSRFToken", csrftoken)
                .post(requestBody)
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        String string = response.body().string();
        return QuestionUtil.parse(string);
    }

    public void getAllCategory() {
        if (categories != null) return;
        JSONArray array = new JSONArray();
        try {
            Document document = Jsoup.connect("https://leetcode-cn.com/problemset/algorithms").get();
            Elements as = document.getElementById("all-topic-tags").getElementsByTag("a");
            JSONObject jsonObject;
            for (int i = 0; i < as.size(); i++) {
                Element a = as.get(i);
                jsonObject = new JSONObject();
                jsonObject.put("titleZN", a.getElementsByClass("text-gray").html());
                jsonObject.put("count", Integer.parseInt(a.getElementsByClass("badge").html()));
                String searchTitle = a.attributes().get("title").toLowerCase();
                if (searchTitle == "") {
                    searchTitle = a.getElementsByClass("text-gray").html().toLowerCase();
                }
                searchTitle = searchTitle.replaceAll(" ", "-");
                jsonObject.put("searchTitle", searchTitle);
//                System.out.println(i + "-->" + searchTitle);
                array.add(jsonObject);
            }
            categories = array;
        } catch (Exception e) {
        }
    }

    public JSONArray getQuestionByTag(String tagTitle) throws IOException {
        String requestData = "{\"operationName\":\"getTopicTag\",\"variables\":{\"slug\":\""+ tagTitle +"\"},\"query\":\"query getTopicTag($slug: String!) {\\n  topicTag(slug: $slug) {\\n    name\\n    translatedName\\n    questions {\\n      status\\n      questionId\\n      questionFrontendId\\n      title\\n      titleSlug\\n      translatedTitle\\n      stats\\n      difficulty\\n      isPaidOnly\\n      topicTags {\\n        name\\n        translatedName\\n        slug\\n        __typename\\n      }\\n      __typename\\n    }\\n    frequencies\\n    __typename\\n  }\\n  favoritesLists {\\n    publicFavorites {\\n      ...favoriteFields\\n      __typename\\n    }\\n    privateFavorites {\\n      ...favoriteFields\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\\nfragment favoriteFields on FavoriteNode {\\n  idHash\\n  id\\n  name\\n  isPublicFavorite\\n  viewCount\\n  creator\\n  isWatched\\n  questions {\\n    questionId\\n    title\\n    titleSlug\\n    __typename\\n  }\\n  __typename\\n}\\n\"}";
        MediaType parse = MediaType.parse("content-type: application/json");
        RequestBody requestBody = RequestBody.create(parse, requestData);

        Request request = new Request.Builder()
                .url("https://leetcode-cn.com/graphql")
                .addHeader("Content-Type", "application/json")
                .addHeader("x-csrftoken", csrftoken)
                .addHeader("origin", "https://leetcode-cn.com")
                .addHeader("pragma", "no-cache")
                .addHeader("Cookie", myCookieJar.toString())
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        ResponseBody responseBody = client.newCall(request).execute().body();

        String string = responseBody.string();

        JSONArray questions = JSON.parseObject(string)
                .getJSONObject("data")
                .getJSONObject("topicTag")
                .getJSONArray("questions");
        JSONArray array = new JSONArray();

        for (int i = 0; i < questions.size(); i++) {
            JSONObject q = this.translateQuestionForTag(questions.getJSONObject(i));
            array.add(q);
        }
        return array;
    }

    private JSONObject translateQuestionForTag(JSONObject q) {

        JSONObject res = new JSONObject();
        JSONObject question = new JSONObject();
        JSONObject originQuestion = new JSONObject();
        JSONObject difficulty = new JSONObject();
        String title = "";
        JSONObject stats = null;
        String status = null;
        String titleSlug = null;

        String stats_str = "";
        String difficulty_str = "";

        question.put("questionId", q.getString("questionId"));
        title = q.getString("translatedTitle");
        stats_str = q.getString("stats");
        stats = JSONObject.parseObject(stats_str);

        difficulty_str = q.getString("difficulty");
        if (difficulty_str.toLowerCase().equals("easy")) {
            difficulty.put("level", 1);
        } else if (difficulty_str.toLowerCase().equals("medium")) {
            difficulty.put("level", 2);
        } else if (difficulty_str.toLowerCase().equals("hard")) {
            difficulty.put("level", 3);
        } else {
            difficulty.put("level", 0);
        }
        status = q.getString("status");
        titleSlug = q.getString("titleSlug");

        stats.put("question__title_slug", titleSlug);
        originQuestion.put("stat", stats);
        originQuestion.put("status", status);
        originQuestion.put("difficulty", difficulty);

        res.put("question", question);
        res.put("originQuestion", originQuestion);
        res.put("title", title);

        return res;
    }

    public JSONArray search(String searchWord) throws IOException {
        searchWord = URLEncoder.encode(searchWord);
        Request request = new Request.Builder()
                .url("https://leetcode-cn.com/problems/api/filter-questions/" + searchWord)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        JSONArray array = JSON.parseArray(response.body().string());
        ArrayList<Integer> list = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            list.add(array.getInteger(i));
        }

        return resolveSearchResult(list);
    }

    private JSONArray resolveSearchResult(ArrayList<Integer> list) {
        int index1 = 0, index2 = 0;

        HashMap<Integer, Integer> map1 = new HashMap<>();

        for (int i = 0; i < list.size(); i++)
            map1.put(i, list.get(i));

        HashMap<Integer, JSONObject> map2 = new HashMap<>();

        Collections.sort(list);

        while (index1 < this.translations.size() && index2 < list.size()) {
            JSONObject question = this.translations.getJSONObject(index1);
            int qid = question.getJSONObject("question").getInteger("questionId");

            if (qid == list.get(index2)) {
//                相等
                map2.put(list.get(index2), question);
                index2++;
                index1++;
            } else if (qid > list.get(index2)) {
                index2++;
            } else {
                index1++;
            }
        }
        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            Integer qid = map1.get(i);
            JSONObject question = map2.get(qid);
            if (question != null)
                array.add(question);
        }
        return array;
    }

    public JSONObject getUserInfo() throws IOException {

        Request request = new Request.Builder()
                .addHeader("Cookie", myCookieJar.toString())
                .url("https://leetcode-cn.com/api/progress/algorithms/")
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();

        return JSON.parseObject(response.body().string());
    }

    public static void main(String[] args) throws IOException {
        Actions actions = new Actions(null);
//        MyCookieJar cookieJar = actions.login("tzy13303707197@163.com", "tzy940203");
        MyCookieJar cookieJar = actions.login("986655732@qq.com", "tzy940203");
//        System.out.println(actions.LEETCODE_SESSION);
//        System.out.println(actions.csrftoken);
//        actions.getAllQuestion();


//        JSONObject json = JSON.parseObject(actions.getQuestion("second-highest-salary"));
//        System.out.println(json);

//        actions.getAllCategory();
//        JSONArray categories = actions.categories;
//        int size = categories.size();
//        JSONArray quString = actions.getQuestionByTag(categories.getJSONObject(size - 2).getString("searchTitle"));
//
//        System.out.println(quString);

//        System.out.println(actions.search("两数"));

//        System.out.println(actions.getUserInfo());

        String question = actions.getQuestion("copy-list-with-random-pointer");

        System.out.println(question);
    }
}
