package com.hss01248.accountcache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.hss01248.accountcache.db.DaoMaster;
import com.hss01248.accountcache.db.DebugAccountDao;


import org.greenrobot.greendao.database.Database;

public class MySQLiteUpgradeOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteUpgradeOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySQLiteUpgradeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, DebugAccountDao.class);
    }
}
