////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.graphics.Typeface;
import android.content.DialogInterface;
import com.best.data.DBHandle;
import com.best.data.GTStore_sqlite;
import com.best.util.TransitionEffect;

public class Best extends Activity
{
	public static Activity me;
	public static boolean result=false;
	public static String[] stopNames=null;
	public static ProgressDialog dialog;
	public static Context m_context ;
	public static boolean m_position_updation_on = false;
	public static LocationManager m_locationManager = null;
	public static Location _location;
	public static int _activityRequestCode = 1;
	public static String m_latitude = "";
	public static String m_longitude = "";
	public static Typeface m_marathi_typeface=null;

    public static boolean language = true; //false means marathi and true means english

    static Progressdialog progressbar = new Progressdialog();
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);


	    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.DONUT)
			TransitionEffect.callOverridePendingTransition(this);
	    me = this;
		log("BEST: onCreate called");
        getUpdatedPosition();
		DBHandle.init( me );	
		loadResources();
		Intent intent = new Intent( ( Context )me,SplashScreen1.class );
		me.startActivityForResult( intent,_activityRequestCode );
		LoadThread loadThread = new LoadThread( loadHandler );
		loadThread.start();
    }
	
	private void loadResources()
	{
		m_marathi_typeface = Typeface.createFromAsset( me.getAssets(), "fonts/mangal.ttf");
	}
	
	@Override
    protected void onResume() {
        super.onResume();

		//getUpdatedPosition();
		log("BEST: onResume called");
    }
	
	public Handler loadHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			boolean allOK = dataBundle.getBoolean( "allOK" );
			if( allOK )
			{
				GTStore_sqlite.init(me);
				Intent intent = new Intent();
				intent.setClass( me, MainTab.class);
				startActivity( intent );
				finishAll();
			}
			else
				showErrorAndExit( "Sorry unable to start application." );
        }
    };
	
	private class LoadThread extends Thread 
	{
        Handler _handler;
        
		LoadThread(Handler handler ) 
		{
            _handler = handler;
        }
       
        public void run() 
		{
			Bundle dataBundle = new Bundle(); 
			dataBundle.putBoolean( "allOK", DBHandle.dbFileReady() );
		    Message msg = _handler.obtainMessage();
			msg.setData( dataBundle );
			_handler.sendMessage( msg );
        }
    }
	
	public static void finishAll()
	{
		try{ me.finishActivity(_activityRequestCode);

            }catch(Exception e){}

        me.finish();
	}
	
	public static void exit(Context context)
	{
        System.out.println("in best exit");
        ExitDialog exit = new ExitDialog();
		exit.show(context);
    }
	
	public static void showError(Context context, String string)
	{
        CustomDialog custom = new CustomDialog();
		custom.show(context,string);

	}
	

	
	public static void showMessage(Context context, String msg,String title)
	{
        CustomDialog custom = new CustomDialog();
		custom.show(context, msg, title);
	/*ew AlertDialog.Builder( context )
		  .setMessage( msg )
		  .setTitle( title )
		  .setCancelable( true )
		  .setNeutralButton( "OK",
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton){  }
			})
		  .show();*/
	}
	
	/*public static void showMessage(Context context, String msg,String title)
	{Custom_dialog custom = new Custom_dialog();
		custom.show(context,msg,title);
		/*new AlertDialog.Builder( context )
		  .setMessage( msg )
		  .setTitle( title )
		  .setCancelable( true )
		  .setNeutralButton( "OK",
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton){  }
			})
		  .show();
	}
*/	

	public static void showProcessing(Context context,String title, String msg)
	{
		
		progressbar.show(context);
		//dialog = ProgressDialog.show( context, title, msg, true );	 
     }
		 
	public static void dissmissProcessing()
	{
	
		progressbar.dismiss();
		//dialog.dismiss();

	}
	
	public static void log(String msg)
	{
		System.out.println( "USER DEBUG : " + msg );
	}
	
	
	/////////GPS
	public void getUpdatedPosition()
	{
		try
		{
			if( !m_position_updation_on )
			{
				m_latitude = "";
				m_longitude = "";
		
				m_position_updation_on = true;
				
				if( m_locationManager == null )
					m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				
				m_locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000, 0, m_networkLocationListener );
				m_locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 0, m_gpsLocationListener );
			}
		}
		catch(Exception e)
		{
            System.out.println("e  "+e);
            e.printStackTrace();

            showMessage(this,e.toString(),m_context.getString(R.string.error));}
	}
	
	public static void cancelUpdatePosition()
	{
		log("GPS: Cancelling UPDATE POSITION");
		m_position_updation_on = false;
		cancelNetWorkUpdatePosition();
		cancelGPSUpdatePosition();
		log("GPS: Canceled UPDATE POSITION");
	}
	
	public static void cancelNetWorkUpdatePosition()
	{
		m_locationManager.removeUpdates( m_networkLocationListener );
	}
	
	public static void cancelGPSUpdatePosition()
	{
		m_locationManager.removeUpdates( m_gpsLocationListener );
	}
	
	private static LocationListener m_gpsLocationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) { cancelGPSUpdatePosition(); }
        @Override
        public void onLocationChanged(Location location) {
            try{ m_latitude = String.format("%9.6f", location.getLatitude()); }catch(Exception e){  } 		
			try{ m_longitude = String.format("%9.6f", location.getLongitude()); }catch(Exception e){  }
			log("Registered GPS LocationListener with values: Lat="+m_latitude+"--Long="+m_longitude);
			//showMessage(me,"Registered GPS LocationListener with values: Lat="+m_latitude+"--Long="+m_longitude,"GPS");
			cancelUpdatePosition();
        }
    };

    private static LocationListener m_networkLocationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) { cancelNetWorkUpdatePosition(); }
        @Override
        public void onLocationChanged(Location location) {
            try{ m_latitude = String.format("%9.6f", location.getLatitude()); }catch(Exception e){  }
			try{ m_longitude = String.format("%9.6f", location.getLongitude()); }catch(Exception e){  }
			//showMessage(me,"Registered GPS LocationListener with values: Lat="+m_latitude+"--Long="+m_longitude,"GPS");
			cancelUpdatePosition();
        }
    };
	
	public static boolean checkGPS(Context context)
	{
		if( !m_locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && !m_locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) )
		{
			showMessage( context, context.getString(R.string.gps_network_not_workngs),context.getString(R.string.service)  );
			return( false );
		}	
		return( true );
	}
	
	public static void showErrorAndExit(String msg)
	{
		new AlertDialog.Builder( me )
		  .setMessage( msg )
		  .setTitle( "Error" )
		  .setCancelable( true )
		  .setNeutralButton( "Exit" ,
			 new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton){ Best.finishAll(); }
			 })
		  .show();
	}
	

}
