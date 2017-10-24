# AndroidDb
对安卓原生db操作进行封装，方便数据库的增删改查操作。使用简单方便。
详细的介绍请移步CSDN : http://blog.csdn.net/achilles_lee/article/details/78327849

##简单使用

###准备工作

* 每一个表对应一个Bean类和一个用来创建表的Dao类

bean类如下：

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
		
		    @Override
		    public String toString() {
		        return "User{" +
		                "userId=" + userId +
		                ", name='" + name + '\'' +
		                ", password='" + password + '\'' +
		                '}';
		    }
		}

Dao类如下：

		public class UserDao extends BaseDao<User>{
		    /**
		     * @return 创建数据库的语句。
		     */
		    @Override
		    public String createTable() {
		        return "create table if not exists tb_user(user_id int,name varchar(20),password varchar(20))";
		    }
		}


###初始化
 
* 初始化操作类：

	 	UserDao userDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);

###增删改查操作

* 增

	    public void insert(View v) {
	
	        for (int i = 0; i < 5; i++) {
	            User user = new User();
	            user.name = "alex" + i;
	            user.password = "123" + i;
	            userDao.insert(user);
	        }
	
	        Toast.makeText(this, "insert finished", Toast.LENGTH_SHORT).show();
	    }
* 删
	
	    public void delete(View v) {
	        User user = new User();
	        user.name = "alex0";
	        userDao.delete(user);
	        Toast.makeText(this, "delete finished", Toast.LENGTH_SHORT).show();
	    }
* 改

		 public void update(View v) {
	        User where = new User();
	        where.name = "alex1";
	        User entity = new User();
	        entity.password = "xxxx";
	        userDao.update(where, entity);
	        Toast.makeText(this, "update finished", Toast.LENGTH_SHORT).show();
	     }
* 查

		public void query(View v) {

	        User where = new User();
	        where.name = "alex2";
	
	        List<User> query = userDao.query(where);
	        Toast.makeText(this, "query finished , Number : " + query.size(), Toast.LENGTH_SHORT).show();
	        for (User user : query) {
	            Log.e(TAG, query.toString());
	        }
    	}
 ### 使用方法
    将dblib以module的形式导入项目即可使用。目前只实现了简单的功能，还需要更多的扩展。
