////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CItenFrom
{
    public  String planFromName="";
    public  String planFromGeometry="";
    public  Object planFromObject=null;
    public  Object planFromStopIdObject=null;
    public  Object planFromStopCodeObject=null;
    public  Object planFromDepartureObject=null;
    public  Object planFromArrivalObject=null;
    public  Object planFromOrigObject=null;
    public  Object planFromZoneIdObject=null;
    public  JSONObject planFromJsonObject=null;
    public  Object planFromLon=null;
    public  Object planFromLat=null;

    CItenFrom()
    {

    }

    void getFrom()
    {
        planFromObject=CPlan.PlanJsonObject.get("from");
        if(planFromObject!=null)
        {
            planFromJsonObject = (JSONObject) planFromObject;
            planFromArrivalObject=planFromJsonObject.get("arrival");
            planFromDepartureObject=planFromJsonObject.get("departure");
            planFromOrigObject=planFromJsonObject.get("orig");
            planFromZoneIdObject=planFromJsonObject.get("zoneId");
            if(planFromJsonObject.get("name")!=null)
            planFromName=planFromJsonObject.get("name").toString();
            planFromStopIdObject =planFromJsonObject.get("stopId");
            planFromLon =planFromJsonObject.get("lon");
            planFromLat =planFromJsonObject.get("lat");
            planFromStopCodeObject =planFromJsonObject.get("stopCode");
            if(planFromJsonObject.get("geometry")!=null)
            planFromGeometry=planFromJsonObject.get("geometry").toString();
        }
    }
}

