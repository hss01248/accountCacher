package com.hss01248.accountcache;

import android.app.Activity;

import android.app.Application;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;


/**
 * by hss
 * data:2020-04-27
 * desc:
 */
public class AccountCacher {




    public static boolean storeReleaseAccount;

    public static void configHostType(int dev, int test, int release) {

    }

    /**
     * 非必须
     *
     * @param dbName                可以为空. 为空则存储于默认数据库
     * @param hasAdaptScopedStorage 是否已经适配Android11的分区存储
     */
    public static void init(@Nullable String dbName, boolean hasAdaptScopedStorage) {

    }

    /**
     *
     * @param activity
     * @param countryCode
     * @param callback
     */
    public static void selectAccount(int hostType, FragmentActivity activity, String countryCode, AccountCallback callback) {

    }



    /**
     * 登录成功后,保存账号密码
     * 不会保存正式服的账号密码
     *
     * @param countryCode
     * @param account
     * @param pw
     */
    public static void saveAccount(Activity activity, int currentHostType, final String countryCode, String account, String pw) {

    }


}
