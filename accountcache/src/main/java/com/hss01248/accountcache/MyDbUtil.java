package com.hss01248.accountcache;

import android.app.Application;
import android.content.Context;

import com.hss01248.accountcache.db.DaoMaster;
import com.hss01248.accountcache.db.DaoSession;
import com.hss01248.accountcache.db.DebugAccountDao;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.List;

public class MyDbUtil {

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private static void initGreenDao(Application context) {
        //指定数据库存储路径
       Context context2 = new MyDBContext(context);
       //升级自动迁移数据的工具
        DaoMaster.OpenHelper helper = new MySQLiteUpgradeOpenHelper(context2, AccountCacher.dbName+"testaccount3.db");
        Database db = helper.getWritableDb();
        //不再加密.以规避sqlitesipher在6.0以下版本的c层崩溃问题
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        //兼容旧数据迁移情况
        new Thread(new Runnable() {
            @Override
            public void run() {
                moveOldData(context);
            }
        }).start();

    }

    private static void moveOldData(Application context) {
        Context context2 = new MyDBContext(context);
       File file =  context2.getDatabasePath(AccountCacher.dbName+"testaccount2.db");

       if(file == null || !file.exists()){
           return;
       }
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context2, AccountCacher.dbName+"testaccount2.db");
       //数据库加解密
        Database db = helper.getEncryptedWritableDb(AccountCacher.dbName+"856yuv98");
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession0 = daoMaster.newSession();
        List<DebugAccount> debugAccounts = daoSession0.getDebugAccountDao().loadAll();
        daoSession.getDebugAccountDao().insertInTx(debugAccounts);
        daoSession0.getDebugAccountDao().deleteAll();
        db.close();
        //删库跑路
        file.delete();
    }

    private static DaoSession daoSession;
    public static DaoSession getDaoSession() {
        if(daoSession ==null){
            initGreenDao(AccountCacher.app);
        }
        return daoSession;
    }

     static List<DebugAccount> getAll(int hostType,String countCode){
       return getDaoSession().getDebugAccountDao().queryBuilder().where(DebugAccountDao.Properties.HostType.eq(hostType)
                ,DebugAccountDao.Properties.CountryCode.eq(countCode))
                .orderDesc(DebugAccountDao.Properties.UsedNum).list();
    }


}
