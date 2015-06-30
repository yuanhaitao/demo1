package org.elasticsearch;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.text.*;
import java.sql.PreparedStatement;
public class ext_pawner {

	public static final String[] Wron_Cha={".","?","&","@",",","(",")","*",
		"0","1","2","3","4","5","6","7","8","9"," ","\t","\n","\r","，","。","、"};
	public static final String table="extendswords";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
           DataUtil da=new DataUtil("pawndb_head");
           DataUtil da1=new DataUtil("mobiledb");
           Connection cn=da.getConn();
           String sql="select * from ee_pawnerinfo";
           Set set=new HashSet();
           try {
				Statement st=  cn.createStatement();
			    ResultSet rs=  st.executeQuery(sql);
			    
			   while(rs.next())
			   {
				   Pawner per=new Pawner(rs.getString("LINKMAN"));
				   if(Is_Conn(per))
				       set.add(per);
			   }
			 
			   rs.close();
			   st.close();
			   cn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("****** pawndb_head.ee_pawnerinfo failed to query*********");
			}
           Connection cn1= da1.getConn();
           SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String sql1="insert into "+table+"(content,title,createdAt) values("
           +"?,'"+"联系人"+"',?)";
           PreparedStatement preparedStatement=null;
           try{
				preparedStatement=cn1.prepareStatement(sql1);
				Iterator<Pawner> ite=set.iterator();
				while(ite.hasNext())
				{
				Pawner man=ite.next();
				preparedStatement.setString(1, man.linkman);
				preparedStatement.setString(2, df.format(new Date()).toString());
				if(0==preparedStatement.executeUpdate())
					break;
				}
				if(preparedStatement!=null)
					preparedStatement.close();
				cn1.close();
			}catch(Exception e)
			{
			//	System.out.println("why are you so funny!");
				System.out.println("****** mobiledb.extendsword failed to insert data*********"); 
			}
		
	}
	public static boolean Is_Conn(Pawner per)
	{
		String man=per.linkman;
		if(man==null||man.length()<2)
			return false; 
		for(int i=0;i<Wron_Cha.length;i++)
		{
			if(-1!=man.indexOf(Wron_Cha[i]))
				return false;
		}
		return true;
	}

}

class Pawner{
	String linkman=null;
	public Pawner(String man)
	{
		super();
		this.linkman=man;
	}
	public String toString()
	{
		return linkman;
	}
	public int hashCode()
	{
		return linkman.hashCode();
	}
	public boolean equals(Object obj)
	{
		return linkman.equals(((Pawner)obj).linkman);
	}
}