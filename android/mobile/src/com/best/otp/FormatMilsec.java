////////////////////////////////////////////////
//
// License: GPLv3
//
//


package com.best.otp;
import android.text.format.Time;

/**
 * Created with IntelliJ IDEA.
 * User: programmer
 * Date: 4/17/13
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class FormatMilsec {


    public String _milisec="";
    public String  am_pmn="am";
    public Long _longmilli=null;

    FormatMilsec(String milisec)
    {
        System.out.println("milisec "+milisec);
        _milisec=milisec;

    }

    public  String getDate()
    {
        String timeS="";
        if(_milisec!=null)
        {
            _longmilli= Long.parseLong(_milisec);
            Time time = new Time();
            time.set(_longmilli);
            long gmtoff=time.gmtoff;
            String Timezone0=time.getCurrentTimezone ();
            int hour=time.hour-12;
            if(time.hour>=12)
                am_pmn="pm";
            int minute=time.minute;
            String Timezone1=time.timezone;
            System.out.println("gmtoff "+ gmtoff);
            System.out.println("hour "+ hour);
            System.out.println("minute "+ minute);
            System.out.println("Timezone1 "+ Timezone1);
            System.out.println("Timezone0 "+ Timezone0);
            timeS= String.valueOf(hour)+":"+ String.valueOf(minute)+String.valueOf(am_pmn) ;

        }
        return timeS;
    }
}
