////////////////////////////////////////////////
//
// License: GPLv3
//
//

//
package com.best.ui;

import java.util.List;
import org.osmdroid.views.util.constants.MapViewConstants;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.Paint;
import android.graphics.Path;
import android.content.Context;
import android.view.KeyEvent;

public class Map extends Activity implements MapViewConstants {

	private MapView mapView;
	private MyLocationOverlay mLocationOverlay;
	private MapController mapController;
	public static GeoPoint geoPoint1 = null;
	public static GeoPoint geoPoint2 = null;
	public static GeoPoint centerGPoint = null;
	public static GeoPoint averagePoint = null;

	public static boolean m_srcIsNew = true;
	public static boolean m_markerShown = false;

	public static Context m_context;
	public static boolean m_showSingle = true;
	public static boolean showMyPlace = false;
	public boolean isFirstMapping = true;
	
	public static String[] stopsGeoX = null;
	public static String[] stopsGeoY = null;
	
	public int minLatitude = 0;
	public int maxLatitude = 0;
	public int minLongitude= 0;
	public int maxLongitude = 0;
	
	public int minimumLat = 99999999;
	public int maxxLat = 0;
	public int minimumLong= 99999999;
	public int maxxLong = 0;
	
	public static int bbLatSpan = 0;
	public static int bbLonSpan = 0;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		this.setContentView( com.best.ui.R.layout.mapstops );
		m_context = this;
		try{
			mapView = ( MapView ) findViewById( com.best.ui.R.id.mapview );
			mapView.setBuiltInZoomControls( true );
			
			mapController = mapView.getController();
			Bundle bundle = getIntent().getExtras(); 
			if( ( m_showSingle = bundle.getBoolean( "showSingle" ) ) == true )
			{
				Double lat = Double.parseDouble( bundle.getString( "lat" ) );
				Double lon = Double.parseDouble( bundle.getString( "long" ) );
				geoPoint1 = new GeoPoint( lat, lon );
				
				try{
					showMyPlace = (boolean)bundle.getBoolean( "showMyPlace" );
					if( showMyPlace && Best.m_latitude.length() > 0 && Best.m_longitude.length() > 0 )
					{
						Double latDouble = Double.valueOf( Best.m_latitude );//Double.valueOf( "40.45939" );
						Double longDouble = Double.valueOf( Best.m_longitude );//Double.valueOf( "-3.78429" );
						geoPoint2 = new GeoPoint( latDouble, longDouble );
					}
				}catch(Exception e){}	
				
				averagePoint = geoPoint1;
			}
			else
			{
				stopsGeoX = (String[])bundle.getSerializable("StopsGeoX");
				stopsGeoY = (String[])bundle.getSerializable("StopsGeoY");
				centerGPoint = new  GeoPoint( Double.parseDouble(stopsGeoX[stopsGeoX.length - 1]), Double.parseDouble(stopsGeoY[stopsGeoY.length - 1]) );
				//ArrayList<GeoPoint> geoPointsList = new ArrayList<GeoPoint>();
				for(int i = 0;i < stopsGeoX.length; i++)
				{
					GeoPoint geoPoint = new  GeoPoint( Double.parseDouble(stopsGeoX[i]), Double.parseDouble(stopsGeoY[i]) );
					
					int latitude = geoPoint.getLatitudeE6();
					int longitude = geoPoint.getLongitudeE6(); 
					minimumLat = ( latitude > minimumLat ) ? minimumLat : latitude;
					maxxLat = ( latitude < maxxLat ) ? maxxLat : latitude;
					
				    minimumLong = ( longitude > minimumLong ) ? minimumLong : longitude;
					maxxLong = ( longitude < maxxLong ) ? maxxLong : longitude;
				
				}
				geoPoint1 = new  GeoPoint( Double.parseDouble(stopsGeoX[0]), Double.parseDouble(stopsGeoY[0]) );
				geoPoint2 = new  GeoPoint( Double.parseDouble(stopsGeoX[stopsGeoX.length - 1]), Double.parseDouble(stopsGeoY[stopsGeoY.length - 1]) );
			}
			
			if( geoPoint1 != null && geoPoint2 != null )
			{
				int geoPointLat1 = geoPoint1.getLatitudeE6();
				int geoPointLon1 = geoPoint1.getLongitudeE6();
				
				int geoPointLat2 = geoPoint2.getLatitudeE6();
				int geoPointLon2 = geoPoint2.getLongitudeE6();
		 
				 minLatitude = ( geoPointLat1 > geoPointLat2 ) ? geoPointLat2 : geoPointLat1;
				 maxLatitude = ( geoPointLat1 < geoPointLat2 ) ? geoPointLat2 : geoPointLat1;
				
				 minLongitude = ( geoPointLon1 > geoPointLon2 ) ? geoPointLon2 : geoPointLon1;
				 maxLongitude = ( geoPointLon1 < geoPointLon2 ) ? geoPointLon2 : geoPointLon1;
				 
				 System.out.println("==minLatitude="+minLatitude+"==maxLatitude="+maxLatitude+"==minLongitude="+minLongitude+"==maxLongitude="+maxLongitude);
				 System.out.println("==minimumLat="+minimumLat+"==maxLatitude="+maxxLat+"==minLongitude="+minimumLong+"==maxLongitude="+maxxLong);
				
				System.out.println("=== maxLatitude - minLatitude ="+ (maxLatitude - minLatitude)+"==maxLongitude - minLongitude ="+(maxLongitude - minLongitude ) );
				int avgLat = ( maxLatitude + minLatitude )/2;
				int avgLong = ( maxLongitude + minLongitude )/2 ;
				
				averagePoint = GeoPoint.fromCenterBetween( geoPoint1, geoPoint2 );//= new GeoPoint( avgLat, avgLong );
				
			}
			else
			{
				mapController.setZoom( 18 ); //always shud come befor setCenter and animate to
				mapController.setCenter( averagePoint );
				mapController.animateTo( averagePoint );
			}
				
			
			MapOverlay mapOverlay = new MapOverlay( m_context, mapView );
			List<Overlay> listOfOverlays = mapView.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add( mapOverlay );
			mapView.invalidate();
			
		}catch(Exception e){ e.printStackTrace(); Best.showError( m_context, m_context.getString(R.string.errNoMap) );}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{//If back was pressed
	    if (keyCode==KeyEvent.KEYCODE_BACK)
	    {
            System.out.println("back of map clicked");
			back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	public void back()
	{
		finish();
	}
	// @Override
	// public void onBackPressed() {
		
		// finish();
	// }
	
	
	class MapOverlay extends org.osmdroid.views.overlay.MyLocationOverlay
    {
		public MapOverlay(Context context, MapView mapview)
		{
			super( context, mapview );
		}
		
		@Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) 
        {
			super.draw(canvas, mapView, shadow);
			
			Point screenPts1, screenPts2;
			Bitmap bmp = BitmapFactory.decodeResource( getResources(), com.best.ui.R.drawable.bus_small1 );  
			
			screenPts1 = new Point();
			mapView.getProjection().toPixels( geoPoint1, screenPts1 );
			canvas.drawBitmap( bmp, screenPts1.x - 6, screenPts1.y - 6, null ); 
			
			screenPts2 = new Point();
			if( !m_showSingle )
			{
				mapView.getProjection().toPixels(geoPoint2, screenPts2);
				canvas.drawBitmap( bmp, screenPts2.x - 6, screenPts2.y - 6, null ); 
				
				if(stopsGeoX != null && stopsGeoY != null)
				{
					Paint paint = new Paint();
					paint.setColor(0xffff0000);
					paint.setStrokeWidth(3);
					Path path = new Path();
					GeoPoint geopointStart = new GeoPoint(Double.parseDouble(stopsGeoX[0]) ,Double.parseDouble(stopsGeoY[0]));
					mapView.getProjection().toPixels( geopointStart, screenPts1 );
					path.moveTo(screenPts1.x, screenPts1.y);
					for(int index = 1; index < stopsGeoX.length;index++)
					{ 
						GeoPoint nextGeoPoint=  new GeoPoint(Double.parseDouble(stopsGeoX[index]) ,Double.parseDouble(stopsGeoY[index]));
						mapView.getProjection().toPixels( nextGeoPoint, screenPts1 );
						path.lineTo(screenPts1.x , screenPts1.y);
					}
					paint.setStyle(Paint.Style.STROKE);
					canvas.drawPath (path, paint);	
				}
			}
			else
			{
				if( geoPoint2 != null && showMyPlace )
				{
					Bitmap bmpUser = BitmapFactory.decodeResource( getResources(), com.best.ui.R.drawable.user );  
					
					mapView.getProjection().toPixels( geoPoint2, screenPts2);
					canvas.drawBitmap( bmpUser, screenPts2.x - 5, screenPts2.y - 5, null ); 
					
				}	
			}
			if( isFirstMapping == true && geoPoint1 != null && geoPoint2 != null)
			{
				
				setZoomAndCentre();
				isFirstMapping = false;
			}
			mapView.invalidate();
        }
		public void setZoomAndCentre()
		{
			mapController.zoomToSpan(( maxxLat - minimumLat ),( maxxLong - minimumLong )) ;
			//mapController.zoomToSpan(( maxLatitude - minLatitude ),( maxLongitude - minLongitude )) ;
			System.out.println("=== maxxLat - minimumLat ="+ (maxxLat - minimumLat)+"==maxxLong - minimumLong ="+(maxxLong - minimumLong ) );
			System.out.println("===mapView.getZoomLevel()==="+mapView.getZoomLevel()); 
			if(m_showSingle == false && bbLatSpan != 0 && bbLonSpan != 0)
			{
				//mapController.zoomToSpan(bbLatSpan , bbLonSpan) ;
				mapController.setCenter( centerGPoint );
				mapController.animateTo( centerGPoint );
			}
			else
			{
				mapController.setCenter( averagePoint );
				mapController.animateTo( averagePoint );
			}
		
		}
	}	
}