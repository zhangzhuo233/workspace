package com.bd.sparkproject;

import java.sql.ResultSet;
import java.util.*;

import com.bd.sparkproject.jdbc.JDBCHelper;

/**
 * JDBC辅助组件测试类
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public class JDBCHelperTest {

	public static void main(String[] args) throws Exception {
		// 1.获取JDBCHelper的单例
		JDBCHelper jdbcHelper = JDBCHelper.getInstance();
		
		// 2.测试普通的增删改语句
		// jdbcHelper.executeUpdate(
		// 		"insert into test_user(name,age) values(?,?)",
		// 		new Object[]{"王二", 28});
		
		// 3.测试查询语句
		// final Map<String, Object> testUser = new HashMap<String, Object>();

		// 设计一个内部接口QueryCallback
		// 那么在执行查询语句的时候，我们就可以封装和指定自己的查询结果的处理逻辑
		// 封装在一个内部接口的匿名内部类对象中，传入JDBCHelper的方法
		// 在方法内部，可以回调我们定义的逻辑，处理查询结果
		// 并将查询结果，放入外部的变量中
		// jdbcHelper.executeQuery(
		// 		"select name,age from test_user where id=?",
		// 		new Object[]{3},
		// 		new JDBCHelper.QueryCallback() {
		//
		// 			@Override
		// 			public void process(ResultSet rs) throws Exception {
		// 				if(rs.next()) {
		// 					String name = rs.getString(1);
		// 					int age = rs.getInt(2);
		//
		// 					// 匿名内部类的使用，有一个很重要的知识点
		// 					// 如果要访问外部类中的一些成员，比如方法内的局部变量
		// 					// 那么，必须将局部变量，声明为final类型，才可以访问
		// 					// 否则是访问不了的
		// 					testUser.put("name", name);
		// 					testUser.put("age", age);
		// 				}
		// 			}
		//
		// 		});
		//
		// System.out.println(testUser.get("name") + ":" + testUser.get("age"));

		final LinkedList<Map<String, Object>> testUserList = new LinkedList<Map<String, Object>>();
		// final Map<String, Object> testUser = new HashMap<String, Object>();
		jdbcHelper.executeQuery(
				"select name,age from test_user",
				null,
				new JDBCHelper.QueryCallback() {
					@Override
					public void process(ResultSet rs) throws Exception {
						while (rs.next()) {
							Map<String, Object> testUser = new HashMap<String, Object>();
							String name = rs.getString(1);
							int age = rs.getInt(2);
							// 匿名内部类的使用，有一个很重要的知识点
							// 如果要访问外部类中的一些成员，比如方法内的局部变量
							// 那么，必须将局部变量，声明为final类型，才可以访问
							// 否则是访问不了的
							testUser.put("name", name);
							testUser.put("age", age);
							// System.out.println(testUser);
							// System.out.println(testUser.size());
							testUserList.add(testUser);
							// System.out.println("ss: " + testUserList.size());
							System.out.println(testUserList);
						}
					}

				});
		// for (Map<String, Object> tmp: testUserList) {
		// 	System.out.println(tmp.get("name") + " " + tmp.get("age"));
		// }

		// 4.测试批量执行SQL语句
		// String sql = "insert into test_user(name,age) values(?,?)";
		//
		// List<Object[]> paramsList = new ArrayList<Object[]>();
		// paramsList.add(new Object[]{"麻子", 30});
		// paramsList.add(new Object[]{"王五", 35});
		//
		// jdbcHelper.executeBatch(sql, paramsList);
	}
	
}
