package com.ram.testproject;

import java.util.ArrayList;
import java.util.List;

import com.ram.testproject.adapter.Bookmark_Custom_Adapter;
import com.ram.testproject.adapter.Session_Custom_Adapter;

import android.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public  class Bookmark_Fragment extends Fragment {
	//declare all variables here
	TextView tv;
	ListView book_list;
	SessionDB sdb;	
	Cursor c,bc;
	SimpleCursorAdapter sca;
	List<String> session_name;
	List<String> session_presenter;
	List<String> session_timings;
	List<String> session_startdate;
	List<String> session_enddate;
	List<String> session_id;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Bookmark_Fragment newInstance(int sectionNumber) {
		Bookmark_Fragment fragment = new Bookmark_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	public Bookmark_Fragment() {
	}
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bookmark_layout, container,false);	
		//initilaizing all variables here
		tv=(TextView) rootView.findViewById(R.id.textView1);
		book_list=(ListView) rootView.findViewById(R.id.book_listView1);
		tv.setVisibility(View.GONE);
		//creating object for arraylist here
		session_name=new ArrayList<String>();
		session_presenter=new ArrayList<String>();
		session_timings=new ArrayList<String>();
		session_startdate=new ArrayList<String>();
		session_enddate=new ArrayList<String>();
		session_id=new ArrayList<String>();
		//getting data from db
	    sdb=new SessionDB(getActivity());
		sdb.open();
		c=sdb.GetBookmark_Data();		
		if(!(c.getCount()>0))
		{
			tv.setVisibility(View.VISIBLE);
			tv.setText("No Bookmarks Available");
			book_list.setVisibility(View.GONE);
		}
		else
		{		
			tv.setVisibility(View.GONE);
			book_list.setVisibility(View.VISIBLE);
			c=sdb.GetBookmark_Data();			
	    	c.moveToFirst();	    	
	    	do
	    	{	  	    		
	    		String sessionid=c.getString(c.getColumnIndex("sessionid"));
	    		//adding data to list
	    		session_id.add(sessionid);
	    		bc=sdb.GetSelectedBookmark_Data(sessionid);			
		    	bc.moveToFirst();	    	
		    	do
		    	{	  
		    		String name=bc.getString(bc.getColumnIndex("name"));
					String presenter_name=bc.getString(bc.getColumnIndex("presenter_name"));
					String timings=bc.getString(bc.getColumnIndex("timings"));
					String time_startdt=bc.getString(bc.getColumnIndex("time_startdt"));
					String time_enddt=bc.getString(bc.getColumnIndex("time_enddt"));
					//adding data to list
					session_name.add(name);
					session_timings.add(timings);
					session_startdate.add(time_startdt);
					session_enddate.add(time_enddt);
					session_presenter.add(presenter_name);		    		
		    	}while(bc.moveToNext());				    	   
	    	}while(c.moveToNext());	    
	    	//creating custom bookmark adapter
	    	Bookmark_Custom_Adapter session_adapter=new Bookmark_Custom_Adapter(getActivity(),session_name,session_presenter,session_timings,session_startdate,session_enddate);
			//assigning adapter to bookmark listview
	    	book_list.setAdapter(session_adapter);
			session_adapter.notifyDataSetChanged();				
		}		
		book_list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Intent details_session=new Intent(getActivity(), SessionDetails.class);
				details_session.putExtra("SessionId",session_id.get(position));
				details_session.putExtra("ComingFrom", "BookMark");
				startActivity(details_session);
			}
		});
		return rootView;
	}	
}