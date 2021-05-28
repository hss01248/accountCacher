package com.hss01248.accountcache;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hss01248.accountcache.db.DaoMaster;
import com.hss01248.accountcache.db.DaoSession;
import com.hss01248.accountcache.db.DebugAccountDao;

import org.greenrobot.greendao.database.Database;

import java.util.List;

public class MyDbUtil {

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private static void initGreenDao(Application context) {
       Context context2 = new MyDBContext(context);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context2, context2.getPackageName()+"testaccount2.db");
        Database db = helper.getEncryptedReadableDb("856yuv98");
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
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
