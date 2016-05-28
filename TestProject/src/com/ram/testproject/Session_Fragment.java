package com.ram.testproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ram.testproject.adapter.Session_Custom_Adapter;

public  class Session_Fragment extends Fragment {
	//declare all variables here
	ListView session_list;
	List<String> session_name;
	List<String> session_presenter;
	List<String> session_timings;
	List<String> session_startdate;
	List<String> session_enddate;
	List<String> session_id;
	SessionDB sdb;	
	Cursor c;
	SimpleCursorAdapter sca;
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static Session_Fragment newInstance(int sectionNumber) {
		Session_Fragment fragment = new Session_Fragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	public Session_Fragment() {
	}
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.session_layout, container,false);	
		//initializing all variables here
		session_list=(ListView) rootView.findViewById(R.id.listView1);
		session_name=new ArrayList<String>();
		session_presenter=new ArrayList<String>();
		session_timings=new ArrayList<String>();
		session_startdate=new ArrayList<String>();
		session_enddate=new ArrayList<String>();
		session_id=new ArrayList<String>();
		sdb=new SessionDB(getActivity());
		sdb.open();
		c=sdb.session_Data();		
		if(!(c.getCount()>0))
		{
			if(CheckNetworkConnection.isNetworkAvailable(getActivity())==true)
			{
				GetTask task=new GetTask();
				task.execute();
			}else
			{
				Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			c=sdb.session_Data();
	    	c.moveToFirst();
	    	do
	    	{	    		
	    		session_name.add(c.getString(c.getColumnIndex("name")));
	    		session_presenter.add(c.getString(c.getColumnIndex("presenter_name")));
	    		session_timings.add(c.getString(c.getColumnIndex("timings")));	    		
				session_startdate.add(c.getString(c.getColumnIndex("time_startdt")));
				session_enddate.add(c.getString(c.getColumnIndex("time_enddt")));	    		
	    		String st_id=c.getString(c.getColumnIndex("sesionid"));
	    		if(st_id.equals(null) || st_id.equals(""))
	    		{
	    			session_id.add("0");
	    		}else
	    		{
	    			session_id.add(st_id);
	    		}	    		
	    	}while(c.moveToNext());
	    	//creating custom adapter class herre
	    	Session_Custom_Adapter session_adapter=new Session_Custom_Adapter(getActivity(),session_name,session_presenter,session_timings,session_startdate,session_enddate);
			//assining adapter to session listview 
	    	session_list.setAdapter(session_adapter);
			session_adapter.notifyDataSetChanged();
		}			
		session_list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Intent details_session=new Intent(getActivity(), SessionDetails.class);
				details_session.putExtra("SessionId",session_id.get(position));
				details_session.putExtra("ComingFrom", "Session");
				startActivity(details_session);
			}
		});
		return rootView;
	}
	public class GetTask extends AsyncTask<Void, Void, Void> {
		String json_str;
		String url="http://myconferenceevents.com/Services/Session.svc/GetSessionsByConferenceId?conferenceId=9";
		ProgressDialog spinner_progress;
		JSONArray jr = null;    	
		@Override
		protected void onPreExecute() {
			spinner_progress = new ProgressDialog(getActivity());
	        // show progress spinner
			spinner_progress.setMessage("Loading Session List");
			spinner_progress.setCancelable(false);
			spinner_progress.show();
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			StringBuilder builder = new StringBuilder();
	    	HttpClient client = new DefaultHttpClient();
	    	HttpGet httpGet = new HttpGet(url);
	    	try{
	    			HttpResponse response = client.execute(httpGet);
	    			Log.e("REsponse", response.toString());
	    			StatusLine statusLine = response.getStatusLine();
	    			int statusCode = statusLine.getStatusCode();
	    			if(statusCode == 200)
	    			{
	    				HttpEntity entity = response.getEntity();
	    				InputStream content = entity.getContent();
	    				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	    				String line;
	    				while((line = reader.readLine()) != null)
	    				{
	    					builder.append(line);
	    				}
	    			} else 
	    			{
	    				Log.e(MainActivity.class.toString(),"Failedet JSON object");
	    			}
	    		}catch(ClientProtocolException e)
	    		{
	    			e.printStackTrace();
	    		} catch (IOException e)
	    		{
	    			e.printStackTrace();
	    		}catch (NullPointerException ne)
	    		{
	    			ne.printStackTrace();
	    		}
	    		catch (Exception ee)
	    		{
	    			ee.printStackTrace();
	    		}
	    		json_str=builder.toString();	    	
	    		try {
	    				jr=new JSONArray(json_str);
	    				Log.e("REsponse", jr.length()+"");
	    			} catch (JSONException e)
	    			{
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
//	    	try {
//				js=jr.getJSONObject(1);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	    	try {
//				String j_str=js.getString("Abstract");
//				Log.e("REsponse", j_str);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		    	
			return null;
		}
		@Override
        protected void onPostExecute(Void result) 
		{
			String description, name, mPresenter_name = null, mPresenter_email=null, presenter_name, 
					presenter_email = null, sesionid, time_startdt, time_enddt, timings, 
					room_capacity = null, room_name=null, roomid=null;
			try {
					if(!jr.isNull(0))
					{								
						for (int i = 0; i < jr.length(); i++) 
						{
				
							JSONObject js=jr.getJSONObject(i);
							//getting session name here
							name=js.getString("Name");
							description=js.getString("Abstract");							
							//adding data to list
							session_name.add(name);		
							int ses_id=js.getInt("SessionId");
							sesionid=ses_id+"";
							session_id.add(ses_id+"");
							//getting times from json object
							String t_str=js.getString("Time");									
							if(t_str.equals("null") || t_str.equals(""))
							{
//								Log.e("Timing Error", t_str);
								session_timings.add("null");
								session_startdate.add(null);
								session_enddate.add(null);
								timings=null;
								time_startdt=null;
								time_enddt=null;
							}else
							{
								JSONObject time_json=new JSONObject(t_str);	
//								Log.e("Timing Er 1", time_json.toString());
								if(time_json.length()==0)
								{
									session_timings.add("null");
									session_startdate.add(null);
									session_enddate.add(null);
									timings=null;
									time_startdt=null;
									time_enddt=null;
								}else
								{
									 timings=time_json.getString("Name");
									String start_dt=time_json.getString("StartDate");
									String end_dt=time_json.getString("EndDate");
									session_timings.add(timings);
									//converting start date into date format
									DateFormat outFormat = new SimpleDateFormat( "dd-mm-yyyy");
									start_dt=start_dt.replace("/Date(", "").replace("-0700)/", "");
									long start_time = Long.parseLong(start_dt);
									Date d_start= new Date(start_time);								     
								    //start date adding to start date list	
									
									time_startdt=outFormat.format(d_start);									
								    session_startdate.add(time_startdt);
									//converting start date into date format
								    end_dt=end_dt.replace("/Date(", "").replace("-0700)/", "");
								    long end_time = Long.parseLong(end_dt);
								    Date d_end= new Date(end_time);								      						       
								    // date adding to the end date list
								    time_enddt=outFormat.format(d_end);
								    session_enddate.add(time_enddt);
								}
							}														
//							session_presenter.add("null");
							String presenter_str=js.getString("Presenters");
							Log.e("Presenter Er", presenter_str);
							JSONArray presenters=new JSONArray(presenter_str);						
							JSONObject presenter_jo = null;							
							if(presenters.length()==0)
							{
								Log.e("Presenter Er 1", presenters.length()+"");
								session_presenter.add("null");	
								presenter_name=null;
								presenter_email=null;
							}else
							{
								String p_name = null;
								for (int j = 0; j < presenters.length(); j++) 
								{
									presenter_jo=presenters.getJSONObject(j);
									p_name=presenter_jo.getString("DisplayName");
									presenter_email=presenter_jo.getString("Email");
//									Log.e("Presenter Er 2", presenter_jo.toString());
								}
								session_presenter.add(p_name);
								presenter_name=p_name;
							}	
							sdb.open();
							sdb.insertSessionData(description, name, mPresenter_name, mPresenter_email, presenter_name, presenter_email, sesionid, time_startdt, time_enddt, timings, room_capacity, room_name, roomid);							
						}
					}else
					{
						
					}
				} catch (JSONException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (NullPointerException ne) 
				{
					// TODO Auto-generated catch block
					ne.printStackTrace();
				}catch (Exception ee) 
				{
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
			Session_Custom_Adapter session_adapter=new Session_Custom_Adapter(getActivity(),session_name,session_presenter,session_timings,session_startdate,session_enddate);
			session_list.setAdapter(session_adapter);
			session_adapter.notifyDataSetChanged();
			spinner_progress.dismiss();
//           Toast.makeText(getActivity(), json_str, 1).show();
       }	       
    }
}