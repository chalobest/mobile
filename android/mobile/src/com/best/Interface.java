////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best;
import com.best.otp.CMain;

public class Interface
{
    public CMain cmain;
    public static String[][] _resultSetf,_resultSetd,finalres;
    public static String[] S,_dateTimeArray;
    public static String _Fromlatitude,_Fromlongitude,_Tolatitude,_Tolongitude;
    public static boolean moden;

    public Interface()
    {

    }

    public  String[][] getMode(boolean mode, String[][] resultSetf,String[][] resultSetd,String[] dateTimeArray)
    {
        System.out.println("in interface");
        moden=mode;

        if(moden==true)
        {
            System.out.println("in interface if");
            if(resultSetf!=null && resultSetd!=null)
            {
                System.out.println("in interface if in condition of not null");
                _dateTimeArray=dateTimeArray;
                System.out.println(_dateTimeArray.length);
                System.out.println(_dateTimeArray[0]);
                System.out.println(_dateTimeArray[1]);
                System.out.println(_dateTimeArray[2]);
                System.out.println(_dateTimeArray[3]);
                System.out.println(_dateTimeArray[4]);
                System.out.println(_dateTimeArray[5]);
                _resultSetf=resultSetf;
                _resultSetd=resultSetd;
                _Fromlatitude = _resultSetf[0][1] ;
                _Fromlongitude= _resultSetf[0][2];
                System.out.println(_Fromlatitude+"_Fromlatitude");
                System.out.println(_Fromlongitude+"_Fromlongitude");

                _Tolatitude = _resultSetd[0][1] ;
                _Tolongitude= _resultSetd[0][2];
                System.out.println(_Tolatitude+"_Tolatitude");

                System.out.println(_Tolongitude+"_Tolongitude");
                S=new String[]{_Fromlatitude,_Fromlongitude,_Tolatitude,_Tolongitude,_dateTimeArray[0],_dateTimeArray[1],_dateTimeArray[2],_dateTimeArray[3],_dateTimeArray[4],_dateTimeArray[5]};
                System.out.println(S.length);

                System.out.println("web mode");
                System.out.println();
                System.out.println();
                cmain=new CMain(S);
                finalres=cmain.mainMethodOfOtp();
            }
        }
        else
        System.out.println("offline mode");
        return(finalres);
    }
}
