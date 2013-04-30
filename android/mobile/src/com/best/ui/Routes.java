////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import com.best.data.GTStore_sqlite;
import com.best.util.TransitionEffect;
import java.util.List;
import java.util.ArrayList;
import android.view.KeyEvent;
import android.graphics.Color;
import android.text.Html;

public class Routes extends Activity {
    public static Activity me;
    public static Context m_context;
    public static LayoutInflater mInflater;
    public static String sourceId="";
    public static String destId="";
    public static String _tripID;
    public static String _routeLongName;
    public static String _routeName;
    public static String _freq;
    public static String _startStopID = "";
    public static String _endStopID = "";
    public static String[] stopGeoPoints;
    public static String[][]routeResult = null;
    public static String[] colors = {"#ffffff" , "#efefef"};

    ArrayList<String> listStopsGeoX = new ArrayList<String>();
    ArrayList<String> listStopsGeoY = new ArrayList<String>();

    public static boolean showFullRoute = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT)
            TransitionEffect.callOverridePendingTransition(this);

        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        me = this;
        m_context = this;
        me.setContentView( com.best.ui.R.layout.routes );
        mInflater = LayoutInflater.from(m_context);

        Bundle bundle = getIntent().getExtras();
        _tripID = bundle.getString( "tripID" );
        _routeLongName = bundle.getString("routeLongName");
        _routeName = bundle.getString("routeName"); //my description string.
        _freq = bundle.getString("freq");
        sourceId = bundle.getString("sourceID");
        destId = bundle.getString("destID");
        showFullRoute = bundle.getBoolean("showFullRoute");

        (( TextView ) me.findViewById( com.best.ui.R.id._routename )).setText(_routeName);
        (( TextView ) me.findViewById( com.best.ui.R.id._routelongname )).setText(_routeLongName);

        int freq = Integer.parseInt(_freq);
        int hr = freq / 60;

        //m(( TextView ) me.findViewById( com.best.ui.R.id._freq )).setText(Funcs.secondsToMinHr(freq));
        // if(hr > 0)
        // (( TextView ) me.findViewById( com.best.ui.R.id._freq )).setText(hr +"hr "+freq % 60 + " min");
        // else
        // (( TextView ) me.findViewById( com.best.ui.R.id._freq )).setText(freq + " min");
        //(( TextView ) me.findViewById( com.best.ui.R.id._freq )).setText(_freq + " min");
        init( _tripID );
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(showFullRoute == true)
            inflater.inflate(com.best.ui.R.menu.onlymap, menu);
        else
            inflater.inflate(com.best.ui.R.menu.mapfullroute, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case com.best.ui.R.id.map:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean( "showSingle", false );
                bundle1.putSerializable("StopsGeoX",listStopsGeoX.toArray( new String[ listStopsGeoX.size() ] ));
                bundle1.putSerializable("StopsGeoY",listStopsGeoY.toArray( new String[ listStopsGeoY.size() ] ));
                Intent intent = new Intent(m_context, Map.class);
                intent.putExtras( bundle1 );
                me.startActivityForResult(intent,5);

                return true;

            case com.best.ui.R.id.fullRoute:
                Intent intent1 = new Intent(m_context, Routes.class);
                Bundle bundle = new Bundle();
                bundle.putString( "tripID",_tripID  );
                bundle.putString("routeLongName",_routeLongName);
                bundle.putString("routeName", _routeName);
                bundle.putString("freq", _freq);
                bundle.putString("sourceID", sourceId);
                bundle.putString("destID", destId);
                bundle.putBoolean("showFullRoute", true);
                //bundle.putSerializable("routeResult", routeResult);
                intent1.putExtras( bundle );

                me.startActivityForResult(intent1,6);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {//If back was pressed
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            System.out.println("back of routes clicked");
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void back()
    {
        System.out.println("back of routes.java clicked");
        finish();
    }
    // @Override
    // public void onBackPressed() {

    // finish();
    // me.setContentView( com.best.ui.R.layout.routing );
    // contentChanged = true;
    // Routing.init(m_context);
    //	return;
    //}
    public void init(String tripID)
    {
        Find.directRouteDisplayed = true;
        /*...m for(int m=0;m< CTrip.legs.get(CPlan.i).v2.size();m++)
        {
            System.out.println("CPlan.i      "+CPlan.i);
            modeRoute=CTrip.get(CPlan.i).v2.get(m).toString();
            System.out.println("modeRoute         "+modeRoute);

            if(S!="")
                S=S.concat(modeRoute);
            else
                S=modeRoute;

            System.out.println("S    "+S);
            int size=legs.get(CPlan.i).v2.size();

            if(m!=size-1)
                S=S+",";
            System.out.println("size "+size);
            if(m==size-1)
            {
                return(S);
            }
        }*/
        if(showFullRoute == false)
        {
            Best.showProcessing(m_context, m_context.getString(R.string.processing), m_context.getString(R.string.please_wait) );
            TripInfoThread tripInfoThread = new TripInfoThread( tripInfoHandler );
            tripInfoThread.start();
        }
        else if( routeResult != null &&  routeResult.length > 0)
        {
            for(int index = 0; index < routeResult.length; index++)
            {
                listStopsGeoX.add( routeResult[index][3] );
                listStopsGeoY.add( routeResult[index][4] );
            }
            EfficientAdapter adapter = new EfficientAdapter( m_context,routeResult);
            //me.setListAdapter( adapter );
            ( ( ListView ) me.findViewById( com.best.ui.R.id.routeslist ) ).setAdapter( adapter );
        }
        else
            Best.showMessage( m_context, m_context.getString(R.string.sorry_somthng_went_wrong), m_context.getString(R.string.chalobest));
    }

    public Handler tripInfoHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
            Best.dissmissProcessing();

            routeResult = ( String[][] )dataBundle.getSerializable( "rts" );
            if( showFullRoute == false && routeResult != null &&  routeResult.length > 0 )
            {
                List<String[]> listspecificRoute = new ArrayList<String[]>();
                outer:	for(int index = 0; index < routeResult.length; index++)
                {
                    if((routeResult[index][1]).equals(sourceId))
                    {
                        for(int i = index; i< routeResult.length; i++)
                        {
                            if((routeResult[i][1]).equals(destId))
                            {
                                listStopsGeoX.add( routeResult[i][3] );
                                listStopsGeoY.add( routeResult[i][4] );
                                listspecificRoute.add(routeResult[ i ]);

                                Best.log( " DT :: " + android.text.TextUtils.join( ",", routeResult[ i ] ) );
                                break outer;
                            }
                            else
                            {
                                listStopsGeoX.add( routeResult[i][3] );
                                listStopsGeoY.add( routeResult[i][4] );
                                listspecificRoute.add(routeResult[ i ]);
                            }
                        }
                    }
                }
                if(listspecificRoute.toArray( new String[ listspecificRoute.size() ][] ) == null)
                    Best.showMessage( m_context, m_context.getString(R.string.sorry_somthng_went_wrong),m_context.getString(R.string.chalobest)  );
                EfficientAdapter adapter = new EfficientAdapter( m_context,listspecificRoute.toArray( new String[ listspecificRoute.size() ][] ) );

                ( ( ListView ) me.findViewById( com.best.ui.R.id.routeslist ) ).setAdapter( adapter );
                //me.setListAdapter( adapter );
            }

        }
    };

    private class TripInfoThread extends Thread
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

    public class EfficientAdapter extends BaseAdapter implements Filterable {

        public String[][] result;
        public int resultSetLength;

        public EfficientAdapter(Context context, String[][] resultSet)
        {
            mInflater = LayoutInflater.from( context );
            result = new String[resultSet.length][];
            result=resultSet;
            resultSetLength = resultSet.length;
            stopGeoPoints = new String[resultSet.length];
            if(resultSet == null)
                Best.showError(context ,context.getString(R.string.No_route_between_Source_and_Destination));
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null)
            {
                convertView = mInflater.inflate(com.best.ui.R.layout.list_double_line, null);

                holder = new ViewHolder();
                holder.stop_name = (TextView) convertView.findViewById(com.best.ui.R.id.stopname);
                holder.stop_name.setTypeface(Best.m_marathi_typeface);
                holder.dep_time = (TextView) convertView.findViewById(com.best.ui.R.id.dep);
                holder.stop_area = (TextView) convertView.findViewById(com.best.ui.R.id.stoparea);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.stop_area.setText(result[position][5]);
            if(position==0 && showFullRoute == true)
            {
                //holder.stop_name.setText(Html.fromHtml("<font color = 'black'>"+"Bus starts at: "+"</font>"+"<font color = 'DarkSlateBlue'>"+result[position][0]+"</font>"));
                holder.stop_name.setText(result[position][0]);
                holder.stop_name.setTypeface(Best.m_marathi_typeface);
                holder.dep_time.setText(Html.fromHtml("<font color = 'Green'>"+"Starting point"+"</font>"));
                _startStopID = result[position][1];
            }
            else if(position==(resultSetLength - 1) && showFullRoute == true )
            {
                //holder.stop_name.setText(Html.fromHtml("<font color = 'black'>"+"Bus stops at: "+"</font>"+"<font color = 'DarkSlateBlue'>"+result[position][0]+"</font>"));
                holder.stop_name.setText(result[position][0]);
                holder.stop_name.setTypeface(Best.m_marathi_typeface);
                holder.dep_time.setText(Html.fromHtml("<font color = 'Blue'>"+"End Station"+"</font>"));
                _endStopID = result[position][1];
            }
            else
            {
                holder.stop_name.setText(result[position][0]);
                holder.stop_name.setTypeface(Best.m_marathi_typeface);
                holder.dep_time.setText("");
            }

            //String[] depParts = result[position][2].split('+');
            //holder.dep_time.setText(result[position][2]);
            stopGeoPoints[position] = result[position][3];
            if((result[position][1]).equals(sourceId) || (result[position][1]).equals(destId))
                convertView.setBackgroundColor(Color.parseColor("#d4b6d4"));		//0xffcccccc
            else if(position == 0 )
                convertView.setBackgroundColor(Color.parseColor("#e6ffe6"));
            else if(position == (resultSetLength - 1) )
                convertView.setBackgroundColor(Color.parseColor("#e1e1ff"));
            else
                convertView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
            //convertView.setBackgroundColor(0xffffffff);
            return convertView;
        }

        public class ViewHolder {


            TextView stop_name;
            TextView dep_time;
            TextView stop_area;
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
		
  