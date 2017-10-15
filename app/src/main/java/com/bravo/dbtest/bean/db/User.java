package com.bravo.dbtest.bean.db;

/**
 * Created by bravo.lee on 2017/10/15.
 */

import com.bravo.dblib.anno.DbFlied;
import com.bravo.dblib.anno.DbTable;

@DbTable("tb_user")
public class User {
    @DbFlied("user_id")
    public int userId = 0;
    @DbFlied("name")
    public String name;
    @DbFlied("password")
    public String password;
}
