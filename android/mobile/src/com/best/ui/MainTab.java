////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.res.Resources;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import 	android.widget.TabWidget;
import android.graphics.Color;

import java.util.Locale;

public class MainTab extends TabActivity
{
	public static TabHost _mainTab = null;
	public static LayoutInflater layoutInflater;
	public static Activity me = null;
	public TextView tv;
	public static Context m_context;

	public static String M_FIND_TAB_ID = "find";
	public static String M_BUS_TAB_ID = "bus";
	public static String M_STOP_TAB_ID = "stop";
	public static String M_ROAD_TAB_ID = "road";
	public static String M_AREA_TAB_ID = "area";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		me = this;
		m_context = this;
		setContentView(R.layout.maintab);
		Resources res = getResources();
		final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
		
		TabHost.TabSpec spec;
		Intent intent;
		
		layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final TextView tab1ViewRed = (TextView)layoutInflater.inflate(com.best.ui.R.layout.myroute_view, null); 
		
		// BitmapDrawable bitDrawable = new BitmapDrawable();
		// bitDrawable = writeOnDrawable(R.drawable.bus_red2, "BUS" );
		
		intent = new Intent().setClass(this, Find.class);
		spec = tabHost.newTabSpec(M_FIND_TAB_ID ).setIndicator( tab1ViewRed ).setContent(intent);
		tabHost.addTab( spec );
		
		//spec = tabHost.newTabSpec(M_FIND_TAB_ID ).setIndicator( Html.fromHtml("<font color = 'Red'>"+"My Route"+"</font>")).setContent(intent);
		//spec = tabHost.newTabSpec(M_FIND_TAB_ID ).setIndicator( "My Route",res.getDrawable(R.drawable.ic_tab_new) ).setContent(intent);
		
		intent = new Intent().setClass(this, Buses.class);
		
		spec = tabHost.newTabSpec(M_BUS_TAB_ID ).setIndicator( "Buses",res.getDrawable(R.drawable.tab_bus) ).setContent(intent);
		//spec = tabHost.newTabSpec(M_BUS_TAB_ID ).setIndicator( "", bitDrawable).setContent(intent);
		tabHost.addTab( spec );
		
		intent = new Intent().setClass(this, Stops.class);
		spec = tabHost.newTabSpec(M_STOP_TAB_ID ).setIndicator( "Stops",res.getDrawable(R.drawable.tab_stops) ).setContent(intent);
		tabHost.addTab( spec );
		
		// intent = new Intent().setClass(this, EmptyActivity.class);
		// spec = tabHost.newTabSpec(M_ROAD_TAB_ID ).setIndicator( "",res.getDrawable(R.drawable.tab_road) ).setContent(intent);
		// tabHost.addTab( spec );
		
		// intent = new Intent().setClass(this, EmptyActivity.class);
		// spec = tabHost.newTabSpec(M_AREA_TAB_ID ).setIndicator( "",res.getDrawable(R.drawable.tab_area) ).setContent(intent);
		// tabHost.addTab( spec );
		
		TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		final int tabChildrenCount = tabWidget.getChildCount();
		View currentView;
		for (int i = 0; i < tabChildrenCount; i++) {
		    currentView = tabWidget.getChildAt(i);
		    LinearLayout.LayoutParams currentLayout =
		        (LinearLayout.LayoutParams) currentView.getLayoutParams();
		    currentLayout.setMargins(0, 0, 5, 0);
		}
		tabWidget.requestLayout();
		
		tabHost.setCurrentTab(0);
		//tabHost.getTabWidget().setDividerDrawable(R.drawable.back);
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#F2F2F2"));                
        }
		//// //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#dcdcdc"));//F9F9F9
		tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#9f9f9f"));
		tabHost.setOnTabChangedListener(new OnTabChangeListener(){
			@Override
			public void onTabChanged(String tabId) {
				tv = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(com.best.ui.R.id.txtmyroute);
				for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		        {
		            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#F2F2F2"));                
		        }
				//// //tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#dcdcdc"));
				 tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#9f9f9f"));
				if( M_FIND_TAB_ID.equals( tabId ) ) {
					tv.setTextColor(Color.parseColor("#fb0300"));
					System.out.println("MY ROUTE SELECTED");
				}
				else {
					tv.setTextColor(Color.parseColor("#666666"));
					System.out.println("MY ROUTE UNSELECTED");
				}
			}
		});
		
	}
	// public BitmapDrawable writeOnDrawable(int drawableId, String text){

        // Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        // Paint paint = new Paint(); 
        // paint.setStyle(Style.FILL);  
        // paint.setColor(Color.BLACK); 
        // paint.setTextSize(18); 

        // Canvas canvas = new Canvas(bm);
        // canvas.drawText(text, bm.getHeight(),bm.getHeight()+bm.getHeight()/2, paint);

        // return new BitmapDrawable(bm);
    // }
		   
		public static void finish1()
		{
            me.finish();
		}
}