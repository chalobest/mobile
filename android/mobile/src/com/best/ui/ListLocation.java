////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.best.data.GTStore_sqlite;
import com.best.util.TransitionEffect;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.MathContext;

public class ListLocation extends ListActivity
{
	public static ListActivity me = null;
	public static Context m_context;
	public EfficientAdapter adapter;
	public static LayoutInflater mInflater;
	public int EditTextNo;
	public static String[] stopNames;
	public static String[] stopsRoad;
	public static String[] stopsArea;
	public static String[] stopID;

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

		Bundle b = getIntent().getExtras(); 
		stopID = b.getStringArray( "StopID" );
		stopNames = b.getStringArray( "Stops" );
		stopsRoad = b.getStringArray( "stopRoad" );
		stopsArea = b.getStringArray( "stopArea" );
		EditTextNo = b.getInt( "EditText" );

		if( EditTextNo == Find.M_FROM_LIST_CAPTION )
		{
			setTitle(com.best.ui.R.string.fromListTitle);
			
			}
		else if( EditTextNo == Find.M_TO_LIST_CAPTION )
			setTitle(com.best.ui.R.string.toListTitle);
		else if( EditTextNo == Find.M_FROM_NEAR_BY_LIST_CAPTION || EditTextNo == Find.M_TO_NEAR_BY_LIST_CAPTION )
			setTitle(com.best.ui.R.string.nearByListTitle);
			
			
			   
        		
		try{
			if(stopNames.length>0)
			{
				adapter = new EfficientAdapter(m_context,stopNames);
				me.setListAdapter(adapter);	
			}
		}catch(Exception e){}
	}
		
	public  class EfficientAdapter extends BaseAdapter implements Filterable {
		
		private Context context;
		 
		public EfficientAdapter(Context context , String[] stopNames)
		{
			mInflater = LayoutInflater.from(context);
			this.context = context;
		}
				 
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null)
			{	
				convertView = mInflater.inflate(com.best.ui.R.layout.listtwotextnbtn, null);
			
				holder = new ViewHolder();
				holder.stopName = (TextView) convertView.findViewById(com.best.ui.R.id.stopname);
				holder.stopRoad = (TextView) convertView.findViewById(com.best.ui.R.id.stopRoad);
				holder.stopArea = (TextView) convertView.findViewById(com.best.ui.R.id.stopArea);
				holder.btnMap = (Button) convertView.findViewById(com.best.ui.R.id.map);
				holder.btnMap.setText( context.getString(R.string.txtMap) );
				holder.btnMap.setTypeface( Best.m_marathi_typeface ); 
				holder.btnMap.setOnClickListener(new OnClickListener() {
				private int pos = position;
				 
					@Override
					public void onClick(View v) {
						String[][] resultset = null;
						resultset = GTStore_sqlite.getLatitudeLongitude( stopID[ position ] );
						if(resultset != null)
						{
							String latitude = resultset[0][1];
							String longitude = resultset[0][2];
							
							Intent intent = new Intent(m_context, Map.class);
							Bundle bundle = new Bundle(); 
							bundle.putBoolean( "showSingle", true );
							
							bundle.putBoolean( "showMyPlace", ( EditTextNo == Find.M_FROM_NEAR_BY_LIST_CAPTION || EditTextNo == Find.M_TO_NEAR_BY_LIST_CAPTION ) );
							
							bundle.putString( "lat", latitude );
							bundle.putString( "long", longitude );
							intent.putExtras( bundle );
							me.startActivityForResult( intent, 5 );
						}
						else
						 System.out.println("CANNOT DISPLAY MAP");
				}
				});
				
				convertView.setTag(holder);
			} 
			else 
				holder = (ViewHolder) convertView.getTag();
			// String strDistance = "";
			// Double d = new Double( distance[ position ] );
			// if(d < 500)
			// {
				// strDistance = ( new BigDecimal( d.doubleValue() ).round(new MathContext( 2, RoundingMode.HALF_UP ) ).toString() )+" km";
				// if( d < 1 )
				// {
					// d = d * 1000;
					// strDistance = ( new BigDecimal( d.doubleValue() ).intValue() )+" m";
				// }	
			// }
			
			   holder.stopName.setTypeface(Best.m_marathi_typeface);	
			holder.stopName.setText( stopNames[position] );
			holder.stopRoad.setText( stopsRoad[position] );
			holder.stopArea.setText( stopsArea[position] );
			//holder.stopDistance.setText( strDistance );
			
			convertView.setOnClickListener( new OnClickListener() {
			
					@Override
					public void onClick(View view)
					{					      
						Bundle bundle = new Bundle();
						bundle.putString( "frmLoc" , stopNames[ position ] );
						bundle.putInt( "position" , position );
						bundle.putInt( "EditText" , EditTextNo );
						bundle.putString( "stopId" , stopID[ position ] );
						
						
						Intent mIntent = new Intent();
						mIntent.putExtras( bundle );

						int res = 0;
						res = ( ( EditTextNo == Find.M_FROM_LIST_CAPTION || EditTextNo == Find.M_FROM_NEAR_BY_LIST_CAPTION )?( 1 ):( 2 ) );
						Best.log("RES VALUE :"+res);
						setResult( res, mIntent );
						finish();
					}
				});
			return convertView;
		}
			
		public class ViewHolder {
		TextView stopName;
		TextView stopRoad;
		TextView stopArea;
		Button btnMap;
		}
				 
		@Override
		public Filter getFilter() {
		return null;
		}
		 
		@Override
		public int getCount() {
		return stopNames.length;
		}
				 
		@Override
		public Object getItem(int position) {
		return stopNames[position];
		}
		
		@Override
		public long getItemId(int position) {
		return 0;
		}			 
	}
	
}