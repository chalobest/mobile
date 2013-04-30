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
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View.OnClickListener;
import com.best.data.GTStore_sqlite;
import com.best.util.TransitionEffect;
import android.view.KeyEvent;

public class Stops extends Activity {

    public static Activity me;
    public static Context m_context;
    public static LayoutInflater mInflater;
    public static boolean tripsDisplayed = false;
    public static boolean tripsRouteDisplayed = false;
    public static boolean searchResultDisplayed = false;
    public static String _selectedStopID = null;
    public static String _selectedStopName = "";
    public static String searchtext = "";
    public static String[] colors = {"#fcfcfc" , "#ececec"};
    public static String[][] _stopsResult, stopsResult;
    public static String[][] tripsResult ;

    public static boolean showFullRoute = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT)
            TransitionEffect.callOverridePendingTransition(this);

        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        me = this;
        m_context = this;
        me.setContentView( com.best.ui.R.layout.stops );
        mInflater = LayoutInflater.from(m_context);

        init( );
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
        System.out.println("in onCreateOptionsMenu of stops.java");
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        System.out.println("in onPrepareOptionsMenu of stops.java");
        MenuInflater inflater = getMenuInflater();
        menu.clear();                                                             //||(tripsDisplayed==true && tripsRouteDisplayed == false)
        if((tripsDisplayed == false && tripsRouteDisplayed == false && searchResultDisplayed == false))
        {
            inflater.inflate(com.best.ui.R.menu.search, menu);
            return true;
        }
        else if(tripsDisplayed == true && tripsRouteDisplayed == true)
        {
            inflater.inflate(com.best.ui.R.menu.onlymap, menu);
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("in onOptionsItemSelected of stops.java");
        // Handle item selection
        switch (item.getItemId()) {
            case com.best.ui.R.id.mnusearch:
                System.out.println("in mnusearch of stops.java");
                if(tripsDisplayed == false)
                {
                    Intent intent = new Intent(m_context, Search.class);
                    intent.putExtra( "callFromStops" , true );
                    me.startActivityForResult( intent, 5 );
                    return true;
                }
               /* else
                {
                    Intent intent = new Intent(m_context, Search.class);
                    intent.putExtra( "callFromStops" , false );
                    me.startActivityForResult( intent, 5 );
                    return true;
                } */

            case com.best.ui.R.id.map:
                if(NearTripRoute._stopLat != null && NearTripRoute._stopLon != null)
                {   System.out.println("in map of stops.java");
                    Bundle bundle1 = new Bundle();
                    bundle1.putBoolean( "showSingle", false );
                    bundle1.putSerializable("StopsGeoX",NearTripRoute._stopLat);
                    bundle1.putSerializable("StopsGeoY",NearTripRoute._stopLon);
                    Intent intent1 = new Intent(m_context, Map.class);
                    intent1.putExtras( bundle1 );
                    me.startActivityForResult(intent1,5);
                }
                return true;

            default: System.out.println("in default of stops.java");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try{
            Bundle bundle = data.getExtras();
            String[] stopID = bundle.getStringArray( "StopId" );
            String[] stopName = bundle.getStringArray( "StopName" );
            String[] stopRoad = bundle.getStringArray( "StopRoad" );
            String[] stopArea = bundle.getStringArray( "StopArea" );
            searchtext = bundle.getString( "searchtxt");
            stopsResult = new String[stopID.length][4];
            for(int i = 0; i < stopID.length ; i++)
            {
                stopsResult[i][0] = stopID[i];
                stopsResult[i][1] = stopName[i];
                stopsResult[i][2] = stopRoad[i];
                stopsResult[i][3] = stopArea[i];
            }
            me.setContentView( com.best.ui.R.layout.routing );
            EfficientAdapter adapter = new EfficientAdapter( m_context , stopsResult);
            ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
            ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Search Result for : " + searchtext);
            System.out.println("searchResultDisplayed =true of stops.java");
            searchResultDisplayed = true ;

        }
        catch(Exception e)
        {
            Best.log("Exception-"+e.toString());
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {//If back was pressed
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            System.out.println("back of stops clicked");
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void back()
    {
        System.out.println("STOPS=========ON BACK PRESS====");

        if(tripsDisplayed == true && tripsRouteDisplayed == false && searchResultDisplayed == false)
        {
            me.setContentView(com.best.ui.R.layout.stops  );
            tripsDisplayed = false;
            init();
        }
        else if(tripsRouteDisplayed == true && tripsDisplayed == true && searchResultDisplayed == false)
        {
            me.setContentView( com.best.ui.R.layout.routing );
            tripsRouteDisplayed = false;
            EfficientAdapterTrip adapter = new EfficientAdapterTrip( m_context);
            ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );

            ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setTypeface(Best.m_marathi_typeface);
            ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Buses Available from : "+_selectedStopName);
        }
        else if(searchResultDisplayed == true && tripsDisplayed == true && tripsRouteDisplayed == true)
        {
            me.setContentView( com.best.ui.R.layout.routing );
            tripsRouteDisplayed = false;
            EfficientAdapterTrip adapter = new EfficientAdapterTrip( m_context);
            ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );

            ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setTypeface(Best.m_marathi_typeface);
            ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Buses Available from : "+_selectedStopName);
        }
        else if(searchResultDisplayed == true && tripsDisplayed == true && tripsRouteDisplayed == false)
        {
            me.setContentView( com.best.ui.R.layout.routing );
            tripsDisplayed = false;
            EfficientAdapter adapter = new EfficientAdapter( m_context , stopsResult);
            ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
            ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Search Result for : " + searchtext);
        }
        else if(searchResultDisplayed == true && tripsDisplayed == false && tripsRouteDisplayed == false)
        {
            me.setContentView(com.best.ui.R.layout.stops  );
            searchResultDisplayed = false;
            init();
        }
        else
            Best.exit( me );

    }
    // @Override
    // public void onBackPressed() {

    // System.out.println("STOPS=========ON BACK PRESS====");

    // if(tripsDisplayed == true && tripsRouteDisplayed == false && searchResultDisplayed == false)
    // {
    // me.setContentView(com.best.ui.R.layout.stops  );
    // tripsDisplayed = false;
    // init();
    // }
    // else if(tripsRouteDisplayed == true && tripsDisplayed == true && searchResultDisplayed == false)
    // {
    // me.setContentView( com.best.ui.R.layout.routing );
    // tripsRouteDisplayed = false;
    // EfficientAdapterTrip adapter = new EfficientAdapterTrip( m_context);
    // ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
    // ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Buses Available from : "+_selectedStopName);
    // }
    // else if(searchResultDisplayed == true && tripsDisplayed == true && tripsRouteDisplayed == true)
    // {
    // me.setContentView( com.best.ui.R.layout.routing );
    // tripsRouteDisplayed = false;
    // EfficientAdapterTrip adapter = new EfficientAdapterTrip( m_context);
    // ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
    // ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Buses Available from : "+_selectedStopName);
    // }
    // else if(searchResultDisplayed == true && tripsDisplayed == true && tripsRouteDisplayed == false)
    // {
    // me.setContentView( com.best.ui.R.layout.routing );
    // tripsDisplayed = false;
    // EfficientAdapter adapter = new EfficientAdapter( m_context , stopsResult);
    // ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
    // ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Search Result for : " + searchtext);
    // }
    // else if(searchResultDisplayed == true && tripsDisplayed == false && tripsRouteDisplayed == false)
    // {
    // me.setContentView(com.best.ui.R.layout.stops  );
    // searchResultDisplayed = false;
    // init();
    // }
    // else
    // Best.exit( me );

    // }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }

    public void init()
    {
        //( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setVisibility(LinearLayout.GONE);
        Best.showProcessing(m_context,m_context.getString(R.string.processing),m_context.getString(R.string.please_wait_while_seacrching));
        try{
            GetStopsThread getStopsThread = new GetStopsThread( getStopsHandler );
            getStopsThread.start();
        }
        catch(Exception e){
            Best.dissmissProcessing();
            Best.showMessage( m_context,m_context.getString(R.string.sorry_somthng_went_wrong) , m_context.getString(R.string.chalobest));
        }
    }

    public Handler getStopsHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle dataBundle = msg.getData();
            Best.dissmissProcessing();

            _stopsResult = ( String[][] )dataBundle.getSerializable( "rts" );
            if( _stopsResult != null &&  _stopsResult.length > 0 )
            {
                EfficientAdapter adapter = new EfficientAdapter( m_context,_stopsResult );
                ( ( ListView ) me.findViewById( com.best.ui.R.id.stopslist ) ).setAdapter( adapter );

            }

        }
    };

    private class GetStopsThread extends Thread
    {
        Handler _handler;

        GetStopsThread(Handler handler )
        {
            _handler = handler;
        }

        public void run()
        {
            //code to get all routes
            Bundle dataBundle = new Bundle();
            dataBundle.putSerializable( "rts", GTStore_sqlite.getAllStops());
            Message msg = _handler.obtainMessage();
            msg.setData( dataBundle );
            _handler.sendMessage( msg );
        }
    }
    public void getAllTripsPassingByStop(String StopID)
    {
        _selectedStopID = StopID;
        Best.showProcessing(m_context, m_context.getString(R.string.processing),m_context.getString(R.string.please_wait_while_seacrching) );
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



                ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setTypeface(Best.m_marathi_typeface);
                ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Buses Available from : "+_selectedStopName);

                EfficientAdapterTrip adapter = new EfficientAdapterTrip( m_context);
                ( ( ListView ) me.findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
                //me.setListAdapter( adapter );

            }
            else
            {
                //Best.showMessage( m_context, "There are no routes to show.", "Processing" );
                ( ( TextView ) me.findViewById( com.best.ui.R.id.search_for ) ).setText("Sorry...Stop data not available..");
            }

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
                if(_selectedStopID != null)
                {
                    String [][] _res = GTStore_sqlite.getAllTripsPassingByStop(_selectedStopID);

                    dataBundle.putSerializable( "rts", _res );
                    Message msg = _handler.obtainMessage();
                    msg.setData( dataBundle );
                    _handler.sendMessage( msg );
                }
            }
            catch(Exception e){Best.dissmissProcessing(); e.printStackTrace();
            }
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

            if (convertView == null)
            {
                convertView = mInflater.inflate(com.best.ui.R.layout.list_double_line, null);

                holder = new ViewHolder();
                holder.stop_name = (TextView) convertView.findViewById(com.best.ui.R.id.stopname);

                holder.stop_road = (TextView) convertView.findViewById(com.best.ui.R.id.stoproad);
                holder.stop_area = (TextView) convertView.findViewById(com.best.ui.R.id.stoparea);
                holder.dep_time = (TextView) convertView.findViewById(com.best.ui.R.id.dep);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.stop_name.setTypeface(Best.m_marathi_typeface);
            holder.stop_name.setText(result[position][1]);

            holder.stop_area.setText(result[position][3]);
            holder.stop_road.setText(result[position][2]);
            //convertView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    System.out.println("trips displayed= true of stops ");
                    tripsDisplayed = true;
                    me.setContentView( com.best.ui.R.layout.routing );
                    getAllTripsPassingByStop(result[position][0]);
                    _selectedStopName = result[position][1];

                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView stop_name;
            TextView stop_area;
            TextView stop_road;
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
    public class EfficientAdapterTrip extends BaseAdapter implements Filterable {

        public EfficientAdapterTrip(Context context)
        {
            if(tripsResult!= null)
            {
                mInflater = LayoutInflater.from( context );
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(tripsResult!= null)
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

                holder.head.setText( tripsResult[position][3] );
                holder.routeName.setText( tripsResult[position][4] );


                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString( "tripID" , tripsResult[position][2] );
                        bundle.putString("routeLongName",tripsResult[position][3]);
                        bundle.putString("routeName",tripsResult[position][4]);
                        Best.log("CLICKED POSITION :"+ position);
                        System.out.println("tripsRouteDisplayed=true of stops.java");
                        tripsRouteDisplayed = true;
                        me.setContentView( com.best.ui.R.layout.routes );
                        //NearTripRoute object = new NearTripRoute();
                        NearTripRoute.init(m_context,tripsResult[position][2],tripsResult[position][4],tripsResult[position][3],false);

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
            if(tripsResult != null)
                return tripsResult.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if(tripsResult != null)
                return tripsResult[position];
            else
                return (null);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

}

