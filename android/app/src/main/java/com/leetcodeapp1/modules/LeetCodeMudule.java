package com.leetcodeapp1.modules;

import com.alibaba.fastjson.JSONArray;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.leetcodeapp1.entity.User;
import com.leetcodeapp1.jdbc.DBManager;
import com.leetcodeapp1.leetcode.Actions;
import com.leetcodeapp1.leetcode.MyCookieJar;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

public class LeetCodeMudule extends ReactContextBaseJavaModule {
    Actions actions = null;
    ExecutorService service = null;
    DBManager dbManager = null;
    ReactApplicationContext reactContext = null;

    public LeetCodeMudule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);

        this.reactContext = reactContext;

        actions = new Actions(getReactApplicationContext());
        service = Executors.newFixedThreadPool(2);

        dbManager = new DBManager(reactContext);

    }

    @Nonnull
    @Override
    public String getName() {
        return "LeetCodeMudule";
    }

    @ReactMethod
    public void login(String username, String password, Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                MyCookieJar cookieJar = actions.login(username, password);

                if (cookieJar.get("csrftoken") != null && cookieJar.get("LEETCODE_SESSION") != null) {
                    callbackSuccess.invoke();
                    User user = new User(username, password);
                    dbManager.saveUser(user);
                } else {
                    callbackError.invoke("用户名或密码有误");
                }
            } catch (IOException e) {
                callbackError.invoke("网络连接失败");
            }
        });
    }

    @ReactMethod
    public void getAllQuestions(Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                //MyCookieJar cookieJar = actions.login("986655732@qq.com", "tzy940203");
                actions.getAllQuestion();
                callbackSuccess.invoke(actions.translations.toString());
            } catch (IOException e) {
                callbackError.invoke("获取题目异常");
                e.printStackTrace();
            }
        });
    }

    @ReactMethod
    public void getQuestion(String title, Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                String question = actions.getQuestion(title);
                callbackSuccess.invoke(question);
            } catch (IOException e) {
                callbackError.invoke("获取题目信息失败！");
                e.printStackTrace();
            }
        });
    }

    @ReactMethod
    public void getAllCategories(Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                actions.getAllCategory();
                callbackSuccess.invoke(actions.categories.toJSONString());
            } catch (Exception e) {
                callbackError.invoke("获取标签失败!");
            }
        });
    }

    @ReactMethod
    public void getQuestionByTag(String tagTitle, Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                JSONArray json = actions.getQuestionByTag(tagTitle);
                callbackSuccess.invoke(json.toJSONString());
            } catch (Exception e) {
                callbackError.invoke("获取题目列表失败！");
            }
        });
    }

    @ReactMethod
    public void searhQuestion(String searchWord, Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                callbackSuccess.invoke(actions.search(searchWord).toJSONString());
            } catch (Exception e) {
                callbackError.invoke("搜索失败！");
            }
        });
    }

    @ReactMethod
    public void getUserInfo(Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                callbackSuccess.invoke(actions.getUserInfo().toJSONString());
            } catch (Exception e) {
                callbackError.invoke("获取用户信息失败！");
            }
        });
    }

    @ReactMethod
    public void checkLogin(Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                User user = dbManager.getFirstUser();
                if (user != null) {
                    MyCookieJar cookieJar = actions.login(user.getUsername(), user.getPassword());
                    if (cookieJar.get("csrftoken") != null && cookieJar.get("LEETCODE_SESSION") != null) {
                        callbackSuccess.invoke();
                    } else {
                        callbackError.invoke("用户名或密码有误");
                    }
                } else {
                    callbackError.invoke();
                }
            } catch (Exception e) {
                callbackError.invoke("查询异常");
            }
        });
    }

    @ReactMethod
    public void cleanUserInfo(Callback callbackSuccess, Callback callbackError) {
        service.submit(() -> {
            try {
                dbManager.clearUserDB();
                callbackSuccess.invoke();
            } catch (Exception e) {
                callbackError.invoke();
            }
        });
    }
}
