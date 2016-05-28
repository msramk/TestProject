package com.ram.testproject.adapter;

import java.sql.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ram.testproject.R;

public class Session_Custom_Adapter extends ArrayAdapter<String>{
	 
	private final Context context;
	
	private List<String> session_name_list;
	private List<String> session_presenters_list;
	private List<String> session_timigns_list;
	private List<String> session_startdt_list;
	private List<String> session_enddt_list;
	//	public ImageLoader imageLoader; 
	int set_friend_id,userRegid;
	String set_action;
	//initialize constructor here
	public Session_Custom_Adapter(Activity activity,List<String> session_name2, List<String> session_presenter, List<String> session_timings, List<String> session_startdate, List<String> session_enddate) {		
		super(activity, R.layout.session_list_item, session_name2);
		this.context = activity;
		this.session_name_list = session_name2;
		this.session_presenters_list = session_presenter;	
		this.session_timigns_list=session_timings;
		this.session_startdt_list=session_startdate;
		this.session_enddt_list=session_enddate;
	}	
	@Override
	public View getView(final int position, View view, ViewGroup parent) 	
	{
	LayoutInflater inflater =  ((Activity) context).getLayoutInflater();
	//Load custom view here for listView
	View rowView= inflater.inflate(R.layout.session_list_item, null, true);
	//Declare all rowView variables here and initialize it's
	TextView session_name = (TextView) rowView.findViewById(R.id.textView1);
	TextView session_presenters=(TextView) rowView.findViewById(R.id.textView2);
	TextView session_time=(TextView) rowView.findViewById(R.id.textView3);
	TextView session_start_date=(TextView) rowView.findViewById(R.id.textView4);
	TextView session_end_date=(TextView) rowView.findViewById(R.id.textView5);
	//assigning the text to textviews
	if(position % 2==0)
	{
		session_name.setText("-->"+session_name_list.get(position));
		session_name.setTextColor(Color.parseColor("#3E50B4"));
	}else
	{
		session_name.setText("-->"+session_name_list.get(position));
		session_name.setTextColor(Color.parseColor("#691A99"));
	}
	session_presenters.setText("Presenters"+"..."+session_presenters_list.get(position));
	session_time.setText("Time : "+session_timigns_list.get(position));
	session_start_date.setText("Start Date: "+session_startdt_list.get(position));
	session_end_date.setText("End Date: "+session_enddt_list.get(position));
	return rowView;
	}	
}