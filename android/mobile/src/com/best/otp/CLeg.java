////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CLeg
{
    public String legEndTime="";
    public String legStartTimeObject="";
    public FormatMilsec formatMilsec=null;
    public  String modeRoute="";
    public  JSONObject  m_legJsonObject=null;
    public  JSONArray legListJsonObject;
    public  Object legIntermediateStopsObject=null;
    public  Object legListObject=null;
    public  Object legInterlineWithPreviousLeg=null;
    public  Object legObject=null;
    public  static List<Object> intermediateStops;
    public  Object legStartTimeOb=null;
    public  Object legEndTimeOb=null;
    public  Object legAgencyName=null;
    public  Object legAgencyUrl=null;
    public  Object legRouteColor=null;
    public  Object legRouteTextColor=null;
    public  Object legTripShortName=null;
    public  Object legHeadsign=null;
    public  Object legRouteShortName=null;
    public  Object legBoardRule=null;
    public  Object legAlightRule=null;
    public  Object legDistance=null;
    public  Object legMode="";
    public  Object legRoute="";
    public  Object legAgencyId="";
    public  Object legTripId="";
    public  Object legRouteLongName="";
    public  String From_name="";
    public  String To_name="";
    public  Object legDuration=null;
    public  Object legBogusNonTransitLeg=null;
    public  CLegFrom legFrom=null;
    public  CLegTo legTo=null ;
    public  CLeglegGeometry leglegGeometry=null;
    public  CLegStep legStep=null;
    public  static List<CLegStep> list = new ArrayList<CLegStep>();
    public  static Vector v2=null;

    CLeg(JSONObject  legJsonObject)
    {
        m_legJsonObject=legJsonObject;
    }

    void getLeg()
    {
        try
        {
            if(m_legJsonObject!=null)
            {
            v2 = new Vector();
            legStartTimeOb=m_legJsonObject.get("startTime");//n //legStartTimeObject
                if(legStartTimeOb!=null)
                {
                    System.out.println("legStartTimeOb in CLEG "+legStartTimeOb.toString());
                    formatMilsec=new FormatMilsec(legStartTimeOb.toString());
                    legStartTimeObject=formatMilsec.getDate();
                    System.out.println("legStartTimeObject "+legStartTimeObject);
                }
            legEndTimeOb=m_legJsonObject.get("endTime");//n
                if(legEndTimeOb!=null)
                {
                    formatMilsec=new FormatMilsec(legEndTimeOb.toString()); //legEndTime
                    legEndTime=formatMilsec.getDate();
                    System.out.println("legEndTime "+legEndTime);
                }
            legAgencyName=m_legJsonObject.get("agencyName");
            legAgencyUrl=m_legJsonObject.get("agencyUrl");
            legRouteColor=m_legJsonObject.get("routeColor");
            legRouteTextColor=m_legJsonObject.get("routeTextColor");
            legInterlineWithPreviousLeg=m_legJsonObject.get("interlineWithPreviousLeg");
            legTripShortName=m_legJsonObject.get("tripShortName");
            legHeadsign=m_legJsonObject.get("headsign");
            legRouteShortName=m_legJsonObject.get("routeShortName");
            legBoardRule=m_legJsonObject.get("boardRule");
            legAlightRule=m_legJsonObject.get("alightRule");
            legDistance =m_legJsonObject.get("distance");
            if(m_legJsonObject.get("mode")!=null)
            legMode=m_legJsonObject.get("mode").toString();

            if(m_legJsonObject.get("route")!=null)
            legRoute=m_legJsonObject.get("route").toString();
            legAgencyId=m_legJsonObject.get("agencyId");
            //if(m_legJsonObject.get("tripId")!=null)
            legTripId=m_legJsonObject.get("tripId");
            //else
            //legTripId="";
            if(m_legJsonObject.get("routeLongName")!=null)
            legRouteLongName=m_legJsonObject.get("routeLongName").toString();
            else
            legRouteLongName="";
            legDuration= ((m_legJsonObject.get("duration")));
            legBogusNonTransitLeg=m_legJsonObject.get("bogusNonTransitLeg");
            legIntermediateStopsObject=m_legJsonObject.get("intermediateStops");

            legFrom=new CLegFrom(m_legJsonObject);
            From_name=legFrom.getLegFrom();

            legTo = new CLegTo(m_legJsonObject);
            To_name =legTo.getLegTo();

            leglegGeometry =new CLeglegGeometry(m_legJsonObject);
            leglegGeometry.getLegGeometry();

            legStep=new CLegStep(m_legJsonObject);
            legStep.getLegStep();

            list.add(legStep);
            modeRoute=legMode+" "+legRoute;

           // v2.add(modeRoute) ;//0
           // v2.add(From_name);    //1
           // v2.add(To_name);       //2
           // v2.add(legStartTimeObject);//3
            //v2.add(legEndTime);        //4
        }
        }

        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}

