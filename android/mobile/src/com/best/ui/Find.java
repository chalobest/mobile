////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import android.util.AttributeSet;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.LinearLayout;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Filterable;
import android.widget.Filter;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import java.util.Locale;
import android.os.Message;
import android.os.Handler;
import java.lang.Double;
import android.text.Html;
import com.best.data.GTStore_sqlite;
import com.best.util.Funcs;
import com.best.util.TransitionEffect;

public class Find extends Activity  {

	public static Activity me ;
	public static Context m_context ;
    public static AttributeSet attr;
	public static LayoutInflater mInflater;
	public static int M_FROM_LIST_CAPTION = 1;
	public static int M_TO_LIST_CAPTION = 2;
	public static int M_FROM_NEAR_BY_LIST_CAPTION = 3;
	public static int M_TO_NEAR_BY_LIST_CAPTION = 4;
	public static int WIDGET_VISIBLE = 0;
	public static int WIDGET_INVISIBLE = 4;
	public static boolean mode;
	public static String[] stopsId ;
	public static String[] stopName;
	public static String[] distance;
	public static String[] stopLatitude;
	public static String[] stopLongitude;
	public static String[][] resultTemp;
	public static String[][] nearByTripsInfo = null;
	public static List<String[]> prevSearchList = new ArrayList<String[]>();
	public static ArrayList<String[]> listOfTrips;
	public static List<String> tripIdList;
	public static List<String> routeNamesList ;
	public static List<String> busHeadList ;
	public static List<String> busFreqList ;
	public static List<String> totalDistList ;
	public static List<String> no_StopsList ;

    public static int selPosition = 0;
	public static int flagSourceSel ;
	public static int flagDestSel ;
	public static int listDisplayed = -1;
	
	public static boolean flagPanelVisible = true;
	public static boolean contentChanged = false ;
	public static boolean nearByBusClick = false ;
	public static boolean callFromNearTripRoute = false;
	public static boolean directRouteDisplayed = false;
	
	public static String _sourceStopId = null ;
	public static String _destStopId =null ;
	public static String sourceStop = "";
	public static String destStop = "";
	public static String _routeLongName ;
	public static String _routeName ;
	public static String _tripId ;
	public static String _freq ;
	public static Double latDouble=0.0;
	public static Double longDouble=0.0;
	
	public static ProgressDialog dialog;
 	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
		
