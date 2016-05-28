package com.ram.testproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetworkConnection {
	/**
	  * Network testing method you can put it into Helper/Utility class also
	  */
	 public static boolean isNetworkAvailable(Context mContext) 
	 {
		 ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		 if (networkInfo != null && networkInfo.isConnected()) 
		 {
			 Log.e("Network Testing", "***Available***");
			 return true;
		 }
		 else
		 {
			 Log.e("Network Testing", "***Not Available***");
			 return false;
		 }	 	 
	 } 		
}