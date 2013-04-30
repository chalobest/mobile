////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CLegFromStopid
{
    public JSONObject m_legFromJsonObject=null;
    public JSONObject legFromStopidJsonObject=null;
    public String legFromStopidAgencyId="";
    public String legFromStopidId="";
    public Object legFromStopidObject=null;

    CLegFromStopid(JSONObject legFromJsonObject)
    {
        m_legFromJsonObject = legFromJsonObject;
    }

    void getLegFromStopid()
    {
        if(m_legFromJsonObject!=null)
        {
            legFromStopidObject= m_legFromJsonObject.get("stopId");
            if(legFromStopidObject!=null)
            {
                legFromStopidJsonObject= (JSONObject) legFromStopidObject;
                if(legFromStopidJsonObject.get("agencyId")!=null)
                legFromStopidAgencyId=legFromStopidJsonObject.get("agencyId").toString();
                if(legFromStopidJsonObject.get("id")!=null)
                legFromStopidId=legFromStopidJsonObject.get("id").toString();
            }

        }

    }
}
