package com.bravo.dblib;

import java.util.List;

/**
 * Created by bravo.lee on 2017/10/10.
 */

public interface IBaseDao<T> {
    /**
     * 增
     * @param entity
     * @return
     */
    Long insert(T entity);

    /**
     * 删
     * @param entity
     * @return
     */
    int delete(T entity);

    /**
     * 改
     * @param where
     * @param entity
     * @return
     */
    int update(T where,T entity);

    /**
     * 查
     * @param where
     * @return
     */
    List<T> query(T where);

    /**
     * 查
     * @param where
     * @param orderBy
     * @param startIndex
     * @param limit
     * @return
     */
    List<T> query(T where,String orderBy,String startIndex,Integer limit);
}
