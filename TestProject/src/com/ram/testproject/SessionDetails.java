package com.ram.testproject;

import com.ram.testproject.Session_Fragment.GetTask;
import com.ram.testproject.adapter.Session_Custom_Adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SessionDetails extends Activity {
	//declare all variables here
	TextView name_tv,timing_tv,startdt_tv,enddt_tv,abstract_tv,description_tv,mpresenter_tv,presenter_tv,room_tv,add_bookmark;
	SessionDB sdb;	
	Cursor c;
	SimpleCursorAdapter sca;
	String session_id;
	String description, name, mPresenter_name = null, mPresenter_email=null, presenter_name,presenter_email = null,
			sesionid, time_startdt, time_enddt, timings,room_capacity = null, room_name=null, roomid=null;
	String start_from;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.session_details);
	    //initialize all variables here
	    name_tv=(TextView) findViewById(R.id.textView1);
	    timing_tv=(TextView) findViewById(R.id.textView2);
	    startdt_tv=(TextView) findViewById(R.id.textView3);
	    enddt_tv=(TextView) findViewById(R.id.textView4);
	    abstract_tv=(TextView) findViewById(R.id.textView5);	    
	    mpresenter_tv=(TextView) findViewById(R.id.textView7);
	    presenter_tv=(TextView) findViewById(R.id.textView8);
	    room_tv=(TextView) findViewById(R.id.textView9);
	    add_bookmark=(TextView) findViewById(R.id.textView6);
	  //getting action bar here
	    ActionBar ab=getActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);	   
	    ab.setDisplayShowHomeEnabled(false);
	    ab.setDisplayUseLogoEnabled(false);
	    
	    //get intent data here
	    Intent in=getIntent();
	    session_id=in.getStringExtra("SessionId");
	    sesionid=session_id+"";
	    start_from=in.getStringExtra("ComingFrom");
	    if(start_from.equals("Session"))
	    {
	    	 ab.setTitle("SessionDetails");
	    	add_bookmark.setText("Add To BookMark");
	    }else
	    {
	    	add_bookmark.setText("Remove From BookMark");
	    	ab.setTitle("SavedSessionDetails");
	    }
	    //getting data from db
	    sdb=new SessionDB(SessionDetails.this);
		sdb.open();
		c=sdb.session_Data();		
		if(!(c.getCount()>0))
		{
			Toast.makeText(SessionDetails.this, "No Session Data Availabele", Toast.LENGTH_LONG).show();
		}
		else
		{
			c=sdb.session_Details(session_id+"");
	    	c.moveToFirst();	    	
	    	do
	    	{	  
	    		name=c.getString(c.getColumnIndex("name"));
	    		description=c.getString(c.getColumnIndex("description"));
	    		mPresenter_name=c.getString(c.getColumnIndex("mPresenter_name"));
	    		mPresenter_email=c.getString(c.getColumnIndex("mPresenter_email"));	    		
				presenter_name=c.getString(c.getColumnIndex("presenter_name"));
				presenter_email=c.getString(c.getColumnIndex("presenter_email"));				
				timings=c.getString(c.getColumnIndex("timings"));
				time_startdt=c.getString(c.getColumnIndex("time_startdt"));
				time_enddt=c.getString(c.getColumnIndex("time_enddt"));
				room_name=c.getString(c.getColumnIndex("room_name"));
				roomid=c.getString(c.getColumnIndex("roomid"));
				room_capacity=c.getString(c.getColumnIndex("room_capacity"));								
	    	}while(c.moveToNext());	    	
		}
		//assigning data to textview here
		name_tv.setText("-->"+name);
		timing_tv.setText("Time : "+timings);
		startdt_tv.setText("Start Date: "+time_startdt);
		enddt_tv.setText("End Date: "+time_enddt);		
		abstract_tv.setText("Abstract : "+"\n"+description);
		mpresenter_tv.setText("MainPresenters : "+"\n  Name : "+mPresenter_name+"\n  Email : "+mPresenter_email);
		presenter_tv.setText("Presenter : "+"\n  Name : "+presenter_name+"\n  Email : "+presenter_email);
		room_tv.setText("Room Details : "+"\n  Id : "+roomid+"\n  Name : "+room_name+"\n  Capacity : "+room_capacity);
		//implement onclick listeneres here
		add_bookmark.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(start_from.equals("BookMark"))
				{
					Toast.makeText(SessionDetails.this, "Session removed from bookmark list", Toast.LENGTH_LONG).show();
					sdb.removeBookmarkSession(sesionid);
				}else
				{
					Toast.makeText(SessionDetails.this, "Session added to bookmark list", Toast.LENGTH_LONG).show();
					sdb.insertBookMarkData(sesionid);
				}				
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			break;		
		}
		return super.onOptionsItemSelected(item);
	}
}