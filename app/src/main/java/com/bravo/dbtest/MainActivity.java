package com.bravo.dbtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bravo.dblib.BaseDao;
import com.bravo.dblib.BaseDaoFactory;
import com.bravo.dbtest.bean.db.User;
import com.bravo.dbtest.bean.db.UserDao;

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
    }

    /**
     * 增
     */
    public void insert(View v) {

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.name = "alex" + i;
            user.password = "123" + i;
            userDao.insert(user);
        }

        Toast.makeText(this, "insert finished", Toast.LENGTH_SHORT).show();
    }

    /**
     * 删
     */
    public void delete(View v) {
        User user = new User();
        user.name = "alex0";
        userDao.delete(user);
        Toast.makeText(this, "delete finished", Toast.LENGTH_SHORT).show();
    }

    /**
     * 改
     */
    public void update(View v) {
        User where = new User();
        where.name = "alex1";
        User entity = new User();
        entity.password = "xxxx";
        userDao.update(where,entity);
        Toast.makeText(this, "update finished", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查
     */
    public void query(View v) {
    }

}
