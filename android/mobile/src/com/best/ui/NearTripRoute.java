////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.app.Dialog;
import android.view.View.OnLongClickListener;
import com.best.data.GTStore_sqlite;
import android.graphics.Color;
import android.text.Html;

public class NearTripRoute {
	public static ListActivity me;
	public static Context m_context;
	public static LayoutInflater mInflater,layoutInflater;
	
	public static String _tripID;
	public static String _routeLongName;
	public static String _routeName;
	public static String nearestStopID;
	public static int _freq;
	public static int nearByPosition = 0;
	public static Double nearestDistance = 999999.0;
	public static String selectedStopId;
	public static String selectedStopName;
	
	public static String[][] routeResult ;
	public static String[][] nearByStops = null;
	
	public static Double latDouble=0.0;
	public static Double longDouble=0.0;
	//public static String[] colors = {"#fcfcfc" , "#ececec"};
	public static String[] colors = {"#ffffff" , "#efefef"};
	public static String[] _stopLat = null;
	public static String[] _stopLon = null;
	public static PopupWindow popupWindow;
	public static Dialog dialog;
	
	public static boolean _callFromMyroute = false;
  
	public static void init(Context context, String tripID, String routeName, String routeLongName,boolean callFromMyroute)
	{  System.out.println("in init of near trip route above try");
		try{
		
		m_context = context;
		_callFromMyroute = callFromMyroute;
		_tripID = tripID;
		_routeLongName = routeLongName;
		_routeName = routeName;
		//_freq = Integer.parseInt(frequency);
		//nearByPosition = 0;
		//int min = _freq / 60;
            System.out.println("above text view of ");
		(( TextView ) ((Activity)m_context).findViewById( com.best.ui.R.id._routename )).setText(_routeName);
		(( TextView ) ((Activity)m_context).findViewById( com.best.ui.R.id._routelongname )).setText(_routeLongName);
		//(( TextView ) ((Activity)m_context).findViewById( com.best.ui.R.id._freq)).setText(Funcs.secondsToMinHr(_freq));
		// if(min > 0)
		// {
			// if(_freq % 60 > 0)
				// (( TextView ) ((Activity)m_context).findViewById( com.best.ui.R.id._freq)).setText(min +"min "+_freq % 60 + " sec");
			// else
				// (( TextView ) ((Activity)m_context).findViewById( com.best.ui.R.id._freq)).setText(min +"min ");
		// }
		// else
			// (( TextView ) ((Activity)m_context).findViewById( com.best.ui.R.id._freq)).setText(_freq + " sec");
		
		 layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View popupView = layoutInflater.inflate(com.best.ui.R.layout.popup, null);  
		 
		dialog = new Dialog(m_context);
		dialog.setContentView(popupView,new LayoutParams(350 , LayoutParams.WRAP_CONTENT));
		dialog.setTitle("Select Stop as:");
		dialog.setCancelable(true);
            System.out.println("above if of callmy route");
		if(callFromMyroute == true)
		{    System.out.println("inside first if ");
			final TextView selectAsSource = ( TextView ) dialog.findViewById( com.best.ui.R.id.as_source );
			if(selectAsSource != null)
			{   System.out.println("inside second if ");
				selectAsSource.setOnClickListener( new View.OnClickListener() {
					public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString( "frmLoc" , selectedStopName );
					bundle.putInt( "position" , 4 );
					bundle.putInt( "EditText" , 1 );
					bundle.putString( "stopId" , selectedStopId );
                        System.out.println("below bundle of inner if ");
					Find.contentChanged = false;
					Find.sourceStop = selectedStopName;
					Find._sourceStopId = selectedStopId;
					Find.flagSourceSel = 1;
					Find.callFromNearTripRoute = true;
                        System.out.println("below finf of inner if ");
                        dialog.dismiss();
					((Activity)m_context).setContentView( com.best.ui.R.layout.find );
					Find.nearByBusClick = false;
					}
				});
			}
			else
			{System.out.println("TextView NULL");}
            System.out.println("above TextView selectAsDest  ");
			final TextView selectAsDest = ( TextView ) dialog.findViewById( com.best.ui.R.id.as_dest );
			selectAsDest.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
				Bundle b = new Bundle();
					b.putString( "frmLoc" , selectedStopName );
					b.putInt( "position" , 5 );
					b.putInt( "EditText" , 2 );
					b.putString( "stopId" , selectedStopId );
                    System.out.println("below putstring");
					Find.contentChanged = false;
					Find.destStop = selectedStopName;
					Find._destStopId = selectedStopId;
					Find.flagDestSel = 1;
					Find.callFromNearTripRoute = true;
                    System.out.println("above dialog");
                    dialog.dismiss();
					((Activity)m_context).setContentView( com.best.ui.R.layout.find );
					Find.nearByBusClick = false;
					// Intent intent = new Intent(m_context, MainTab.class);  
					// ((Activity)m_context).startActivityForResult(intent,6);
					// ((Activity)m_context).finish();
					

					// setResult( 6, intent );
					// finish();
					// me.finish();
				}
			});
		}
		
		if( Best.m_latitude.length() > 0 && Best.m_longitude.length() > 0 )
		{   System.out.println("in if of Best.m_latitude.length() ");
			latDouble = Double.valueOf( Best.m_latitude );//Double.valueOf( "40.45939" );
			longDouble = Double.valueOf( Best.m_longitude );//Double.valueOf( "-3.78429" );
			nearByStops = GTStore_sqlite.getNearByStops( latDouble, longDouble);
			// for(int i = 0 ;i < nearByStops.length; i++)
			// {
				// if(nearestDistance > Double.valueOf(nearByStops[i][2]))
				// {
					// nearestStopID = nearByStops[i][0];
					// nearestDistance = Double.valueOf(nearByStops[i][2]);
				// }
			// }
		}
            System.out.println(" above TripInfoThread tripInfoThread");
		Best.showProcessing(m_context,  m_context.getString(R.string.processing), m_context.getString(R.string.please_wait) );
		TripInfoThread tripInfoThread = new TripInfoThread( tripInfoHandler );
		tripInfoThread.start();
		}
		catch(Exception e)
		{Best.showMessage(m_context,e.toString(),m_context.getString(R.string.exception));}
		
	}
	
	public static Handler tripInfoHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
			Best.dissmissProcessing();
			
			routeResult = ( String[][] )dataBundle.getSerializable( "rts" );
			if( routeResult != null &&  routeResult.length > 0)
			{
				_stopLat = new String[routeResult.length];
				_stopLon = new String[routeResult.length];
				for(int i = 0; i< routeResult.length ; i++)
				{
					_stopLat[i] = routeResult[i][3];
					_stopLon[i] = routeResult[i][4];
				}
				EfficientAdapter adapter = new EfficientAdapter( m_context,routeResult);
				//(((ListActivity)m_context).getListView()).setListAdapter( adapter );
			    ( ( ListView )((Activity)m_context).findViewById( com.best.ui.R.id.routeslist ) ).setAdapter( adapter );
				System.out.println("SDK VERSION ========="+android.os.Build.VERSION.SDK_INT );
				if(nearByStops != null)
				{
					if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) 
					{
						System.out.println("SDK VERSION = "+android.os.Build.VERSION.SDK_INT);
					//	( ( ListView )((Activity)m_context).findViewById( com.best.ui.R.id.routeslist ) ).smoothScrollToPosition (nearByPosition); //method smoothScrollToPosition is since API level 8.
					}
					System.out.println("NEAR BU POSITION="+nearByPosition);
				}
			}
			else
				Best.showMessage( m_context, m_context.getString(R.string.sorry_somthng_went_wrong), _tripID );
						
        }
    };
	
	private static class TripInfoThread extends Thread 
	{
        Handler _handler;
        
		TripInfoThread(Handler handler ) 
		{
            _handler = handler;
        }
       
        public void run() 
		{
			//code to get all routes
			Bundle dataBundle = new Bundle(); 
			dataBundle.putSerializable( "rts", GTStore_sqlite.getTripInfo(_tripID));
			Message msg = _handler.obtainMessage();
			msg.setData( dataBundle );
			_handler.sendMessage( msg );
        }
    }
	
	public static class EfficientAdapter extends BaseAdapter implements Filterable {
	
		public String[][] result;
		public int resultSetLength;
		
		public EfficientAdapter(Context context, String[][] resultSet)
		{
			mInflater = LayoutInflater.from( context );
			result = new String[resultSet.length][];
			result=resultSet;
			resultSetLength = resultSet.length;
			if(resultSet == null)
				Best.showError(context ,context.getString(R.string.txtSmthingWentWrong));
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
					
			ViewHolder holder;
			if (convertView == null)
			{	
				convertView = mInflater.inflate(com.best.ui.R.layout.list_double_line, null);
			
				holder = new ViewHolder();
				
				holder.stop_name = (TextView) convertView.findViewById(com.best.ui.R.id.stopname);
				holder.dep_time = (TextView) convertView.findViewById(com.best.ui.R.id.dep);
				holder.stop_area = (TextView) convertView.findViewById(com.best.ui.R.id.stoparea);
				holder.stop_road = (TextView) convertView.findViewById(com.best.ui.R.id.stoproad);
				
				convertView.setTag(holder);
			} 
			else 
				holder = (ViewHolder) convertView.getTag();
				
		    
		    holder.stop_name.setTypeface(Best.m_marathi_typeface);
				
			
			
				
			holder.stop_name.setText(result[position][0]);
			holder.stop_area.setText(result[position][5]);
			holder.stop_road.setText(result[position][6]); 
				
			boolean nearby = false;
			int i = 0;
			if(nearByStops != null)
			{
				
				for(i = 0; i < nearByStops.length; i++)
				{
					if(result[position][1].equals(nearByStops[i][0]))
					{
						nearby = true;
						// if(nearestDistance > Double.valueOf(nearByStops[i][2]))
						// {
							// nearestStopID = nearByStops[i][0];
							// nearestDistance = Double.valueOf(nearByStops[i][2]);
						// }
						break;
					}
				}
			}
			if(position == 0 )
			{
				holder.dep_time.setText(Html.fromHtml("<font color = 'Green'>"+"Starting point"+"</font>")); 
				//holder.stop_name.setText(Html.fromHtml("<font color = 'black'>"+"Bus starts at: "+"</font>"+"<font color = 'DarkSlateBlue'>"+result[position][0]+"</font>"));
				convertView.setBackgroundColor(Color.parseColor("#e6ffe6"));
			}
			else if(position == (resultSetLength - 1) )
			{
				holder.dep_time.setText(Html.fromHtml("<font color = 'Blue'>"+"End Station"+"</font>")); 
				convertView.setBackgroundColor(Color.parseColor("#e1e1ff"));
			}
			else if(nearby == true)
			{
				// if(nearByPosition == 0)
					nearByPosition = position;
				convertView.setBackgroundColor(Color.parseColor("#d4b6d4"));		//0xffcccccc
				holder.dep_time.setText(Html.fromHtml("<font color = 'Blue'>"+"Close to you"+"</font>"));
			}
			else
			{
				convertView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
				holder.dep_time.setText("");
			}
				
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					selectedStopId = result[position][1];
					selectedStopName = result[position][0];
					
					if(_callFromMyroute)
						dialog.show();
					//popupWindow.showAsDropDown(v);
					//popupMenu.show();
					System.out.println("Selected Stop:"+result[position][0]);
					return (true);
				}
			});

			return convertView;
		}

		public class ViewHolder {
			TextView stop_name;
			TextView dep_time;
			TextView stop_area;
			TextView stop_road;
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
	
}
		
  