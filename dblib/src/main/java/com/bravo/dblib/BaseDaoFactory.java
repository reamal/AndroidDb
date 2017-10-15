package com.bravo.dblib;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * Created by bravo.lee on 2017/10/10.
 */

public class BaseDaoFactory {
    private static volatile BaseDaoFactory instance;
    private final String sqliteDataBasePath;
    private final SQLiteDatabase sqLiteDatabase;

    private BaseDaoFactory() {
        sqliteDataBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dbTest.db";
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDataBasePath, null);
    }

    public static BaseDaoFactory getInstance() {
        if (instance == null) {

            synchronized (BaseDaoFactory.class) {
                if (instance == null) {
                    instance = new BaseDaoFactory();
                }
            }
        }
        return instance;
    }

    public synchronized <M extends BaseDao<T>, T> M getDataHelper(Class<M> clazz, Class<T> entityClass) {
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass,sqLiteDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (M)baseDao;
    }
}
