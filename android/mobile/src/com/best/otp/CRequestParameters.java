////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CRequestParameters
{
    public String reqParaOptimize="";
    public String reqParaTime="";
    public String reqParawheelChair="";
    public String reqParaMaxWalkDistance="";
    public String reqParaRouterId="";
    public String reqParaFromPlace="";
    public String reqParatoPlace="";
    public String reqParaDate="";
    public String reqParMode="";
    public String reqParNumItineraries="";
    public Object requestParametersObject=null;
    public JSONObject requestParameterJsonObject=null;

    CRequestParameters()
    {

    }

    void getRequestParameters()
    {
        if(CPlan.jsonObject!=null)
        {
            requestParametersObject =  CPlan.jsonObject.get("requestParameters");
            if(requestParametersObject!=null)
            {
                requestParameterJsonObject = (JSONObject) requestParametersObject;
                if(requestParameterJsonObject!=null)
                {

                    if(requestParameterJsonObject.get("optimize")!=null)
                    reqParaOptimize= (requestParameterJsonObject.get("optimize")).toString();

                    if(requestParameterJsonObject.get("wheelchair")!=null)
                    reqParawheelChair=(requestParameterJsonObject.get("wheelchair")).toString();

                    if(requestParameterJsonObject.get("maxWalkDistance")!=null)
                    reqParaMaxWalkDistance=(requestParameterJsonObject.get("maxWalkDistance")).toString();

                    if(requestParameterJsonObject.get("routerId")!=null)
                    reqParaRouterId=(requestParameterJsonObject.get("routerId")).toString();

                    if(requestParameterJsonObject.get("fromPlace")!=null)
                    reqParaFromPlace=(requestParameterJsonObject.get("fromPlace")).toString();

                    if(requestParameterJsonObject.get("toPlace")!=null)
                    reqParatoPlace=(requestParameterJsonObject.get("toPlace")).toString();

                    if(requestParameterJsonObject.get("date")!=null)
                    reqParaDate=(requestParameterJsonObject.get("date")).toString();
                    System.out.println("reqParaDate "+reqParaDate);
                    if(requestParameterJsonObject.get("mode")!=null)
                    reqParMode =(requestParameterJsonObject.get("mode")).toString();

                    if(requestParameterJsonObject.get("numItineraries")!=null)
                    reqParNumItineraries=(requestParameterJsonObject.get("numItineraries")).toString();

                    if(requestParameterJsonObject.get("time")!=null)
                    reqParaTime=(requestParameterJsonObject.get("time")).toString();
                }
            }
        }
    }

}
