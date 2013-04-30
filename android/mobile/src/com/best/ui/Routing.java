////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Filterable;
import android.widget.Filter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.text.Html;
import android.content.Intent;
import android.content.Context;
import com.best.util.Funcs;

public class Routing  {

	//public static ListActivity me;
	public static Context m_context;
	public static LayoutInflater mInflater;
	public static String scrStopId , destStopId;
	public static String sourceStopName = "";
	public static String destStopName = "";
	public static String[] routes , itenDescArray ,busHead, totalDist,no_Stops ,busFreq ,tripId = new String[5];
	public static boolean routeDisplayed = false;

	public static void init(Context context)
	{
		m_context = context;
		routeDisplayed = false;
		tripId = Find.tripIdList.toArray( new String[ Find.tripIdList.size() ] ) ;
        itenDescArray = Find.routeNamesList.toArray( new String[ Find.routeNamesList.size() ] ) ; //my description values
		busHead = Find.busHeadList.toArray( new String[ Find.busHeadList.size() ] ) ;
		busFreq = Find.busFreqList.toArray( new String[ Find.busFreqList.size() ] ) ;
		totalDist =  Find.totalDistList.toArray( new String[ Find.totalDistList.size() ] ) ;
		no_Stops = Find.no_StopsList.toArray( new String[ Find.no_StopsList.size() ]) ;
		scrStopId = Find._sourceStopId;
		destStopId = Find._destStopId;
		sourceStopName = Find.sourceStop;
		destStopName = Find.destStop;
		((TextView) ((Activity)m_context).findViewById(com.best.ui.R.id.search_for)).setTypeface(Best.m_marathi_typeface);
		((TextView) ((Activity)m_context).findViewById(com.best.ui.R.id.search_for)).setText(Html.fromHtml("<font color = 'Red'>Search Results for : </font><font color = 'Black'>"+sourceStopName+" to "+destStopName+"</font>"));
		
		 EfficientAdapter adapter = new EfficientAdapter( m_context );
		 try{
			 if(adapter != null && context != null && routeDisplayed == false)
			 {
				
				( ( ListView )((Activity)m_context).findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
			 }
			else if(context != null)
				Best.log("ADAPTER IS NULL");
			else
				Best.log("CONTEXT IS NULL");
		}
		catch(Exception e){e.printStackTrace();}
	}



public static class EfficientAdapter extends BaseAdapter implements Filterable {
	
		
		public EfficientAdapter(Context context)
		{
			if(itenDescArray!= null && itenDescArray.length > 0)
			{
				mInflater = LayoutInflater.from( context );
			}
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if(itenDescArray!= null)
			{
				if (convertView == null)
				{	
					convertView = mInflater.inflate(com.best.ui.R.layout.list_button, null);
				
					holder = new ViewHolder();
					holder.itenarydescString= (TextView) convertView.findViewById(com.best.ui.R.id.routename);
					holder.iteration = (TextView) convertView.findViewById(com.best.ui.R.id.iterationString);
					//holder.total_dist = (TextView) convertView.findViewById(com.best.ui.R.id.totaldist);
					// holder.bus_freq = (TextView) convertView.findViewById(com.best.ui.R.id.bus_freq);
					//holder.no_of_stops = (TextView) convertView.findViewById(com.best.ui.R.id.stopsno);
					//mholder.blue_bus = (ImageView) convertView.findViewById(com.best.ui.R.id.busBlue);
					convertView.setOnClickListener(new OnClickListener() {
					 
						@Override
						public void onClick(View v) {
                            System.out.println("position "+position);
							Bundle bundle = new Bundle();
							bundle.putString( "tripID" , tripId[position] );
							bundle.putString( "sourceID" , scrStopId);
							bundle.putString( "destID" ,destStopId);
							bundle.putString("routeLongName",busHead[position]);
							bundle.putString("routeName",itenDescArray[position]); //my desc string
							bundle.putString("freq",busFreq[position]);
                            bundle.putInt("position",position);
							bundle.putBoolean( "showFullRoute" ,false);
							Intent intent = new Intent(m_context, Otpdescdetail.class);//M
							routeDisplayed = true;
							intent.putExtras( bundle );
							((Activity)m_context).startActivityForResult(intent,6);

						
					}
					});
					
					convertView.setTag(holder);
				} 
				else 
				{
					holder = (ViewHolder) convertView.getTag();
				}
				
				String _itenarydesc = itenDescArray[position];
				String _iteration = String.valueOf(position);
				String _betweenStopsNum = no_Stops[position];
                String freq = freq = (busFreq[position]);
				//....mansi holder.bus_freq.setText(Funcs.secondsToMinHr(freq));
				// int min = freq / 60;
				// if(min > 0)
				// {
					// if(freq % 60 > 0)
						// holder.bus_freq.setText(min +"min "+freq % 60 + " sec");
					// else
						// holder.bus_freq.setText(min +"min ");
				// }
				// else
					// holder.bus_freq.setText(freq + " sec");

				int _distance = 0;
				try{ _distance = Integer.parseInt( totalDist[position] ); }catch(Exception e){}
				
				holder.itenarydescString.setText( _itenarydesc );
				holder.iteration.setText( _iteration+":" );
				if( _distance > 1000)
				{
					if( _distance == 0 )
					{
						//..mansi holder.total_dist.setText( "NA");
					}
					else
					{
						String dist = Double.toString(Funcs.roundDouble((Double.parseDouble(_distance+"")/1000), 1));
						//mansi..holder.total_dist.setText( dist +" km");
					}	
				}
				else ;
					// mansi..holder.total_dist.setText( _distance +" m");
				
				if( _betweenStopsNum == null )
				{
					//holder.no_of_stops.setText( "NA" );
				}
				else
				{
					//if(Integer.parseInt(_betweenStopsNum) == 1);
						//holder.no_of_stops.setText( _betweenStopsNum + " Bus stop" );
					//else ;
						//..mansi /holder.no_of_stops.setText( _betweenStopsNum + " Bus stops" );
				}		
				return convertView;
			}
			
				return (null);
		}
			
		public class ViewHolder {
		TextView itenarydescString;
		TextView iteration;
		//ImageView blue_bus;
		//TextView total_dist;
		//TextView bus_freq ;
		//TextView no_of_stops;
		}
				 
		@Override
		public Filter getFilter() {
			return null;
		}
		 
		@Override
		public int getCount() {
			if(itenDescArray != null)
				return itenDescArray.length;
			else
				return 0;
		}
				 
		@Override
		public Object getItem(int position) {
			if(itenDescArray != null)
				return itenDescArray[position];
			else
				return (null);
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}			 
	}
	
}