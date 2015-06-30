package org.elasticsearch;

import java.sql.DriverManager;

import com.mysql.jdbc.Connection;

public class DataUtil {
	/**
	 * 从数据中获取一个连接
	 * @return
	 */
private  String db;
public DataUtil(String database)
{
	this.db=database;
}
public  Connection getConn(){
Connection conn = null;
try {
Class.forName("com.mysql.jdbc.Driver").newInstance();
//表为test，用户名root，密码admin。
String info="jdbc:mysql://192.168.1.155:3306/"+this.db;
conn =   (Connection) DriverManager.getConnection(info, "root", "12345");		
} catch (Exception e) {
e.printStackTrace();
}
return conn;
}
}
