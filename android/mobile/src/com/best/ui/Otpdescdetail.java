////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.*;
import android.widget.*;
import com.best.otp.CPlan;
import com.best.otp.CTrip;
import com.best.util.TransitionEffect;
import org.json.simple.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: programmer
 * Date: 4/16/13
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Otpdescdetail extends Activity {

    public static boolean iterlistdisplayed=false;
    public static CTrip ctrip=null;
    public static boolean individualModePage=false;
    public static boolean contentChanged=false;
    public static boolean busRouteDisplayed=false;
    public  JSONObject ItineraryJsonObject=null;
    private  Object ItineraryObject=null;
    public static Activity me;
    public static Context m_context;
    public static LayoutInflater mInflater;
    public static String scrStopId , destStopId;
    public static String sourceStopName = "";
    public static String destStopName = "";
    public static String[] routes , routeNames ,legStartTime, legFrom_name,legEndTime ,legto_name ,tripId = new String[5];
    public static boolean routeDisplayed = false;
    public static int position=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT)
            TransitionEffect.callOverridePendingTransition(this);


        me = this;
        m_context = this;
        me.setContentView( com.best.ui.R.layout.routing );
        mInflater = LayoutInflater.from(m_context);

        Bundle bundle = getIntent().getExtras();

        String DESCRIP = bundle.getString("routeName"); //my description string.
        routeNames= DESCRIP.split(",");
        System.out.println("CPlan.plan.size() "+CPlan.plan.size());
        System.out.println("CPlan.plan.get(0).legs.size()"+CPlan.plan.get(0).legs.size());
        System.out.println("CPlan.plan.get(1).legs.size()"+CPlan.plan.get(1).legs.size());
        System.out.println("routeNames.length"+routeNames.length);
        position=bundle.getInt("position");                              //CPlan.plan.get(position).legs.get(0).v2.size()
        System.out.println("position "+position);                          //legs.get(CPlan.i).v2.size()
        System.out.println(" CPlan.plan.size() " + CPlan.plan.size());

        ItineraryObject = CPlan.ItineraryListJsonObject.get(position);
        ItineraryJsonObject = (JSONObject) ItineraryObject;//legs.get(CPlan.i).v2.size()

        ctrip = CPlan.plan.get(position);
        System.out.println("CTrip.legs.size()*  "+ctrip.legs.size());

        legStartTime=new String[routeNames.length];
        legFrom_name=new String[routeNames.length];
        legEndTime=new String[routeNames.length];
        legto_name=new String[routeNames.length]; //no of legs
        for(int j=0;j<routeNames.length;j++)
        {
            System.out.println("j "+j);

            legStartTime[j]= ctrip.legs.get(j).legStartTimeObject;//legStartTimeObject

            legFrom_name[j]= ctrip.legs.get(j).From_name.toString();     //From_name

            legEndTime[j]= ctrip.legs.get(j).legEndTime; //legEndTime

            legto_name[j]= ctrip.legs.get(j).To_name.toString();
        }
        System.out.println("busHead.length "+legStartTime.length);
        System.out.println("totalDist.length "+legFrom_name.length);
        System.out.println("busFreq.length "+legto_name.length);
        System.out.println("no_Stops.length "+legEndTime.length);

        init( this );
    }
    public void init(Context context)
    {

        System.out.println("in init ");
        m_context = context;
        routeDisplayed = false;
        tripId = Find.tripIdList.toArray( new String[ Find.tripIdList.size() ] ) ;

        scrStopId = Find._sourceStopId;
        destStopId = Find._destStopId;
        sourceStopName = Find.sourceStop;
        destStopName = Find.destStop;
        ((TextView) ((Activity)m_context).findViewById(com.best.ui.R.id.search_for)).setTypeface(Best.m_marathi_typeface);
        ((TextView) ((Activity)m_context).findViewById(com.best.ui.R.id.search_for)).setText(Html.fromHtml("<font color = 'Red'>Search Results for : </font><font color = 'Black'>" + sourceStopName + " to " + destStopName + "</font>"));

        EfficientAdapter adapter = new EfficientAdapter( m_context );

        try{
            if(adapter != null && context != null && routeDisplayed == false)
            {
                ((ListView)((Activity)m_context).findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );

            }
            else if(context != null)
                Best.log("ADAPTER IS NULL");
            else
                Best.log("CONTEXT IS NULL");
        }
        catch(Exception e){e.printStackTrace();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("in onCreateOptionsMenu of otpdescdetail.java");
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        System.out.println("in onPrepareOptionsMenu of stops.java");
        MenuInflater inflater = getMenuInflater();
        menu.clear();
        /*if((tripsDisplayed == false && tripsRouteDisplayed == false && searchResultDisplayed == false)||(tripsDisplayed==true && tripsRouteDisplayed == false))
        {
            inflater.inflate(com.best.ui.R.menu.search, menu);
            return true;
        } */
         if(iterlistdisplayed==false && busRouteDisplayed==true && individualModePage==false)
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
            /*case com.best.ui.R.id.mnusearch:
                System.out.println("in mnusearch of stops.java");
                if(tripsDisplayed == false)
                {
                    Intent intent = new Intent(m_context, Search.class);
                    intent.putExtra( "callFromStops" , true );
                    me.startActivityForResult( intent, 5 );
                    return true;
                }
                else
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {//If back was pressed
        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            System.out.println("back of Otpdescdetail clicked");
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void back()
    {                              //individual mode page
        if( iterlistdisplayed==false && busRouteDisplayed==true && individualModePage==false)
        {
            individualModePage=true;
            busRouteDisplayed=false;

            this.setContentView( com.best.ui.R.layout.routing );
            System.out.println("in if of contentChanged == true");
            m_context=this;
            EfficientAdapter adapter = new EfficientAdapter( m_context );
            try{
                if(adapter != null && m_context != null && routeDisplayed == false)
                {

                    ( (ListView)((Activity)m_context).findViewById( com.best.ui.R.id.routinglist ) ).setAdapter( adapter );
                }
                else if(m_context != null)
                    Best.log("ADAPTER IS NULL");
                else
                    Best.log("CONTEXT IS NULL");
            }
            catch(Exception e){e.printStackTrace();}
        }
        //iteanary list page
        else if(busRouteDisplayed==false && iterlistdisplayed==false && individualModePage==true )
        {
            iterlistdisplayed=true;
            individualModePage=false;
            System.out.println("in if of contentChanged == false");
            this.setContentView( com.best.ui.R.layout.routing );
            System.out.println("below this.setcontentview");
            System.out.println("m_context "+m_context);
            Routing.init(m_context);
            System.out.println("below Routing init");
             me.finish(); //mansi today
        }

        else if(iterlistdisplayed==true && busRouteDisplayed==false && individualModePage==false)
        {
            iterlistdisplayed=false;
            System.out.println("contentChanged "+contentChanged);
            System.out.println("iterlistdisplayed "+iterlistdisplayed);
            iterlistdisplayed=false;
            System.out.println("iterlistdisplayed "+iterlistdisplayed);
            System.out.println("below iterlistdisplayed");
            Intent intent = new Intent(m_context, MainTab.class);
            me.startActivity( intent );
        }
    }

    public class EfficientAdapter extends BaseAdapter implements Filterable {


        public EfficientAdapter(Context context)
        {
            if(routeNames!= null && routeNames.length > 0)
            {
                System.out.println("in constructor of EfficientAdapter");
                mInflater = LayoutInflater.from( context );
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            System.out.println("in getView of EfficientAdapter");
            ViewHolder holder;
            if(routeNames!= null)
            {
                try
                {
                    if (convertView == null)
                    {
                        System.out.println("in convertView == null of EfficientAdapter");
                        individualModePage=true;//list_button
                        iterlistdisplayed=false;
                        convertView = mInflater.inflate(com.best.ui.R.layout.otp_indetail_list_ofiten, null);
                        System.out.println("ibelow convertView = of EfficientAdapter");
                        holder = new ViewHolder();
                        holder.Legname= (TextView) convertView.findViewById(com.best.ui.R.id.Legname);
                        holder.Legstarttime = (TextView) convertView.findViewById(com.best.ui.R.id.Legstarttime);
                        holder.legfromname = (TextView) convertView.findViewById(com.best.ui.R.id.legfromname);
                        holder.legtoname = (TextView) convertView.findViewById(com.best.ui.R.id.legtoname);
                        holder.legendtime = (TextView) convertView.findViewById(com.best.ui.R.id.legendtime);
                        holder.commImage = (ImageView) convertView.findViewById(com.best.ui.R.id.commImage);


                        convertView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                final String mode=ctrip.legs.get(position).legMode.toString();
                                if(mode.contains("BUS"))
                                {
                                    System.out.println("in onClick of EfficientAdapter");
                                    System.out.println("position "+position);

                                    final String tripID=ctrip.legs.get(position).legTripId.toString();
                                    final String routeName=ctrip.legs.get(position).legRouteShortName.toString();
                                    final String routeLongName=ctrip.legs.get(position).legRouteLongName.toString();

                                    System.out.println("in onClick BUS of EfficientAdapter");
                                    busRouteDisplayed=true;
                                    individualModePage=false;
                                    me.setContentView( com.best.ui.R.layout.routes );

                                    NearTripRoute.init(m_context,  tripID, routeName, routeLongName,false);
                                }


                            }
                        });
                        System.out.println("above convertView.setTag(holder);");
                        convertView.setTag(holder);
                    }

                    else
                    {
                        System.out.println("in else of holder = (ViewHolder) convertView.getTag();");
                        holder = (ViewHolder) convertView.getTag();
                    }
                    System.out.println("position "+position);
                    String _distance =  legFrom_name[position] ;
                    String _routeName = routeNames[position];
                    System.out.println("busHead[position] "+legStartTime[position]);
                    String _headerName = legStartTime[position];
                    String _betweenStopsNum = legEndTime[position];
                    String freq =(legto_name[position]);
                    holder.legtoname.setText(freq);
                    holder.Legname.setText( _routeName );
                    holder.Legstarttime.setText( _headerName );
                    holder.legfromname.setText( _distance);
                    holder.legendtime.setText( _betweenStopsNum);
                    if(_routeName.contains("WALK"))
                    {
                        Drawable  drawable  = getResources().getDrawable(R.drawable.walk);
                        holder.commImage.setImageDrawable(drawable);
                    }

                    if(_routeName.contains("BUS"))
                    {
                        Drawable  drawable  = getResources().getDrawable(R.drawable.bus_otp);
                        holder.commImage.setImageDrawable(drawable);
                    }


                    if(_routeName.contains("RAIL"))
                    {
                        Drawable  drawable  = getResources().getDrawable(R.drawable.rail);
                        holder.commImage.setImageDrawable(drawable);
                    }

                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
                return convertView;
            }

            return (null);
        }

        public class ViewHolder {
            TextView Legname;
            TextView Legstarttime;
            ImageView commImage;
            TextView legfromname;
            TextView legtoname ;
            TextView legendtime;
        }

        @Override
        public Filter getFilter() {
            return null;
        }

        @Override
        public int getCount() {
            if(routeNames != null)
                return routeNames.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if(routeNames != null)
                return routeNames[position];
            else
                return (null);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

}

