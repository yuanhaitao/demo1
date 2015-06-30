package org.elasticsearch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;
import java.sql.PreparedStatement;

public class ext_dict {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			try{
		//		doGet();
			}catch(Exception e)
			{
				
			}
	}
 	 
		public  static  void doGet() throws   IOException {
			// TODO Auto-generated method stub
			 StringBuilder sb = new StringBuilder();
		        //访问数据库获取
			 DataUtil da=new DataUtil("mobiledb");
			 Connection cn=da.getConn();
			 ArrayList<Word> wordList =null;
			 String sql="select * from corporation";
			 try {
				Statement st=  cn.createStatement();
			    ResultSet rs=   st.executeQuery(sql);
			   wordList = new ArrayList<Word>();
			   while(rs.next())
			   {
				   long id= rs.getLong("id");
				   Word wd=new Word(rs.getString("companyName"),id);
				   wordList.add(wd);
			   }
			   for(Word wd:wordList)
			   {
				   if(!wd.getTrim(cn))
				   {
					  // System.out.println("*********Debuging*****"+wd.getId()+"********"+wd.getWord()+"*********");
				   }
			   } 
			   rs.close();
			   st.close();
			   cn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
}
class Word{
	
	private String word=null;
	private Connection cn=null;
	private long id=-1;
	private String table="extendswords";
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Word(String word,long id)
	{
		this.word=word;
		this.id=id;
	}
	public String getValue()
	{
		return this.word;
	}
	public boolean getTrim(Connection cn)
	{
		 this.cn=cn;
		 String sub_Word=this.word;
		 int  flag=0;
		 PreparedStatement preparedStatement=null;
		 PreparedStatement preparedStatement1=null;
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String sql="insert into "+table+"(id,content,title,createdAt) values("+id+",?,'"+"典当行"+"','"+df.format(new Date()).toString()+"')";
		 String sql1="select * from "+table+" t where t.content=?";
		 if(sub_Word!=null)
		{
			//System.out.println(sub_Word);
			String company=getCompanyName(sub_Word);
			//System.out.println(company);
			try{
				preparedStatement=cn.prepareStatement(sql);
				preparedStatement1=cn.prepareStatement(sql1);
				preparedStatement.setString(1, company);
				preparedStatement1.setString(1, company);
				ResultSet rs=preparedStatement1.executeQuery();
				if(company!=null&&!"".equals(company)&&!rs.next())
				{
				 // System.out.println(company);
				  flag=preparedStatement.executeUpdate();
				}
				if(preparedStatement!=null)
					preparedStatement.close();
			}catch(Exception e)
			{
			//	System.out.println("why are you so funny!");
				e.printStackTrace();
			}
		}
		 if(flag==0)
			 return false;
		 else
			 return true;
	}
	
	public String getCompanyName(String sub_word)
	{
		String company=sub_word;
		String sql="select * from area a where a.NAME=?";
		String[] ar={"","省","市","区","县","自治区","盟"};
		int flag=-1;
		int start=0;        //选取最小范围的地名
		int truncation_length=10;
		int end=sub_word.length();
		if(sub_word!=null){
		for(int i=0;i<sub_word.length();i++)
		{
			
			//当字符长度超过10的时候默认为不是省市地名了
			if(i>truncation_length)
				break;
			 PreparedStatement preparedStatement=null;
			 String city=sub_word.substring(start, i+1); //截取城市
			try{
			 preparedStatement=cn.prepareStatement(sql);
             for(int p=0;p<ar.length;p++){
            preparedStatement.setString(1, city+ar[p]);
    	    ResultSet rs = preparedStatement.executeQuery();
			 if(rs.next())
			 {
				 flag=i;
				 start=i+1;
				 if(ar[p].equals(sub_word.substring(i+1,i+1+ar[p].length())))
				 {
			     start=start+ar[p].length();
			     i=i+ar[p].length();
				 }
				 break;
			 }
             }
			 if(preparedStatement!=null)
			 {
				preparedStatement.close();
			 }
			 
			}catch(Exception e)
			{
				
			}	
		}
		}
		for(int i=start;i<sub_word.length();i++)
		{
			 for(int p=1;p<5;p++)
			 {
				 if(ar[p].equals(sub_word.substring(i,i+ar[p].length())))
				 {
			     start=i+ar[p].length();
			     break;
				 } 
			 }
			String pawn=sub_word.substring(i,i+2);
			if("典当".equals(pawn))
			{
				end=i;
				break;
			}
		}
		return company.substring(start,end);
		 
	}
}
