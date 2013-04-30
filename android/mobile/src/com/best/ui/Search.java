////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.content.Intent;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.LayoutInflater;
import com.best.data.GTStore_sqlite;
import com.best.util.TransitionEffect;

public class Search extends Activity
{
	public static Activity me = null;
	public static Context m_context;
	public static LayoutInflater mInflater;
	public int EditTextNo;
	public static boolean _callFromStops = false;
	public static String[] stopNames;
	public static String[] stopsRoad;
	public static String[] stopsArea;
	public static String[] stopID;
	public static String searchtxt;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate( savedInstanceState );
		//overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) {
				TransitionEffect.callOverridePendingTransition(this);
		}
		me = this;
		m_context = this;
		me.requestWindowFeature( Window.FEATURE_NO_TITLE );
		me.setContentView( com.best.ui.R.layout.search );
		TextView title = (TextView)me.findViewById(R.id.title);
		title.setText(me.getString(R.string.txtSearchFor));
		title.setTypeface( Best.m_marathi_typeface ); 
		//setTitle(com.best.ui.R.string.txtSearchFor);
		Intent intent = me.getIntent();
		_callFromStops = intent.getBooleanExtra("callFromStops" , false);
		init();
	
	}
		
	public void init()
	{
		final Button btnSearch = ( Button ) me.findViewById( com.best.ui.R.id.search );
		TextView title = (TextView)me.findViewById(R.id.title);
		title.setText(me.getString(R.string.txtSearchFor));
		title.setTypeface( Best.m_marathi_typeface );
		btnSearch.setText(me.getString(R.string.search_on_but));
		btnSearch.setTypeface( Best.m_marathi_typeface );	
		final EditText txtSearch = (EditText) findViewById(com.best.ui.R.id.searchtxt);
			
		btnSearch.setOnClickListener( new View.OnClickListener() {
			public void onClick(View v) {
				 searchtxt = txtSearch.getText().toString();
                System.out.println("searchtxt "+searchtxt);
				if(searchtxt != null && searchtxt.length() > 0)
				{
					if(_callFromStops == true)
					{
                        System.out.println("in callFromStops == true");
						Best.showProcessing( m_context, m_context.getString(R.string.searching),m_context.getString(R.string.please_wait_while_seacrching) );
						StopSearchThread thread = new StopSearchThread( searchStopsHandler, searchtxt);
						thread.start();
					}
					else
					{
                        System.out.println("in callFromStops == false");
						Best.showProcessing( m_context,  m_context.getString(R.string.searching), m_context.getString(R.string.please_wait_while_seacrching) );
						BusSearchThread thread = new BusSearchThread( searchBusHandler, searchtxt);
						thread.start();
					}
				}
			}
		});	
	}
	public Handler searchStopsHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			
			String[][] resultSet = (String[][]) dataBundle.getSerializable( "resultSet" );
			
			Best.dissmissProcessing();
			
			if( resultSet!=null && resultSet.length > 0 )
			{
				System.out.println("SEARCH RESULT LENGTH* = "+resultSet.length );
                String[] stopIDs1 = new String[2];
                System.out.println("above new string [] 1st");
				String[] stopIDs = new String[resultSet.length];
				String[] stopNames = new String[resultSet.length];
				String[] stopRoad = new String[resultSet.length];
				String[] stopArea = new String[resultSet.length];
                System.out.println("above for loop");
				for(int i = 0; i < resultSet.length;i++)
				{   System.out.println("in for loop");
					stopIDs[i] = resultSet[i][0];
                    System.out.println("resultSet[i][0] "+resultSet[i][0]);
					stopNames[i] = resultSet[i][1];
                    System.out.println("resultSet[i][1] "+resultSet[i][1]);
					stopRoad[i] = resultSet[i][2];
                    System.out.println("resultSet[i][2] "+resultSet[i][2]);
					stopArea[i] = resultSet[i][3];
                    System.out.println("resultSet[i][3] "+resultSet[i][3]);
				}
				Intent mIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putStringArray( "StopId" , stopIDs );
				bundle.putStringArray( "StopName" , stopNames );
				bundle.putStringArray( "StopRoad" , stopRoad );
				bundle.putStringArray( "StopArea" , stopArea );
                System.out.println("below StopArea");
                System.out.println("searchtxt "+searchtxt );
				bundle.putString( "searchtxt" , searchtxt);
				mIntent.putExtras( bundle );
				setResult( 0, mIntent );
				finish();
			}
			else
			{
				Best.showMessage( m_context,m_context.getString(R.string.errMsgNoStopsMatch) ,m_context.getString(R.string.message)  );
			}	
        }
    };
	
	private class StopSearchThread extends Thread
	{
        Handler _handler;
        String _txtSearch = "";
		
		StopSearchThread(Handler handler, String txtSearch) 
		{
            _handler = handler;
			_txtSearch = txtSearch;
        }
       
        public void run() 
		{
			String[][] resultSet = null;
			try{
                System.out.println("getAllTripsByText** called from search");
				resultSet = GTStore_sqlite.getAllStopsByText( _txtSearch, 0.0, 0.0 );
			}catch(Exception e){System.out.println("Excp2: " + e.toString() );}
		
			Bundle dataBundle = new Bundle(); 
			dataBundle.putSerializable( "resultSet", resultSet );
			
			Message msg = _handler.obtainMessage();
			msg.setData( dataBundle );
			_handler.sendMessage( msg );
        }
    }
	
	public Handler searchBusHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			
			String[][] resultSet = (String[][]) dataBundle.getSerializable( "resultSet" );
			
			Best.dissmissProcessing();
			
			if( resultSet!=null && resultSet.length > 0 )
			{
				System.out.println("SEARCH RESULT LENGTH = "+resultSet.length );
                String[] stopIDs1 = new String[2];
                System.out.println("above new string [] 1st");
				String[] routeID = new String[resultSet.length];
				String[] tripID = new String[resultSet.length];
				String[] routeLongName = new String[resultSet.length];
				String[] routeName = new String[resultSet.length];
                System.out.println("above for loop");
				//MString[] tripFreq = new String[resultSet.length];
				for(int i = 0; i < resultSet.length;i++)
				{
                    System.out.println("in for loop");
					routeID[i] = resultSet[i][0];
                    System.out.println("resultSet[i][0] "+resultSet[i][0]);
					tripID[i] = resultSet[i][1];
                    System.out.println("resultSet[i][1] "+resultSet[i][1]);
					routeLongName[i] = resultSet[i][2];
                    System.out.println("resultSet[i][2] "+resultSet[i][2]);
					routeName[i] = resultSet[i][3];
                    System.out.println("resultSet[i][3] "+resultSet[i][3]);
					//MtripFreq[i] = resultSet[i][4];
				}
                System.out.println("outside for loop");
				Intent mIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putStringArray( "routeID" , routeID );
                System.out.println("below routeid");
				bundle.putStringArray( "tripID" , tripID );
                System.out.println("below tripid");
				bundle.putStringArray( "routeLongName" , routeLongName );
                System.out.println("below routeLongName");
				bundle.putStringArray( "routeName" , routeName );
                System.out.println("below routeName");
				//bundle.putStringArray( "tripFreq" , tripFreq );
                System.out.println("searchtxt "+searchtxt );
				bundle.putString( "searchtxt" , searchtxt);
                System.out.println("below searchtxt");
				mIntent.putExtras( bundle );
				setResult( 0, mIntent );
				finish();
                System.out.println("below finish");
			}
			else
			{
				Best.showMessage( m_context, m_context.getString(R.string.errMsgNoBusMatch),  m_context.getString(R.string.message) );
			}	
        }
    };
	
	private class BusSearchThread extends Thread
	{
        Handler _handler;
        String _txtSearch = "";
		
		BusSearchThread(Handler handler, String txtSearch) 
		{
            _handler = handler;
			_txtSearch = txtSearch;
        }
       
        public void run() 
		{
			String[][] resultSet = null;
			try{
				 System.out.println("getAllTripsByText called from search");
				resultSet = GTStore_sqlite.getAllTripsByText( _txtSearch);
			}catch(Exception e){System.out.println("Excp2: " + e.toString() );}
		
			Bundle dataBundle = new Bundle(); 
			dataBundle.putSerializable( "resultSet", resultSet );
			
			Message msg = _handler.obtainMessage();
			msg.setData( dataBundle );
			_handler.sendMessage( msg );
        }
    }
	
	
}