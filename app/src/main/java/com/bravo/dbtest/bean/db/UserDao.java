package com.bravo.dbtest.bean.db;

import com.bravo.dblib.BaseDao;

/**
 * Created by bravo.lee on 2017/10/15.
 */

public class UserDao extends BaseDao<User>{
    @Override
    public String createTable() {
        return "create table if not exists tb_user(user_id int,name varchar(20),password varchar(20))";
    }
}
