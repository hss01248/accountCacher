package com.hss01248.accountcacherdemo;

import androidx.multidex.MultiDexApplication;

import com.hss01248.accountcache.AccountCacher;

public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AccountCacher.init("",true);
        AccountCacher.storeReleaseAccount = true;
        AccountCacher.configHostType(1,3,0);
    }
}
