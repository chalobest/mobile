////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CItenTo
{
    public  String planToName="";
    public  String planToGeometry="";
    public  Object planToObject=null;
    public  Object planToStopIdObject=null;
    public  Object planToStopCodeObject=null;
    public  Object planToArrivalObject=null;
    public  Object planToDepartureObject=null;
    public  Object planToOrigObject=null;
    public  Object planToZoneIdObject=null;
    public  JSONObject planToJsonObject=null;
    public  Object planToLat=null;
    public  Object planToLon=null;
    public  CPlan cplan=null;

    CItenTo()
    {

    }
    void getTo()
    {
        cplan=new CPlan();
        planToObject=cplan.PlanJsonObject.get("to");
        if(planToObject!=null)
        {
            planToJsonObject = (JSONObject) planToObject;
            planToStopIdObject=planToJsonObject.get("stopId");
            planToStopCodeObject=planToJsonObject.get("stopCode");
            planToArrivalObject=planToJsonObject.get("arrival");
            planToDepartureObject=planToJsonObject.get("departure");
            planToOrigObject=planToJsonObject.get("orig");
            planToZoneIdObject=planToJsonObject.get("zoneId");
            if(planToJsonObject.get("name")!=null)
            planToName= planToJsonObject.get("name").toString();
            if(planToJsonObject.get("geometry")!=null)
            planToGeometry=planToJsonObject.get("geometry").toString();
            planToLat=planToJsonObject.get("lon");
            planToLon=planToJsonObject.get("lat");
        }
    }
}
