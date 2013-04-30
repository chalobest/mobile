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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.content.res.Configuration;
import android.content.Intent;
import android.content.Context;
import android.view.KeyEvent;
import com.best.data.GTStore_sqlite;
import com.best.util.TransitionEffect;

public class Buses extends Activity  {

	public static Activity me;
	public static Context m_context;
	public LayoutInflater mInflater;
	public static String scrStopId;
	public static String destStopId;
	public static String searchtext = "";
	public static boolean contentChanged = false;
	public static boolean RouteDisplayed = false;
	public static boolean searchResultDisplayed = false;
	public static String[][] tripsResult, _tripsResult ;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) 
			TransitionEffect.callOverridePendingTransition(this);
			
		this.setContentView( com.best.ui.R.layout.routing );
		
		init();
		
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
		//Best.dissmissProcessing();
		
    }
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{//If back was pressed
	    if (keyCode==KeyEvent.KEYCODE_BACK)
	    {
            System.out.println("back of buses clicked");
			back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	public void back()
	{
		if(RouteDisplayed == true  && searchResultDisplayed == false)
		{   System.out.println("in RouteDisplayed == true");
			me.setContentView(com.best.ui.R.layout.routing  );
			EfficientAdapter adapter = new EfficientAdapter( m_context , tripsResult);
			((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout1)).setVisibility(LinearLayout.GONE);
			( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			RouteDisplayed = false;
		}
		else if(searchResultDisplayed == true && RouteDisplayed == false )
		{   System.out.println("in searchResultDisplayed== true");
			me.setContentView(com.best.ui.R.layout.routing  );
			EfficientAdapter adapter = new EfficientAdapter( m_context , tripsResult);
			((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout1)).setVisibility(LinearLayout.GONE);
			( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			searchResultDisplayed = false;
		}
		else if(searchResultDisplayed == true && RouteDisplayed == true )
		{    System.out.println("in searchResultDisplayed == true && RouteDisplayed == true");
			me.setContentView(com.best.ui.R.layout.routing  );
			EfficientAdapter adapter = new EfficientAdapter( m_context , _tripsResult);
			( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			RouteDisplayed = false;
			( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Search Result for : " + searchtext);
		}
		else
			Best.exit( me );
	}
	// @Override
	// public void onBackPressed() {
		
		// if(RouteDisplayed == true  && searchResultDisplayed == false)
		// {
			// me.setContentView(com.best.ui.R.layout.routing  );
			// EfficientAdapter adapter = new EfficientAdapter( m_context , tripsResult);
			// ((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout1)).setVisibility(LinearLayout.GONE);
			// ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			// RouteDisplayed = false;
		// }
		// else if(searchResultDisplayed == true && RouteDisplayed == false )
		// {
			// me.setContentView(com.best.ui.R.layout.routing  );
			// EfficientAdapter adapter = new EfficientAdapter( m_context , tripsResult);
			// ((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout1)).setVisibility(LinearLayout.GONE);
			// ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			// searchResultDisplayed = false;
		// }
		// else if(searchResultDisplayed == true && RouteDisplayed == true )
		// {
			// me.setContentView(com.best.ui.R.layout.routing  );
			// EfficientAdapter adapter = new EfficientAdapter( m_context , _tripsResult);
			// ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			// RouteDisplayed = false;
			// ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Search Result for : " + searchtext);
		// }
		// else
			// Best.exit( me );
	// }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

        System.out.println("in onCreateOptionsMenu of buses.java");
			return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
		MenuInflater inflater = getMenuInflater();
        System.out.println("in onPrepareOptionsMenu of buses.java");
		menu.clear();
		if( searchResultDisplayed == false && RouteDisplayed == false)
		{   System.out.println("in searchResultDisplayed of buses.java");
			inflater.inflate(com.best.ui.R.menu.search, menu);
			return true;
		}
		else if( RouteDisplayed == true )
		{
			inflater.inflate(com.best.ui.R.menu.onlymap, menu);
			return true;
		}
		else
			return false;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case com.best.ui.R.id.mnusearch:
				Intent intent = new Intent(m_context, Search.class);  
				intent.putExtra( "callFromStops" , false );	
				me.startActivityForResult( intent, 5 );
				return true;
				
			case com.best.ui.R.id.map:
				if(NearTripRoute._stopLat != null && NearTripRoute._stopLon != null)
				{
					Bundle bundle1 = new Bundle(); 
					bundle1.putBoolean( "showSingle", false );
					bundle1.putSerializable("StopsGeoX",NearTripRoute._stopLat);
					bundle1.putSerializable("StopsGeoY",NearTripRoute._stopLon);
					Intent intent1 = new Intent(m_context, Map.class);
					intent1.putExtras( bundle1 );
					me.startActivityForResult(intent1,5);
				}
			return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	public void init()
	{
		me = this;
		m_context = this;
		((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout1)).setVisibility(LinearLayout.GONE);
		
		Best.showProcessing(m_context,m_context.getString(R.string.processing),m_context.getString(R.string.please_wait) );
		TripsSearchThread tripsSearchThread = new TripsSearchThread( searchTripsHandler );
		tripsSearchThread.start();
	}

	public Handler searchTripsHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			
			Best.dissmissProcessing();
			
			tripsResult = ( String[][] )dataBundle.getSerializable( "rts" );
			
			if(tripsResult != null && tripsResult.length > 0 )
			{
				EfficientAdapter adapter = new EfficientAdapter( m_context , tripsResult);
				( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
				//me.setListAdapter( adapter );
			
			}
			else
				Best.showMessage( m_context, m_context.getString(R.string.Thier_are_no_routes_to_show), m_context.getString(R.string.chalobest));
		
        }
    };
	
	private class TripsSearchThread extends Thread 
	{
        Handler _handler;
        
		TripsSearchThread(Handler handler ) 
		{
            _handler = handler;
			
        }
       
        public void run() 
		{
			//code to get all routes
			Bundle dataBundle = new Bundle(); 
			try
			{
				String [][] _res = GTStore_sqlite.getAllTrips();
				
					dataBundle.putSerializable( "rts", _res );
					Message msg = _handler.obtainMessage();
					msg.setData( dataBundle );
					_handler.sendMessage( msg );
			}
			catch(Exception e){Best.dissmissProcessing(); e.printStackTrace();
			}
        }
    }
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {   
	
		super.onActivityResult(requestCode, resultCode, data); 
		
		try{
			Bundle bundle = data.getExtras();
			String[] routeID = bundle.getStringArray( "routeID" );
			String[] tripID = bundle.getStringArray( "tripID" );
			String[] routeLongName = bundle.getStringArray( "routeLongName" );
			String[] routeName = bundle.getStringArray( "routeName" );
			//String[] tripFreq = bundle.getStringArray( "tripFreq" );

            searchtext = bundle.getString("searchtxt");
			_tripsResult = new String[routeID.length][5];
			for(int i = 0; i < routeID.length ; i++)
			{
				_tripsResult[i][0] = routeID[i];
				_tripsResult[i][1] = tripID[i];
				_tripsResult[i][2] = routeLongName[i];
				_tripsResult[i][3] = routeName[i];
				//_tripsResult[i][4] = tripFreq[i];
			}
			me.setContentView( com.best.ui.R.layout.routing );
			EfficientAdapter adapter = new EfficientAdapter( m_context , _tripsResult);
			( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Search Result for : " + searchtext);
			System.out.println("searchResultDisplayed =true of buses.java");
            searchResultDisplayed = true ;
		}
		catch(Exception e)
		{
			Best.log("Exception-"+e.toString());
		}	
	}
	
	public class EfficientAdapter extends BaseAdapter implements Filterable {
		
		public String[][] result;
		public EfficientAdapter(Context context, String[][] resultSet)
		{
			mInflater = LayoutInflater.from( context );
			result = new String[resultSet.length][];
			result=resultSet;

			if(resultSet == null)
				Best.showError(context ,context.getString(R.string.sorry_somthng_went_wrong));
		}

		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if(result!= null)
			{
				if (convertView == null)
				{	
					convertView = mInflater.inflate(com.best.ui.R.layout.list_route, null);
				
					holder = new ViewHolder();
					holder.head = (TextView) convertView.findViewById(com.best.ui.R.id.bushead);
					holder.blue_bus = (ImageView) convertView.findViewById(com.best.ui.R.id.busBlue);
					holder.routeName = (TextView) convertView.findViewById(com.best.ui.R.id.routename);
					
					convertView.setTag(holder);
				} 
				else 
					holder = (ViewHolder) convertView.getTag();
			
				 holder.head.setText( result[position][2] );
				 holder.routeName.setText( result[position][3] );
				 
				
				convertView.setOnClickListener(new OnClickListener() {
					 
						@Override
						public void onClick(View v) {
							System.out.println("RouteDisplayed is true of buses.java");
							RouteDisplayed = true;
							Bundle bundle = new Bundle();
							bundle.putString( "tripID" , result[position][1] );
							bundle.putString("routeLongName",result[position][2]);
							bundle.putString("routeName",result[position][3]);
							Best.log("CLICKED POSITION :"+ position);
							
							
							me.setContentView( com.best.ui.R.layout.routes );
							contentChanged = true;
							//NearTripRoute object = new NearTripRoute();
							NearTripRoute.init(m_context,result[position][1],result[position][3],result[position][2],false);
							
						
					}
				});
				
				return convertView;
			}
			return (null);
		}
			
		public class ViewHolder {
		TextView head;
		TextView routeName;
		ImageView blue_bus;
		
		}
				 
		@Override
		public Filter getFilter() {
			return null;
		}
		 
		@Override
		public int getCount() {
			if(result != null)
				return result.length;
			else
				return 0;
		}
				 
		@Override
		public Object getItem(int position) {
			if(result != null)
				return result[position];
			else
				return (null);
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}			 
	}
	
}