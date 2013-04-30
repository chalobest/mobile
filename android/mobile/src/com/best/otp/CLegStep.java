////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CLegStep
{
    public JSONObject m_legJsonObject=null;
    public JSONObject legStepJsonObject=null;
    public JSONArray legStepListJsonObject;
    public Object legStepDistance=null;
    public Object legSteplon=null;
    public Object legStepLat=null;
    public Object legStepExit=null;
    public Object legStepStayOnObject=null;
    public Object legStepBogusNameObject=null;
    public Object legStepListObject=null;
    public Object legStepObject=null;
    public String legStepRelativeDirection="";
    public String legStepStreetName="";
    public String legStepAbsoluteDirection="";
    public String legStepElevation="";
    public Object legStepStayOn=null;
    public Object legStepBogusName=null;

    CLegStep(JSONObject legJsonObject)
    {
        m_legJsonObject=legJsonObject;
    }

    void getLegStep()
    {
        if(m_legJsonObject!=null)
        {
            legStepListObject=m_legJsonObject.get("steps");
            if(legStepListObject!=null)
            {
                legStepListJsonObject = (JSONArray) legStepListObject;
                for(int k=0;k<legStepListJsonObject.size();k++)
                {
                    legStepObject=legStepListJsonObject.get(k);
                    if(legStepObject!=null)
                    {
                        legStepJsonObject = (JSONObject) legStepObject;
                        legStepDistance=legStepJsonObject.get("distance") ;
                        legSteplon= legStepJsonObject.get("lon") ;
                        legStepLat= legStepJsonObject.get("lat") ;
                        if(legStepJsonObject.get("relativeDirection")!=null)
                        legStepRelativeDirection= legStepJsonObject.get("relativeDirection").toString() ;

                        if(legStepJsonObject.get("streetName")!=null)
                        legStepStreetName= legStepJsonObject.get("streetName").toString() ;

                        if(legStepJsonObject.get("absoluteDirection")!=null)
                        legStepAbsoluteDirection= legStepJsonObject.get("absoluteDirection").toString() ;

                        if(legStepJsonObject.get("elevation")!=null)
                        legStepElevation= legStepJsonObject.get("elevation").toString() ;

                        legStepExit= legStepJsonObject.get("exit") ;
                        legStepStayOnObject=legStepJsonObject.get("stayOn");
                        legStepBogusNameObject=legStepJsonObject.get("bogusName");
                        //System.out.println(String.valueOf(legStepDistance)+" legStepDistance");
                    }
                }
            }
        }
    }
}
