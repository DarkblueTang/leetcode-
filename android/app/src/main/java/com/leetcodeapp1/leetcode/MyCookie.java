package com.leetcodeapp1.leetcode;

public class MyCookie {

    private String key;
    private String value;

    public MyCookie() {
    }

    public MyCookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "MyCookie{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
