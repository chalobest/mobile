////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;

import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CItinerary
{
    public  Date d=null;
    public  Date date=null;
    public  Date dateL=null;
    public  SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
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
    private  JSONObject m_ItineraryJsonObject=null;
    public  Object itneraryStartTime=null;
    public  Object itneraryEndTime=null;
    public  Object itneraryFare=null ;
    public  CItenFrom from=null;
    public  CItenTo to=null;
    public  CLeg leg=null ;
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

    CItinerary(JSONObject ItineraryJsonObject )
    {
        m_ItineraryJsonObject= ItineraryJsonObject;

    }

    void  getItinerary()
    {
        try
        {
            itneraryDuration= Long.valueOf(m_ItineraryJsonObject.get("duration").toString());
            System.out.println(m_ItineraryJsonObject.get("duration").toString()+"duration");

            //itneraryDurationlongS=caltime(itneraryStartTimelong);
            //System.out.println("itneraryDurationlongS"+itneraryDurationlongS);
            itineraryWalkTime= Long.valueOf(m_ItineraryJsonObject.get("walkTime").toString());
            itineraryTransitTime= Long.valueOf(m_ItineraryJsonObject.get("transitTime").toString());
            itineraryWaitingTime= Long.valueOf(m_ItineraryJsonObject.get("waitingTime").toString());
            itineraryTransfers= Long.valueOf(m_ItineraryJsonObject.get("transfers").toString());
            itneraryStartTime=m_ItineraryJsonObject.get("startTime") ;
            itneraryStartTimelong= Long.valueOf(m_ItineraryJsonObject.get("startTime").toString()) ;
            System.out.println(m_ItineraryJsonObject.get("startTime").toString()+"startTime");
            ms=itneraryStartTimelong;

            itneraryEndTime=m_ItineraryJsonObject.get("endTime") ;
            itneraryFare=m_ItineraryJsonObject.get("fare") ;
            itneraryWalkDistance =(Double)m_ItineraryJsonObject.get("walkDistance");
            itneraryElevationLost=(Double)m_ItineraryJsonObject.get("elevationLost");
            itneraryElevationGained=(Double)m_ItineraryJsonObject.get("elevationGained");
            itneraryTooSloped = ((Boolean) m_ItineraryJsonObject.get("tooSloped"));
            planDate= Long.valueOf(CPlan.PlanJsonObject.get("date").toString());
            d = new Date(itneraryStartTimelong);
            System.out.println(d+"date");
            date = new Date();
            calendar.setTime(date);


            sd.setTimeZone(TimeZone.getTimeZone("IST"));
            System.out.println(sd.format(d)+" sd.format(d)");
            //....comented bcoz not going further  dateL = sd.parse(String.valueOf(itneraryStartTimelong));


            /*int hour = calendar.get(Calendar.HOUR);
            minute = calendar.get(Calendar.MINUTE);
            int  am_pm=  calendar.get(Calendar.AM_PM);

            if(am_pm==0)
            am_pmn="am";
            else
            am_pmn="pm";
            timeString="hour+";
            System.out.print("*"+hour+":"+minute+ am_pmn+"**"); */
            /*System.out.println(String.valueOf(itneraryDuration)+" itneraryDuration");
            System.out.println(String.valueOf(itineraryWalkTime)+" itineraryWalkTime");
            System.out.println(String.valueOf(itineraryTransitTime)+" itineraryTransitTime");
            System.out.println(String.valueOf(itineraryWaitingTime)+" itineraryWaitingTime");
            System.out.println(String.valueOf(itineraryTransfers)+" itineraryTransfers");
            System.out.println(itneraryStartTime.toString()+" itneraryStartTime");
            System.out.println(itneraryEndTime.toString()+" itneraryEndTime");
            System.out.println(itneraryFare+" itneraryFare");
            System.out.println(Double.toString(itneraryWalkDistance)+" itneraryWalkDistance");
            System.out.println(Double.toString(itneraryElevationLost)+" itneraryElevationLost");
            System.out.println(Double.toString(itneraryElevationGained)+" itneraryElevationGained");
            System.out.println(String.valueOf(itneraryTooSloped)+" itneraryTooSloped");
            System.out.println(String.valueOf(planDate) +"date");*/
            from=new CItenFrom();
            from.getFrom();
            to=new CItenTo();
            to.getTo();
            leg=new CLeg(m_ItineraryJsonObject);
            leg.getLeg();
            CTrip ctrip =new CTrip(m_ItineraryJsonObject);
            CTrip.legs.add(leg);
            System.out.println(".........................................................................................");

            /* public static String caltime(long dur)
            {

            StringBuffer text = new StringBuffer("");
            if (ms > DAY) {
                text.append(ms / DAY).append(" days ");
                ms %= DAY;
            }
            if (ms > HOUR) {
                text.append(ms / HOUR).append(" hours ");
                ms %= HOUR;
            }
            if (ms > MINUTE) {
                text.append(ms / MINUTE).append(" minutes ");
                ms %= MINUTE;
            }
            if (ms > SECOND) {
                text.append(ms / SECOND).append(" seconds ");
                ms %= SECOND;
            }
            text.append(ms + " ms");
            time=text.toString();
            System.out.println(text.toString()+"itneraryStartTimelong*");
            // return(time);
            // }  */

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
   }
   }



