////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONObject;

public class CLeglegGeometry
{

    private Object legLegGeometryObject=null;
    private JSONObject m_legJsonObject=null;
    public String legLegGeometryPoints="";
    public Object legLegGeometrylevels=null;
    public long legLegGeometrylength=0;

    CLeglegGeometry(JSONObject legJsonObject)
    {
        m_legJsonObject= legJsonObject;
    }

    void getLegGeometry()
    {
        if(m_legJsonObject!=null)
        {
            legLegGeometryObject=m_legJsonObject.get("legGeometry");
            if(legLegGeometryObject!=null)
            {
                JSONObject legLegGeometryJsonObject = (JSONObject) legLegGeometryObject;
                if(legLegGeometryJsonObject.get("points")!=null)
                legLegGeometryPoints =legLegGeometryJsonObject.get("points").toString();
                legLegGeometrylevels =legLegGeometryJsonObject.get("levels");
                if(legLegGeometryJsonObject.get("length")!=null)
                legLegGeometrylength= Long.valueOf(legLegGeometryJsonObject.get("length").toString());
            }
        }
    }
}
