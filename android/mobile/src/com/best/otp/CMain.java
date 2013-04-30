////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.otp;
import java.net.URL;


public class CMain
    {
        public String _args[];
        public String[][] result;
        public String _sourcelat="";
        public String _sourcelon="";
        public String _hour="";
        public String _minute="";
        public String _am_pm="";
        public String _MONTH="";
        public String _destlat="";
        public String _destlon="";
        public String _DATE="";
        public String _YEAR="";
        public String _am_pmn="";
        public String _url="";

        public CMain( String args[])
        {
            System.out.println("in cmaintry");
            System.out.println(args[0]+"args[0]");
            System.out.println(args[1]+"args[1]");
            _sourcelat= args[0];
            _sourcelon = args[1];
            System.out.println(args[2]+"args[2]");
            System.out.println(args[3]+"args[3]");
            _destlat= args[2];
            _destlon = args[3];
            _hour= args[4];
            _minute=args[5];
            _am_pm =args[6];
            _MONTH =args[8];
            _DATE =args[7];
            _YEAR =args[9];
        }
         public  String[][] mainMethodOfOtp ( )
        {
            System.out.println("in cmain");
            try
            {
                System.out.println(_hour + "hour");
                System.out.println(_minute+"minute");
                System.out.println(_am_pm+"am_pm");
                System.out.println(_MONTH+"MONTH");
                System.out.println(_DATE+"DATE");
                System.out.println(_YEAR+"YEAR");

                _url="http://trip.chalobest.in/opentripplanner-api-webapp/ws/plan?_dc=1362721402899&arriveBy=false&time="+_hour+"%3A"+_minute+_am_pm+"&date="+_MONTH+"-"+_DATE+"-"+_YEAR+"&mode=TRANSIT%2CWALK&optimize=QUICK&maxWalkDistance=840&routerId=&toPlace="+_destlat+"%2C"+_destlon+"&fromPlace="+_sourcelat +
"%2C"+_sourcelon;
                System.out.println("_url"+_url);
               /* _url="http://trip.chalobest.in/opentripplanner-api-webapp/ws/plan?_dc=1362996826838&arriveBy=false&time=3%3A43pm&date=03-11-2013&mode=TRANSIT%2CWALK&optimize=QUICK&maxWalkDistance=840&routerId=&toPlace=19.194974%2C72.990504&fromPlace=19.043162%2C72.921153"; */

                CPlan plan=new CPlan();
                result= plan.getCRouteInfo(new URL(_url));

            }
            catch (Exception e)
            {
                System.out.println("e" +e );
                System.out.println(e.getStackTrace()+"*");
            }
         return(result);
        }

       public static void logIt(String str)
       {
           System.out.println("str");
       }
    }

