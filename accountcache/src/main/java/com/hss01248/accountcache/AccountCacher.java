package com.hss01248.accountcache;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hss01248.accountcache.db.DebugAccountDao;

import java.util.List;

/**
 * by hss
 * data:2020-04-27
 * desc:
 */
public class AccountCacher {


    static Application app;
   public   static int TYPE_RELEASE = 0;
    public static int TYPE_TEST = 3;
    public  static int TYPE_DEV = 1;

    static boolean hasAdaptScopedStorage = true;
    static String appName = "";
    /**
     * 是否存储正式环境账号,默认false,可以设置为true
     */
    public static boolean storeReleaseAccount;

    public static void configHostType(int dev, int test, int release) {
        TYPE_RELEASE = release;
        TYPE_DEV = dev;
        TYPE_TEST = test;
    }

    /**
     * 非必须
     *
     * @param appName                可以为空. 为空则存储于默认数据库
     * @param hasAdaptScopedStorage 是否已经适配Android11的分区存储
     */
    public static void init(@Nullable String appName, boolean hasAdaptScopedStorage) {
        AccountCacher.appName = appName;
        AccountCacher.hasAdaptScopedStorage = hasAdaptScopedStorage;
    }

    static void init(Application application) {
        app = application;
        XXPermissions.setScopedStorage(hasAdaptScopedStorage);
    }

    /**
     *
     * @param activity
     * @param countryCode
     * @param callback
     */
    public static void selectAccount(int hostType, FragmentActivity activity, String countryCode, AccountCallback callback) {
        init(activity.getApplication());
        if(!storeReleaseAccount){
            if ( isNotDevOrTest(hostType)) {
                callback.onError(new Throwable("isUrlRelease"));
                return;
            }
        }

        /*if (clickCount < 2) {
            clickCount++;
            return;
        }*/


        XXPermissions permissions = XXPermissions.with(activity);
        if (hasAdaptScopedStorage) {
            permissions.permission(Permission.MANAGE_EXTERNAL_STORAGE);
        } else {
            permissions.permission(Permission.Group.STORAGE);
        }
        // .permission(hasAdaptScopedStorage ? Permission.Group.STORAGE : Permission.MANAGE_EXTERNAL_STORAGE)
        //.permission(Permission.READ_EXTERNAL_STORAGE)
        // 申请多个权限
        // .permission(Permission.Group.CALENDAR)
        permissions.request(new OnPermissionCallback() {

            @Override
            public void onGranted(List<String> permissions, boolean all) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<DebugAccount> accounts = MyDbUtil.getAll(hostType, countryCode);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                showSelectAccountDialog(hostType, activity, countryCode, accounts, callback);
                            }
                        });
                    }
                }).start();

            }

            @Override
            public void onDenied(List<String> permissions, boolean never) {
                Toast.makeText(activity.getApplicationContext(), "需要存储权限", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static boolean isNotDevOrTest(int hostType) {
        return hostType != TYPE_DEV && hostType != TYPE_TEST;
    }


    private static void showSelectAccountDialog(int hostType, Activity activity, String countryCode, final List<DebugAccount> accounts,
                                                final AccountCallback callback) {

        DebugAccount accountLast = null;
        int idx = 0;
        if (accounts != null && !accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                DebugAccount account = accounts.get(i);
                if (accountLast == null) {
                    idx = i;
                    accountLast = account;
                } else {

                    if (accountLast.updateTime < account.updateTime) {
                        accountLast = account;
                        idx = i;
                    }
                }
            }
        }
        if (idx != 0) {
            accounts.remove(idx);
            accounts.add(0, accountLast);
        }

        String[] strs = new String[accounts.size()];
        for (int i = 0; i < accounts.size(); i++) {
            if (i == 0) {
                strs[i] = accounts.get(i).account + "(上一次使用)" + accounts.get(i).usedNum;
            } else {
                strs[i] = accounts.get(i).account + "(使用次数:" + accounts.get(i).usedNum + ")";
            }

        }

        String msg = "当前国家:" + countryCode + ",当前环境:" + descs(hostType);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setSingleChoiceItems(strs, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onSuccess(accounts.get(which));
                        dialog.dismiss();
                    }
                })
                .setTitle("选择一个账号即可\n" + msg)
                //.setMessage("点击切换后,自动重启app,然后即可生效")
                //.setNegativeButton("取消",null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    private static String descs(int currentHostType) {
        if (currentHostType == TYPE_DEV) {
            return "dev环境";
        } else if (currentHostType == TYPE_TEST) {
            return "test环境";
        } else if (currentHostType == TYPE_RELEASE) {
            return "release环境";
        }
        return "未知+" + currentHostType;
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
        init(activity.getApplication());
        if(TextUtils.isEmpty(account)){
            return;
        }
        if(!storeReleaseAccount){
            if (isNotDevOrTest(currentHostType)) {
                return;
            }
        }

        XXPermissions permissions = XXPermissions.with(activity);
        if (hasAdaptScopedStorage) {
            permissions.permission(Permission.MANAGE_EXTERNAL_STORAGE);
        } else {
            permissions.permission(Permission.Group.STORAGE);
        }
        permissions.request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<DebugAccount> list = MyDbUtil.getDaoSession().getDebugAccountDao()
                                        .queryBuilder().where(
                                                DebugAccountDao.Properties.AppName.eq(AccountCacher.appName),
                                                DebugAccountDao.Properties.Account.eq(account)
                                                , DebugAccountDao.Properties.CountryCode.eq(countryCode)
                                                , DebugAccountDao.Properties.HostType.eq(currentHostType)).list();
                                if (list == null || list.isEmpty()) {
                                    DebugAccount debugAccount = new DebugAccount();
                                    debugAccount.account = account;
                                    debugAccount.pw = pw;
                                    debugAccount.appName = AccountCacher.appName;
                                    debugAccount.updateTime = System.currentTimeMillis();
                                    debugAccount.position = 0;
                                    debugAccount.countryCode = countryCode;
                                    debugAccount.hostType = currentHostType;
                                    debugAccount.usedNum = 1;
                                    MyDbUtil.getDaoSession().getDebugAccountDao().insert(debugAccount);
                                } else {
                                    DebugAccount debugAccount = list.get(0);
                                    debugAccount.usedNum = debugAccount.usedNum + 1;
                                    debugAccount.pw = pw;
                                    debugAccount.updateTime = System.currentTimeMillis();
                                    MyDbUtil.getDaoSession().getDebugAccountDao().update(debugAccount);
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        Toast.makeText(activity.getApplicationContext(), "需要存储权限", Toast.LENGTH_LONG).show();
                    }
                });


    }

    private static String encrypt(String pw) {
        if (TextUtils.isEmpty(pw)) {
            return pw;
        }
        return Base64.encodeToString(pw.getBytes(), 0);
    }

    private static String decrypt(String pw) {
        if (TextUtils.isEmpty(pw)) {
            return pw;
        }
        return new String(Base64.decode(pw, 0));
    }


}
