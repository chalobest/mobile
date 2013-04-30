////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CPlan
{
    public String S="";
    public  String modeRoute="";
    public static String[][] FINALRES={{"mansi","","a","80000","800","800"},
            { "mansi","n","a","90000","800","800"},
            { "mansi","o","a","70000","800","800"}};
    public static  int i;
    public  String _sourceStopId;
    public  String destStopId="";
    public  String RESPONSE="";
    public  JSONParser parser=null;
    public  static JSONArray ItineraryListJsonObject=null;
    private String reMessage="";
    private String reBody="";
    private String output="";
    public CRequestParameters requestParameters=null;
    private CTrip ctrip=null ;
    public static  List<CTrip> plan =null;
    private  Object errorObject=null;
    private  Object planObject=null;
    private  Object t=null;
    private  Object obj=null;
    private  Object ItineraryListObject=null;
    private  Object ItineraryObject=null;
    private  HttpURLConnection con=null;
    public  static JSONObject jsonObject=null;
    public  JSONObject ItineraryJsonObject=null;
    private  URL m_theURL=null;
    private  int reCode=0;
    public  static JSONObject PlanJsonObject=null;

    public CPlan( )
    {


    }

    public String[][] getCRouteInfo(URL theURL)
    {
        plan= new ArrayList<CTrip>();
        m_theURL=theURL;
        System.out.println("in cplan");
        System.out.println("m_theURL"+ m_theURL);
        parser = new JSONParser();

        try
        {
            con = (HttpURLConnection)m_theURL.openConnection();
            reCode = con.getResponseCode();
            reMessage = con.getResponseMessage();
            reBody = con.getContent().toString();
            System.out.println("HTTP response and message: " + reCode  + " - " + reMessage );
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            System.out.println("Output from Server .... \n");

            while ((output = br.readLine()) != null)
            {
                System.out.println("output"+output);
                RESPONSE=output.toString();
                obj = parser.parse(output);
            }

            jsonObject= (JSONObject) obj;
            requestParameters=new CRequestParameters() ;
            requestParameters.getRequestParameters();
            planObject =  jsonObject.get("plan");
            errorObject=jsonObject.get("error");
            if(planObject!=null&&errorObject==null)
            {

                PlanJsonObject = (JSONObject) planObject;
                if(PlanJsonObject!=null)
                    ItineraryListObject=PlanJsonObject.get("itineraries");
                ItineraryListJsonObject = (JSONArray) ItineraryListObject;
                if(ItineraryListJsonObject!=null)
                    System.out.println("ItineraryListJsonObject.size() "+ItineraryListJsonObject.size());
                System.out.println("ItineraryListJsonObject.size()* "+ItineraryListJsonObject.size());
                for( i=0;i<ItineraryListJsonObject.size();i++)
                {
                    ItineraryObject = ItineraryListJsonObject.get(i);
                    ItineraryJsonObject = (JSONObject) ItineraryObject;
                    ctrip =new CTrip(ItineraryJsonObject);
                    ctrip.modeRoute="";
                    if(i!=0)
                    {
                        ctrip.S="";
                    }
                    FINALRES[i][1]=ctrip.getTripInfo();
                    plan.add(ctrip);
                    System.out.println(ctrip.description);
                    System.out.println(plan.get(i).tripStartTime+"plan.get(i).tripStartTime");
                }
                System.out.println("plan.size() "+plan.size());
                for(int m=0;m<plan.size();m++)
                {
                    System.out.println("m      "+m);
                    CTrip ctrip = plan.get(m);
                    System.out.println("CTrip.legs.size()*  "+ctrip.legs.size());
                    for(int k=0;k<plan.get(m).legs.size();k++)
                    {
                        modeRoute=plan.get(m).legs.get(k).modeRoute.toString();
                        System.out.println("k      "+k);
                        System.out.println("modeRoute         "+modeRoute);
                        if(S!="")
                            S=S.concat(modeRoute);
                        if(S=="")
                            S=modeRoute;
                        System.out.println("S    "+S);
                        int size=plan.get(m).legs.size();
                        if(k!=size-1)
                        S=S+", ";
                        System.out.println("size "+size);
                        if(k==size-1)
                        {
                            FINALRES[m][1]=S;
                            S="";
                        }
                    }
                }
            }
        }
        catch( IOException e )
        {
            System.out.println(   e );
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(planObject!=null&&errorObject==null)
        return(FINALRES);
        else
            return(null);
    }
}