////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CLegTo
{
    public  JSONObject LegToGeometryJsonObject=null;
    public  JSONObject m_legJsonObject=null;
    public  double legToLon=0;
    public  double legToLat=0;
    public  Object legToStopCode=null;
    public  Object legToArrival=null;
    public  Object legToDeparture=null;
    public  Object legToOrig=null;
    public  Object legToZoneId=null;
    public  Object LegToGeometryTypeObject=null;
    public  Object LegToGeometrycoordinatesObject=null;
    public  JSONObject legToJsonObject;
    public  String LegToName="";
    public  String LegToGeometry="";
    public  String LegToGeometryType;
    public  String LegToGeometryPoint="";
    public  String LegToGeometrycoordinates="";
    public  CLegToStopId legToStopId=null;

    CLegTo(JSONObject legJsonObject)
    {
        m_legJsonObject= legJsonObject;
    }

    String getLegTo()
    {
        try
        {
            if(m_legJsonObject!=null)
            {
                Object legToObject=m_legJsonObject.get("to");
                if(legToObject!=null)
                {
                    legToJsonObject= (JSONObject) legToObject;
                    if(legToJsonObject.get("name")!=null)
                    LegToName=legToJsonObject.get("name").toString();

                    if(legToJsonObject.get("geometry")!=null)
                    LegToGeometry=legToJsonObject.get("geometry").toString();
                    String LegToGeometryObject=legToJsonObject.get("geometry").toString();
                    JSONObject  jsonobj = (JSONObject)new JSONParser().parse(LegToGeometryObject);

                    legToStopCode=legToJsonObject.get("stopCode");
                    legToArrival=legToJsonObject.get("arrival");
                    legToDeparture=legToJsonObject.get("departure");
                    legToOrig=legToJsonObject.get("orig");
                    legToZoneId=legToJsonObject.get("zoneId");

                    if(legToJsonObject.get("lon")!=null)
                    legToLon= Double.valueOf(legToJsonObject.get("lon").toString());

                    if(legToJsonObject.get("lat")!=null)
                    legToLat= Double.valueOf(legToJsonObject.get("lat").toString());
                    LegToGeometryTypeObject= jsonobj.get("type");
                    if(LegToGeometryTypeObject!=null)
                    LegToGeometryType=(LegToGeometryTypeObject.toString());

                    LegToGeometrycoordinatesObject= jsonobj.get("coordinates");
                    if(LegToGeometrycoordinatesObject!=null)
                        LegToGeometrycoordinates=(LegToGeometrycoordinatesObject).toString();

                    System.out.println(LegToGeometryType+" LegToGeometryType");
                    System.out.println(LegToGeometryPoint+" LegToGeometryPoint");
                    System.out.println(LegToGeometrycoordinates+" LegToGeometrycoordinate");

                    legToStopId = new CLegToStopId(legToJsonObject);
                    legToStopId.getlegToStopId();
                }
            }
        }
        catch(Exception e)
        {
          System.out.println(e);
        }
        return(LegToName);
    }
}
