package com.leetcodeapp1.packages;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.leetcodeapp1.modules.LeetCodeMudule;

public class LeetCodePackage implements ReactPackage {
    @Nonnull
    @Override
    public List<NativeModule> createNativeModules(@Nonnull ReactApplicationContext reactContext) {
        ArrayList<NativeModule> list = new ArrayList<>(1);
        list.add(new LeetCodeMudule(reactContext));
        return list;
    }

    @Nonnull
    @Override
    public List<ViewManager> createViewManagers(@Nonnull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
