////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CLegToStopId
{
    public Object legToStopIdObject=null;
    public Object legToStopIdAgencyId=null;
    public Object legToStopIdId=null;
    public  JSONObject m_legToJsonObject=null;
    public  JSONObject legToStopIdJsonObject=null;
    public  String legToStopidAgencyId="";
    public  String legToStopidid="";

    CLegToStopId(JSONObject legToJsonObject)
    {
        m_legToJsonObject=legToJsonObject;
    }

    void getlegToStopId()
    {
        if(m_legToJsonObject!=null)
        {
            legToStopIdObject =m_legToJsonObject.get("stopId");
            if(legToStopIdObject!=null)
            {
                legToStopIdJsonObject= (JSONObject) legToStopIdObject;
                if(legToStopIdObject!=null)
                {
                    legToStopIdAgencyId =legToStopIdJsonObject.get("agencyId");
                    if(legToStopIdAgencyId!=null)
                    legToStopidAgencyId=legToStopIdAgencyId.toString();
                    legToStopIdId =legToStopIdJsonObject.get("id");
                    if(legToStopIdId!=null)
                    legToStopidid=legToStopIdId.toString();

                }

            }
        }

    }
}
