package com.leetcodeapp1.leetcode;

import java.util.HashMap;
import java.util.Iterator;

public class MyCookieJar {

    private HashMap<String, String> map = new HashMap<>();

    public MyCookieJar() {
    }

    public MyCookieJar(String data) {
        this.init(data);
    }

    private void init(String data) {
        String[] split = data.split(";");
        for (int i = 0; i < split.length; i++) {
            String[] temp = split[i].split("=");
            if (temp.length >= 2) {
                map.put(temp[0], temp[1]);
            }
        }
    }

    public void add(String key, String value) {
        this.map.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<String> iterator = this.map.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            stringBuilder.append(next + "=" + this.map.get(next) + ";");
        }

        return stringBuilder.toString();
    }

    public void addAll(MyCookieJar myCookieJar) {
        this.map.putAll(myCookieJar.getMap());
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public String get(String key) {
        return this.map.get(key);
    }
}
