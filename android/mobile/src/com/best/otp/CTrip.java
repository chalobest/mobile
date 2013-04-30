////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.SimpleDateFormat;
import java.util.*;

public class CTrip
{
    public Date d=null;
    public  Date date=null;
    public  Date dateL=null;
    public SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
    public  long itneraryDuration=0;
    public  long itineraryWalkTime=0;
    public  long itineraryTransitTime=0;
    public  long itineraryWaitingTime=0;
    public  long itineraryTransfers =0;
    public  long planDate=0;
    public  long hours=0;
    public  long minute=0;
    public  long second=0;
    public  long itneraryStartTimelong=0;
    public  JSONObject  legJsonObject=null;
    public  Object itneraryStartTime=null;
    public  Object itneraryEndTime=null;
    public  Object itneraryFare=null ;
    public  Object legListObject=null;
    public JSONArray legListJsonObject;
    public  CItenFrom from=null;
    public  CItenTo to=null;
    public  CLeg leg=null ;
    public  Object legObject=null;
    public  double itneraryWalkDistance=0;
    public  double itneraryElevationLost=0;
    public  double itneraryElevationGained=0;
    public  boolean itneraryTooSloped=false;
    private final int SECOND = 1000;
    private  final int MINUTE = 60 * SECOND;
    private  final int HOUR = 60 * MINUTE;
    private  final int DAY = 24 * HOUR;
    public  double ms=0;
    public  String time="";
    public  String itneraryStartTimelongS="";
    public  String itneraryDurationlongS="";
    public  String am_pmn="";
    public  String timeString="";
    Calendar calendar = GregorianCalendar.getInstance();
    public  String modeRoute="";
    public  String S="";
    public  String[][] desc;
    public  String[][] DESC;
    public  String mode="";
    public  String route="";
    public  String tripStartTime="";
    public  String tripEndTime="";
    public  String tripWalkDistance="";
    public  String tripNoOfLegs="";
    public  String description="";
    public    List<CLeg> legs = null;
    public  JSONObject  m_ItineraryJsonObject=null;
    CPlan cplan= new CPlan();

    public CTrip(JSONObject ItineraryJsonObject)
    {
        m_ItineraryJsonObject=ItineraryJsonObject;
    }

    String getTripInfo( )
    {
        try
        {
            if(m_ItineraryJsonObject!=null)
            {
                legs= new ArrayList<CLeg>();
                if(m_ItineraryJsonObject.get("duration")!=null)
                {
                    itneraryDuration= Long.valueOf(m_ItineraryJsonObject.get("duration").toString());
                    System.out.println(m_ItineraryJsonObject.get("duration").toString()+"duration");
                }

                if(m_ItineraryJsonObject.get("walkTime")!=null)
                itineraryWalkTime= Long.valueOf(m_ItineraryJsonObject.get("walkTime").toString());

                if(m_ItineraryJsonObject.get("transitTime")!=null)
                itineraryTransitTime= Long.valueOf(m_ItineraryJsonObject.get("transitTime").toString());

                if(m_ItineraryJsonObject.get("waitingTime")!=null)
                itineraryWaitingTime= Long.valueOf(m_ItineraryJsonObject.get("waitingTime").toString());

                if(m_ItineraryJsonObject.get("transfers")!=null)
                itineraryTransfers= Long.valueOf(m_ItineraryJsonObject.get("transfers").toString());

                itneraryStartTime=m_ItineraryJsonObject.get("startTime") ;
                if(itneraryStartTime!=null)
                {
                    itneraryStartTimelong= Long.valueOf(m_ItineraryJsonObject.get("startTime").toString()) ;
                    System.out.println(m_ItineraryJsonObject.get("startTime").toString()+"startTime");
                    ms=itneraryStartTimelong;
                }
                itneraryEndTime=m_ItineraryJsonObject.get("endTime") ;
                itneraryFare=m_ItineraryJsonObject.get("fare") ;

                if(m_ItineraryJsonObject.get("walkDistance")!=null)
                itneraryWalkDistance =(Double)m_ItineraryJsonObject.get("walkDistance");

                if(m_ItineraryJsonObject.get("elevationLost")!=null)
                itneraryElevationLost=(Double)m_ItineraryJsonObject.get("elevationLost");
                if(m_ItineraryJsonObject.get("elevationGained")!=null)
                itneraryElevationGained=(Double)m_ItineraryJsonObject.get("elevationGained");

                if(m_ItineraryJsonObject.get("tooSloped")!=null)
                itneraryTooSloped = ((Boolean) m_ItineraryJsonObject.get("tooSloped"));

                if(CPlan.PlanJsonObject.get("date")!=null)
                planDate= Long.valueOf(CPlan.PlanJsonObject.get("date").toString());

                d = new Date(itneraryStartTimelong);
                System.out.println(d+"date");
                date = new Date();
                calendar.setTime(date);
                sd.setTimeZone(TimeZone.getTimeZone("IST"));
                System.out.println(sd.format(d)+" sd.format(d)");

                from=new CItenFrom();
                from.getFrom();
                to=new CItenTo();
                to.getTo();

                legListObject=m_ItineraryJsonObject.get("legs");
                legListJsonObject = (JSONArray) legListObject;
                System.out.println("legListJsonObject.size() "+legListJsonObject.size());
                for(int i=0;i<legListJsonObject.size();i++)
                {
                    legObject=legListJsonObject.get(i);
                    legJsonObject = (JSONObject) legObject;
                    leg=new CLeg(legJsonObject);
                    leg.getLeg();
                    legs.add(leg);
                    System.out.println("legs.size() "+legs.size());
                }
                System.out.println(".........................................................................................");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return(S);




    }

}
