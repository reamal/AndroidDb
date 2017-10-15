package com.bravo.dblib;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.bravo.dblib.anno.DbFlied;
import com.bravo.dblib.anno.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bravo.lee on 2017/10/10.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    private boolean isInit = false;

    /**
     * 持有操作数据库表所对应的java类型
     */
    private Class<T> entityClass;

    /**
     * 持有数据库操作类的引用
     */
    private SQLiteDatabase database;
    /**
     * 表名
     */
    private String tableName;
    private HashMap<String, Field> cacheMap;

    public synchronized boolean init(Class<T> clazz, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.entityClass = clazz;
            database = sqLiteDatabase;

            if (entityClass.getAnnotation(DbTable.class) == null) {
                tableName = entityClass.getSimpleName();
            } else {
                tableName = entityClass.getAnnotation(DbTable.class).value();
            }

            if (!database.isOpen()) {
                return false;
            }
            String sql;
            if (!TextUtils.isEmpty(sql = createTable())) {
                database.execSQL(sql);
            }
            cacheMap = new HashMap<>();
            initHasMap();
            isInit = true;

        }
        return isInit;
    }

    private void initHasMap() {
        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();
            Field[] columnFields = entityClass.getFields();
            for (Field field : columnFields) {
                field.setAccessible(true);
            }

            for (String columnName : columnNames) {
                Field columnField = null;
                for (Field field : columnFields) {
                    String fieldName = field.getAnnotation(DbFlied.class).value();
                    if (fieldName == null) {
                        fieldName = field.getName();
                    }

                    if (columnName.equals(fieldName)) {
                        columnField = field;
                        break;
                    }
                }
                if (columnField != null) {
                    cacheMap.put(columnName, columnField);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    public abstract String createTable();


    @Override
    public Long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        Long result = database.insert(tableName, null, values);
        return result;
    }


    @Override
    public int delete(T entity) {
        Map<String,String> map = getValues(entity);
        Condition condition = new Condition(map);
        int delete = database.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
        return delete;
    }

    @Override
    public int update(T where, T entity) {
        Map<String ,String> map = getValues(where);
        Condition condition = new Condition(map);
        ContentValues contentValues = getContentValues(getValues(entity));
        int update = database.update(tableName, contentValues, condition.getWhereClause(), condition.getWhereArgs());

        return update;
    }

    @Override
    public List<T> query(T where) {
        return null;
    }

    @Override
    public List<T> query(T where, String orderBy, String startIndex, Integer limit) {
        return null;
    }

    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field columnToField = fieldIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (columnToField.getAnnotation(DbFlied.class).value() != null) {
                cacheKey = columnToField.getAnnotation(DbFlied.class).value();
            } else {
                cacheKey = columnToField.getName();
            }

            try {
                if (null == columnToField.get(entity)) {
                    continue;
                }
                cacheValue = columnToField.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            result.put(cacheKey, cacheValue);
        }

        return result;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    /**
     * 封装语句
     */
    class Condition{
        /**
         * 查询条件
         * name=? && password =?
         */
        private String whereClause;

        private  String[] whereArgs;

        public Condition(Map<String ,String> whereClause) {
            ArrayList<String> list = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1 = 1");
            Set<String> keys = whereClause.keySet();
            Iterator<String> iteratorKey = keys.iterator();
            while (iteratorKey.hasNext()){
                String key = iteratorKey.next();
                String value = whereClause.get(key);
                if (value != null){
                    stringBuilder.append(" and " + key + " =?");
                    list.add(value);
                }
            }
            this.whereClause = stringBuilder.toString();
            this.whereArgs = list.toArray(new String[list.size()]);
        }


        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }
}