		System.out.println("SDK VERSION ========="+android.os.Build.VERSION.SDK_INT );
		
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) 
			TransitionEffect.callOverridePendingTransition(this);

			this.setContentView( com.best.ui.R.layout.find );
			GTStore_sqlite.init(this);
			flagSourceSel=-1;
			flagDestSel=-1;
			sourceStop = "";
			destStop = "";
			init();
		
	}
	@Override
	public void onContentChanged() {
		if(callFromNearTripRoute == true)
		{
			init();
			callFromNearTripRoute = false;
		}
		Best.log("CONTENT CHANGED");
	}
	
	@Override
    protected void onSaveInstanceState(final Bundle outState) {
		outState.putInt("src",flagSourceSel);
		outState.putInt("dest",flagDestSel);
		outState.putInt("listDisplay",listDisplayed);
    }
  
   @Override
    protected void onRestoreInstanceState(final Bundle outState) {
		flagSourceSel=outState.getInt("src");
		flagDestSel=outState.getInt("dest");
    }
 
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
    protected void onResume() {
        super.onResume();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
		MenuInflater inflater = getMenuInflater();
		menu.clear();
		if(nearByBusClick == true)
			inflater.inflate(com.best.ui.R.menu.onlymap, menu);
		else
			inflater.inflate(com.best.ui.R.menu.menustart, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case com.best.ui.R.id.mnuexitmain:
				Best.exit( me );
				return true;
				
			case com.best.ui.R.id.map:
				if(NearTripRoute._stopLat != null && NearTripRoute._stopLon != null)
				{
					Bundle bundle1 = new Bundle(); 
					bundle1.putBoolean( "showSingle", false );
					bundle1.putSerializable("StopsGeoX",NearTripRoute._stopLat);
					bundle1.putSerializable("StopsGeoY",NearTripRoute._stopLon);
					Intent intent = new Intent(m_context, Map.class);
					intent.putExtras( bundle1 );
					me.startActivityForResult(intent,5);
				}
			return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
    @Override
    protected void onPause() {
		super.onPause();
       //cancelUpdatePosition();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{//If back was pressed
	    if (keyCode==KeyEvent.KEYCODE_BACK)
	    {
            System.out.println("back of find clicked");
			back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	public void back()
	{
		if(contentChanged == true )
		{   System.out.println("bacj of find .java in contentChanged == true");
			this.setContentView( com.best.ui.R.layout.find );
			contentChanged = false;
			init();
			nearByBusClick = false;
            Otpdescdetail.iterlistdisplayed=false;
           // Otpdescdetail.finishme();
		}

		else if(!contentChanged)
        {
            System.out.println("bacj of find .java in contentChanged == false");
			Best.exit( this );

        }
		
	}

	public void handleFromSearch(String fromTxt)
	{
		if( fromTxt.length() > 0 )
		{
			if( fromTxt.length() > 2 )
				findBusStops( false, fromTxt, M_FROM_LIST_CAPTION );
			else
			{
				Best.showError( m_context, m_context.getString(R.string.errMsgMoreChar));
				Best.log("ERROR MESSAGE IN MARATHI: "+com.best.ui.R.string.errMsgMoreChar);
			}
		}
		else
				{	
				    Best.showError( m_context, m_context.getString(R.string.errMsgEnterSrcLoc) );
				
                }
				
	}
	
	public void handleToSearch(String toTxt)
	{
		if(toTxt.length()>0)
		{
			if(toTxt.length()>2)
				findBusStops( false, toTxt, M_TO_LIST_CAPTION );
			else
				Best.showError( m_context, m_context.getString(R.string.errMsgMoreChar) );
		}
		else
			
			Best.showError( m_context, m_context.getString(R.string.errMsgEnterDestLoc) );
	}
	
	public void handleEnter(KeyEvent e,EditText txt,boolean frmSearch)
	{
		if( e.getKeyCode() == KeyEvent.KEYCODE_ENTER && e.getAction() == KeyEvent.ACTION_DOWN) //enter
		{
			if( frmSearch )
				handleFromSearch( txt.getText().toString().trim() );
			else	
				handleToSearch( txt.getText().toString().trim() );
		}
	}

   /* public static void finishme()
    {
        System.out.println("in find.finishme");
        me.finish();
    }*/
	public  void init()  //m
	{
		me = this;
		m_context = this;
		callSwitchLang();
		tripIdList = new ArrayList<String>();
		routeNamesList = new ArrayList<String>();
		busHeadList = new ArrayList<String>();
		busFreqList = new ArrayList<String>();
		totalDistList = new ArrayList<String>();
		no_StopsList = new ArrayList<String>();
		
		if(prevSearchList != null && prevSearchList.size() > 0)
		{
			((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout5)).setVisibility(LinearLayout.VISIBLE);
			EfficientAdapter adapter = new EfficientAdapter( m_context, prevSearchList.toArray( new String[ prevSearchList.size() ][] ) );
				( ( ListView ) me.findViewById(android.R.id.list) ).setAdapter( adapter );
		}
		
		final EditText txtFrm = (EditText) me.findViewById(com.best.ui.R.id.fromtxt);
		
			   txtFrm.setTypeface(Best.m_marathi_typeface);
        		
		
        
		final EditText txtTo = (EditText) me.findViewById(com.best.ui.R.id.totxt); 
		txtTo.setTypeface(Best.m_marathi_typeface);
		if(sourceStop != null && sourceStop.length() > 0)
			txtFrm.setText(sourceStop);
		if(destStop != null && destStop.length() > 0)
			txtTo.setText(destStop);
		if(prevSearchList == null || prevSearchList.size() == 0)
			((LinearLayout) me.findViewById(com.best.ui.R.id.linearlayout5)).setVisibility(LinearLayout.GONE);
		final LinearLayout expLayout = (LinearLayout) me.findViewById(com.best.ui.R.id.explinearlayout);
		///////
		
		 if( Best.m_latitude.length() > 0 && Best.m_longitude.length() > 0 )
		 {
			latDouble = Double.valueOf( Best.m_latitude );//Double.valueOf( "40.45939" ); 18.930075
			longDouble = Double.valueOf( Best.m_longitude );//Double.valueOf( "-3.78429" );72.833308
		}	
		
		Best.showProcessing( m_context, m_context.getString(R.string.loading),m_context.getString(R.string.please_wait) );
		NearByTripsSearchThread nearByTripsSearch = new NearByTripsSearchThread( nearByTripsSearchHandler );
		nearByTripsSearch.start();

		final Button btnexpand = (Button) me.findViewById(com.best.ui.R.id.expandable);
		btnexpand.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(flagPanelVisible == true)
				{
					 expLayout.setVisibility(LinearLayout.GONE);
					 flagPanelVisible = false;
					// btnexpand.setImageResource(R.drawable.ic_plus_trans);
				}
				else
				{
					 expLayout.setVisibility(LinearLayout.VISIBLE);
					 flagPanelVisible = true;
					// btnexpand.setImageResource(R.drawable.ic_minus_trans);
				}	
			}	
		});
		
		
		 txtFrm.setOnKeyListener( new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				handleEnter( event, txtFrm, true );
				return false;
			}
		}); 
		
		final ImageButton btnsearchfrm = (ImageButton) me.findViewById(com.best.ui.R.id.findfrom);
		btnsearchfrm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				handleFromSearch( txtFrm.getText().toString().trim() );
				
			}	
		});
		
		 txtTo.setOnKeyListener( new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				handleEnter( event, txtTo, false );
				return false;
			}
		}); 
		
		final ImageButton btnsearchto = (ImageButton) me.findViewById(com.best.ui.R.id.findto);
		btnsearchto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				handleToSearch( txtTo.getText().toString().trim() );
			}	
		});
		
		// final Button btnnearfrom = (Button) me.findViewById(com.best.ui.R.id.nearFrom);
		// btnnearfrom.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			////	Best.showMessage(me,"Registered GPS LocationListener with values: Lat="+latDouble+"--Long="+longDouble,"GPS");
				// findNearByStops( true );
			// }	
		// });
			
		// final Button btnnearto = (Button) me.findViewById(com.best.ui.R.id.nearTo);
		// btnnearto.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
				// findNearByStops( false );
			// }	
		// });
		
		final Button btnfindbus = ( Button ) me.findViewById( com.best.ui.R.id.findbus );
		
			   btnfindbus.setTypeface(Best.m_marathi_typeface);
		btnfindbus.setOnClickListener( new View.OnClickListener() {
			public void onClick(View v) {
			
				String sourceText = txtFrm.getText().toString();
				String destText = txtTo.getText().toString();
				if( sourceText.length() > 0 )
				{
					if( flagSourceSel != -1 )
					{
						if( destText.length() > 0 )
						{
							if( flagDestSel != -1 )
							{   
								//Best.showProcessing( m_context, "Searching","Please wait while searching.." );
								Best.showProcessing( m_context,m_context.getString(R.string.lnfrser),m_context.getString(R.string.lnfrserb) );
								RouteSearchThread searchRoute = new RouteSearchThread( searchRoutesHandler );
								searchRoute.start();
							}
							else
								Best.showError( m_context, m_context.getString(R.string.errMsgEnterValidDest) );
						}
						else
							Best.showError( m_context, m_context.getString(R.string.errMsgEnterDestLoc) );
					}
					else
						Best.showError( m_context, m_context.getString(R.string.errMsgEnterValidSrc) );
				}
				else
				Best.showError( m_context, m_context.getString(R.string.errMsgEnterSrcLoc) );
			}
		
		});	
	}
	
	
	public Handler nearByTripsSearchHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			Best.dissmissProcessing();
			nearByTripsInfo = ( String[][] )dataBundle.getSerializable( "rts" );
		
			if( nearByTripsInfo != null &&  nearByTripsInfo.length > 0)
			{
				listOfTrips = new ArrayList<String[]>();
			
				for(int i=0; i< nearByTripsInfo.length; i++)
				{
					System.out.println("======nearByTripsInfo=="+nearByTripsInfo[i][0]+"=="+nearByTripsInfo[i][1]+"==="+nearByTripsInfo[i][2]+"==="+nearByTripsInfo[i][3]);
					listOfTrips.add(nearByTripsInfo[i]);
					if(!(i >= (nearByTripsInfo.length - 2)))
					{
						if(nearByTripsInfo[i][3].equals(nearByTripsInfo[i+1][3]))
						{ i++;}
					}
				}
				if(listOfTrips.size() > 0 )
				{
					Best.log("listOfTrips NOT NULL");
					EfficientAdapterForNear adapter = new EfficientAdapterForNear( m_context, listOfTrips.toArray( new String[ listOfTrips.size() ][] ) );
					 ((TextView) me.findViewById(com.best.ui.R.id.txtnearby)).setVisibility( WIDGET_VISIBLE );
					 ( ( ListView )me.findViewById( com.best.ui.R.id.list ) ).setVisibility( WIDGET_VISIBLE );
					( ( ListView )me.findViewById( com.best.ui.R.id.list ) ).setAdapter( adapter );
				}
			}
			else
			{
				System.out.println("======No near by buses=====");
				( ( ListView )me.findViewById( com.best.ui.R.id.list ) ).setVisibility( WIDGET_INVISIBLE );
				((TextView) me.findViewById(com.best.ui.R.id.txtnearby)).setVisibility( WIDGET_INVISIBLE );
				
			}		
        }
    };
	
	private class NearByTripsSearchThread extends Thread 
	{
        Handler _handler;
        
		NearByTripsSearchThread(Handler handler ) 
		{
            _handler = handler;
        }
       
        public void run() 
		{
			//code to get all routes
			Bundle dataBundle = new Bundle(); 
			
			dataBundle.putSerializable( "rts", GTStore_sqlite.getNearByTripInfo(latDouble, longDouble));
			Message msg = _handler.obtainMessage();
			msg.setData( dataBundle );
			_handler.sendMessage( msg );
        }
    }
	
	public Handler searchRoutesHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			
			Best.dissmissProcessing();
			
			String[][] routesResult = ( String[][] )dataBundle.getSerializable( "rts" );
			
			if(routesResult != null && routesResult.length > 0 )
			{
				String currentTime = Funcs.getCurrentTime(true);
				
				sourceStop =((EditText) me.findViewById(com.best.ui.R.id.fromtxt)).getText().toString().trim();
				destStop = ((EditText) me.findViewById(com.best.ui.R.id.totxt)).getText().toString().trim();
				String[] searchInfo = {_sourceStopId, sourceStop, _destStopId, destStop ,currentTime};
				if(prevSearchList.size() == 4)
					prevSearchList.remove(3);
				if(prevSearchList.size() > 0)
				{
					int addItem = 0;
					for(int i =0 ;i < prevSearchList.size(); i++)
					{
						String[] temp = prevSearchList.get(i);
						if(temp[0].equals(searchInfo[0]))
						{
							if(temp[2].equals(searchInfo[2]))
							{
								prevSearchList.remove(i);
								break;
							}
						}
					}
					prevSearchList.add(0,searchInfo);
				}
				else
					prevSearchList.add(0,searchInfo);
				//GTStore.insertIntoPreviousSearch(_sourceStopId,_destStopId);
				//( prevSearchList.toArray( new String[ prevSearchList.size() ][] ) )
				
				( ( ListView )me.findViewById( android.R.id.list ) ).setVisibility( WIDGET_VISIBLE );
				listDisplayed = 1;
			
			
				for(int i = 0; i< routesResult.length; i++)
				{
					tripIdList.add(routesResult[i][0]);
					routeNamesList.add(routesResult[i][1]);
					busHeadList.add(routesResult[i][2]);
					busFreqList.add(routesResult[i][5]);
					totalDistList.add(routesResult[i][4]);
					no_StopsList.add(routesResult[i][3]);
					if(!(i >= (routesResult.length - 2)))
					{
						if((routesResult[i][1]).equals(routesResult[i+1][1]))
							i++;
					}
				}
			
				me.setContentView( com.best.ui.R.layout.routing );
				contentChanged = true;
                Otpdescdetail.iterlistdisplayed=true;
				Routing.init(m_context);
			}
			else
				Best.showMessage( m_context, m_context.getString(R.string.Thier_are_no_routes_to_show), m_context.getString(R.string.chalobest) );
        }
    };
	
	public class RouteSearchThread extends Thread
	{
        public Handler _handler;
        
		RouteSearchThread(Handler handler ) 
		{
            _handler = handler;
        }
       
        public void run() 
		{
			//code to get all routes
			Bundle dataBundle = new Bundle(); 
			String currTime = Funcs.getCurrentTime( true );
			
			// dataBundle.putSerializable( "rts", GTStore.getRoutes( _sourceStopId, _destStopId, Funcs.getCurrentDay(), currTime, Funcs.addTime( currTime, "00:25:00" ) ) );
			//dataBundle.putSerializable( "rts", GTStore.findRoutes( 40.44938, -3.6912, 40.41876, -3.69263, Funcs.getCurrentDay() ) );
			
			try
			{
				String [][] _res = GTStore_sqlite.getDirectRoutes( _sourceStopId, _destStopId );

				// String [][] _res = GTStore.getDirectRoutes( _sourceStopId, _destStopId );
				
				dataBundle.putSerializable( "rts", _res );
				Message msg = _handler.obtainMessage();
				msg.setData( dataBundle );
				_handler.sendMessage( msg );
				
			}
			catch(Exception e){Best.dissmissProcessing(); e.printStackTrace();}
        }
    }
	
	public void findNearByStops(boolean fromButton)
	{
		if( Best.checkGPS( m_context ) )
		{
			if( fromButton )
				findBusStops( true, "", M_FROM_NEAR_BY_LIST_CAPTION );
			else
				findBusStops( true, "", M_TO_NEAR_BY_LIST_CAPTION );
		}		
	}
	
	public Handler searchStopsHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			
			String[][] resultSet = (String[][]) dataBundle.getSerializable( "resultSet" );
			boolean byPosition = dataBundle.getBoolean( "byPosition" );
			boolean allOK = dataBundle.getBoolean( "allOK" );
			int editBox = dataBundle.getInt( "editBox" );
			
			Best.dissmissProcessing();
			
			if( allOK )
			{
				if( resultSet!=null && resultSet.length > 0 )
					displayList( resultSet, editBox );
				else
				{
					if( byPosition )
						Best.showMessage( m_context, m_context.getString(R.string.errMsgNoNearbyStop), m_context.getString(R.string.chalobest) );
					else
						Best.showMessage( m_context,m_context.getString(R.string.errMsgNoStopsMatch), m_context.getString(R.string.chalobest));
				}	
			}
			else
				Best.showMessage( m_context, m_context.getString(R.string.sorry_somthng_went_wrong), m_context.getString(R.string.chalobest) );
        }
    };
	
	private class StopSearchThread extends Thread
	{
        Handler _handler;
        String _txtSearch = "";
		boolean _byPosition = false;
		int _editBox = -1;
		
		
		StopSearchThread(Handler handler, String txtSearch, boolean byPosition, int editBox ) 
		{
            _handler = handler;
			_txtSearch = txtSearch;
			_byPosition = byPosition;
			_editBox = editBox;
        }
       
        public void run() 
		{
			boolean allOK = true;
			String[][] resultSet = null;
			
			if( Best.m_latitude.length() > 0 && Best.m_longitude.length() > 0 )
			{
				latDouble = Double.valueOf( Best.m_latitude );//Double.valueOf( "40.45939" );
				longDouble = Double.valueOf( Best.m_longitude );//Double.valueOf( "-3.78429" );
			}	
			
            if( _byPosition )
			{
				//near by stops
				try{
					
					resultSet = GTStore_sqlite.getNearByStops( latDouble, longDouble );
				}catch(Exception e){System.out.println("Excp2: " + e.toString() );allOK = false;}
			}
			else
			{
				//search by inputtext
				try{
					resultSet = GTStore_sqlite.getAllStopsByText( _txtSearch, latDouble, longDouble );
				}catch(Exception e){allOK = false; e.printStackTrace();}
			}
			
			Bundle dataBundle = new Bundle(); 
			dataBundle.putSerializable( "resultSet", resultSet );
			dataBundle.putBoolean( "byPosition", _byPosition );
			dataBundle.putBoolean( "allOK", allOK );
			dataBundle.putInt( "editBox", _editBox );
			
			Message msg = _handler.obtainMessage();
			msg.setData( dataBundle );
			_handler.sendMessage( msg );
        }
    }
	
	public void findBusStops(boolean byPosition, String enteredText, int editBoxNo)
	{
		Best.showProcessing(  m_context,m_context.getString(R.string.lnfrser),m_context.getString(R.string.lnfrserb) );
		StopSearchThread thread = new StopSearchThread( searchStopsHandler, enteredText, byPosition, editBoxNo );
		thread.start();
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {   
	
		super.onActivityResult(requestCode, resultCode, data); 
	
			Best.log("ON ACTIVITY RESULT");
			try{
				Bundle bundle = data.getExtras();
				int editBoxNo=bundle.getInt( "EditText" );
				selPosition=bundle.getInt( "position" );
				Best.log("EDIT BOX NUMBER :" + editBoxNo);
				if( editBoxNo == 1 || editBoxNo == 3 )
				{
					String FrmLoc=bundle.getString( "frmLoc" );
					EditText txtfrm1 = ( EditText ) findViewById(com.best.ui.R.id.fromtxt); 
					txtfrm1.setText(FrmLoc);
					flagSourceSel = selPosition;
					_sourceStopId = bundle.getString( "stopId" );
				}
				else if(editBoxNo==2||editBoxNo==4)
				{
					String ToLoc=bundle.getString("frmLoc");
					EditText txtTo1 = (EditText) findViewById(com.best.ui.R.id.totxt); 
					txtTo1.setText(ToLoc);
					flagDestSel = selPosition;
					_destStopId = bundle.getString( "stopId" );
				}
			}
			catch(Exception e)
			{
				Best.log("Exception-"+e.toString());
			}	
		
	}
	
	public void displayList(String[][] resultSet,int editBoxNo)
	{
		try{
				String[] stopsId, stopName, stopRoad, stopArea;
				stopsId = new String[resultSet.length ];
				stopName = new String[resultSet.length ];
				stopRoad = new String[resultSet.length ];
				stopArea = new String[resultSet.length ];
				
				for(int index=0; index<resultSet.length; index++)
				{
					if( resultSet[ index ] != null && resultSet[ index ].length > 0 )
					{
						String[] stopDesc = (String[])(resultSet[ index ]);
						stopsId[ index ] = stopDesc[0];
						stopName[ index ] = stopDesc[1];
						stopRoad[ index ] = stopDesc[2];
						stopArea[ index ] = stopDesc[3];
						Best.log( "ID : "+stopsId[ index ].toString()+" -- "+stopName[ index ].toString()+" --- "+stopRoad[ index ].toString() );
					}
				}
				
				Intent intent = new Intent(m_context, ListLocation.class);  
				Bundle bundle = new Bundle(); 
				bundle.putStringArray( "StopID", stopsId );
				bundle.putStringArray( "Stops", stopName );
				bundle.putStringArray( "stopRoad", stopRoad );
				bundle.putStringArray( "stopArea", stopArea );
				bundle.putInt( "EditText", editBoxNo );
				//edit.putExtras( bundle );
				intent.putExtras( bundle );
				//parentActivity.startChildActivity("EditActivity", edit);
				me.startActivityForResult( intent, 5 );
		}
		catch(Exception e){}
	}
	 private void callSwitchLang() {
	  
	    Locale[] locales = Locale.getAvailableLocales();
		for(int i = 0; i<locales.length; i++)
			System.out.println("LOCALE: "+locales[i]);
		// Locale locale = new Locale("mr");
		// Locale.setDefault(locale);
		// Configuration config = new Configuration();
		// config.locale = locale;
		// getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics()); 
		//onCreate(null);
	}
	
	public class EfficientAdapter extends BaseAdapter implements Filterable {
	
		public String[][] result;
		public int resultSetLength;
		
		public EfficientAdapter(Context context, String[][] resultSet)
		{
			mInflater = LayoutInflater.from( context );
			result = new String[resultSet.length][];
			result=resultSet;
			resultSetLength = resultSet.length;
			
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
					
			ViewHolder holder;
			if (convertView == null)
			{	
				convertView = mInflater.inflate(com.best.ui.R.layout.listthreetext, null);
				holder = new ViewHolder();
				holder.stop_name = (TextView) convertView.findViewById(com.best.ui.R.id.stopname);
				holder.stop_name.setTypeface(Best.m_marathi_typeface);
				holder.dep_time = (TextView) convertView.findViewById(com.best.ui.R.id.dep);
				
				convertView.setTag(holder);
			} 
			else 
				holder = (ViewHolder) convertView.getTag();
			
			holder.stop_name.setText(Html.fromHtml("<font color = 'DarkSlateBlue'>"+result[position][1]+"</font>"+"<font color = 'black'>" +" TO "+ "</font>"+"<font color = 'DarkSlateBlue'>"+result[position][3]+"</font>"));
			//_startStopID = result[position][1];
			holder.stop_name.setTextSize(15);
			holder.dep_time.setTextSize(12);
			String timeDiff = Funcs.subtractTime(result[position][4], Funcs.getCurrentTime(true));
			System.out.println("==Current Time = "+Funcs.getCurrentTime(true)+"---Search Time= "+result[position][4]);
			
			String[] timeDiffSplit = timeDiff.split( ":" );
			if( (Integer.parseInt( timeDiffSplit[ 0 ] )) > 0)
				holder.dep_time.setText("("+timeDiffSplit[ 0 ]+" hr)");
			else if( (Integer.parseInt( timeDiffSplit[ 1 ] )) > 0)
				holder.dep_time.setText("("+timeDiffSplit[ 1 ]+" min)");
			else
				holder.dep_time.setText("");
				
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				    	
					((EditText) me.findViewById(com.best.ui.R.id.fromtxt)).setText(result[position][1]);
					_sourceStopId = result[position][0];
					flagSourceSel = 1;
					
					((EditText) me.findViewById(com.best.ui.R.id.totxt)).setText(result[position][3]);
					_destStopId = result[position][2];
					flagDestSel = 1;
				}
			});

			return convertView;
		}

		public class ViewHolder {
			TextView stop_name;
			TextView dep_time;
		}
	
		@Override
		public Filter getFilter() {
			return null;
		}
		 
		@Override
		public int getCount() {
			return result.length;
		}
				 
		@Override
		public Object getItem(int position) {
			return result[position];
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}			
	}

	public class EfficientAdapterForNear extends BaseAdapter implements Filterable {
	
		public String[][] result;
		
		public EfficientAdapterForNear(Context context, String[][] resultSet)
		{
			if(resultSet!= null)
			{
				mInflater = LayoutInflater.from( context );
				result = new String[resultSet.length][];
				result=resultSet;
			}
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
	
				if (convertView == null)
				{	
					convertView = mInflater.inflate(com.best.ui.R.layout.list_route, null);
				
					holder = new ViewHolder();
					holder.head = (TextView) convertView.findViewById(com.best.ui.R.id.bushead);
					//..m..holder.blue_bus = (ImageView) convertView.findViewById(com.best.ui.R.id.busBlue);
					holder.routeName = (TextView) convertView.findViewById(com.best.ui.R.id.routename);
					
					convertView.setTag(holder);
				} 
				else 
					holder = (ViewHolder) convertView.getTag();
			
				 holder.head.setText( result[position][2] );
				 holder.routeName.setText( result[position][3] );
			//	convertView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
				convertView.setOnClickListener(new OnClickListener() {
					 
					@Override
					public void onClick(View v) {
			
						Best.log("CLICKED POSITION :"+ position);
						Best.log("PASSED TRIP ID :"+result[position][1]+" PASSED Long NAME :"+result[position][2]+"PASSED ROUTE NAME :"+result[position][3]);
						
						me.setContentView( com.best.ui.R.layout.routes );
						contentChanged = true;
						nearByBusClick = true;
						_routeLongName = result[position][2];
						_routeName = result[position][3];
						_tripId = result[position][1];
						//_freq = result[position][4];
						//NearTripRoute object = new NearTripRoute();
						NearTripRoute.init(m_context,result[position][1],result[position][3],result[position][2],true);
					
				}
				});
				return convertView;
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

 