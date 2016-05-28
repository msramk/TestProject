package com.ram.testproject;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class SessionDB {	
	//creating an object of helper class
		MyHelper mh;
		public SessionDB(Context con)
		{
			mh=new MyHelper(con, "SessionDB", null, 1);
		}		
		//create sqlite database object for DML operations
		SQLiteDatabase sdb;
		public void open()
		{
			sdb=mh.getWritableDatabase();
		}
		public void close()
		{
			sdb.close();
		}		
		//performing DML operation
		//insert into sessiondata table
		public void insertSessionData(String description,String name,String mPresenter_name,String mPresenter_email,String presenter_name,
				String presenter_email,String sesionid,String time_startdt,String time_enddt,String timings,String room_capacity,String room_name,String roomid){
			//prepare content values
			ContentValues cv=new ContentValues();
			cv.put("description", description);
			cv.put("name", name);
			cv.put("mPresenter_name", mPresenter_name);
			cv.put("mPresenter_email", mPresenter_email);
			cv.put("presenter_name", presenter_name);
			cv.put("presenter_email", presenter_email);
			cv.put("sesionid", sesionid);
			cv.put("time_startdt", time_startdt);
			cv.put("time_enddt", time_enddt);
			cv.put("timings", timings);
			cv.put("room_capacity", room_capacity);
			cv.put("room_name", room_name);
			cv.put("roomid", roomid);
			sdb.insert("SessionData", null, cv);			
		}
		//insert into sessionBookmark data table
		public void insertBookMarkData(String sesionid)
		{
			//prepare content values
			ContentValues cv=new ContentValues();
			cv.put("sessionid", sesionid);					
			sdb.insert("BookmarkData", null, cv);			
		}
		private class MyHelper extends SQLiteOpenHelper
		{
			public MyHelper(Context context, String name, CursorFactory factory,int version) 
			{
				super(context, name, factory, version);
				// TODO Auto-generated constructor stub
			}
			public void onCreate(SQLiteDatabase db) 
			{
				//creating all tables here 
				db.execSQL("create table SessionData(_id integer primary key,description text,name text,mPresenter_name text,mPresenter_email text,presenter_name text,presenter_email text,sesionid text,time_startdt text,time_enddt text,timings text,room_capacity text,room_name text,roomid text);");
				db.execSQL("create table BookmarkData(_id integer primary key,sessionid text);");
			}
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
			{
				// TODO Auto-generated method stub				
			}			
		}
		//get all bookmarks id here
		public Cursor GetBookmark_Data()
		{
			Cursor c=null;
			c=sdb.query("BookmarkData",new String[]{"sessionid"},null, null, null,null, null);
			return c;
		}
		public Cursor GetSelectedBookmark_Data(String sessionId)
		{		
			Cursor c=null;
			c=sdb.query("SessionData", new String[]{"name","presenter_name","timings","time_startdt","time_enddt"},"sesionid=?",new String[]{sessionId}, null, null, null,null);
			return c;
		}	
		public Cursor session_Data()
		{
			Cursor c=null;
			c=sdb.query("SessionData",new String[]{"name","presenter_name","timings","time_startdt","time_enddt","sesionid"},null, null, null,null, null);
			return c;
		}
		//get data for selected session id
		public Cursor session_Details(String sessionId)
		{
			String xyz=sessionId+"";
			Cursor c=null;
			c=sdb.query("SessionData", new String[]{"name","description","mPresenter_name","mPresenter_email","presenter_name","presenter_email","room_capacity","room_name","roomid","timings","time_startdt","time_enddt"},"sesionid=?",new String[]{xyz}, null, null, null,null);
			return c;
		}
		//remove saved bookmarks from here
		public void removeBookmarkSession(String s_id)
		{
			 sdb.delete("BookmarkData", "sessionid"+"="+s_id, null);		
		}
}