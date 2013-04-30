////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CLegFrom
{
    public  String legFromName="";
    public  String legFromGeometry="";
    public  Object legFromObject=null;
    public  Object legFromStopCode=null;
    public  Object legFromArrival=null;
    public  Object legFromDeparture=null;
    public  Object legFromOrig=null;
    public  Object legFromZoneId=null;
    public  JSONObject legFromJsonObject=null;
    public  JSONObject m_legJsonObject=null;
    public  CLegFromStopid legFromStopid=null;
    public  Object legFromLon=null;
    public  Object legFromLat=null;

    CLegFrom(JSONObject  legJsonObject)
    {
        m_legJsonObject=legJsonObject;
    }

    String getLegFrom()
    {
        if(m_legJsonObject!=null)
        {
            legFromObject=m_legJsonObject.get("from");
            if(legFromObject!=null)
            {
                legFromJsonObject= (JSONObject) legFromObject;
                if(legFromJsonObject.get("name")!=null)
                legFromName=legFromJsonObject.get("name").toString();

                if(legFromJsonObject.get("geometry")!=null)
                legFromGeometry=legFromJsonObject.get("geometry").toString();

                legFromStopCode=legFromJsonObject.get("stopCode");
                legFromArrival=legFromJsonObject.get("arrival");
                legFromDeparture=legFromJsonObject.get("departure");
                legFromOrig=legFromJsonObject.get("orig");
                legFromZoneId=legFromJsonObject.get("zoneId");
                legFromLon=legFromJsonObject.get("lon");
                legFromLat=legFromJsonObject.get("lat");

                legFromStopid=new  CLegFromStopid(legFromJsonObject);
                legFromStopid.getLegFromStopid();
            }
        }
        return(legFromName);
    }
}
