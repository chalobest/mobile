package gtfs.conv;
import java.util.Hashtable;
import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
public class GTFSReadIn
{

    static Hashtable<String, String[]> freq_table = new Hashtable<String, String[]>();
    String [] nextLine;
    String fileGTFSn;

        GTFSReadIn(String fileGTFS)
        {
             fileGTFSn= fileGTFS;
        }
    Hashtable read_gtfs()
    {
        try
        {
        CSVReader reader = new CSVReader(new FileReader(fileGTFSn), ',', '\"', 2);
            while ((nextLine = reader.readNext()) != null)
            {


                    String key = nextLine[ 0 ];
                    int ifreq = Integer.parseInt( nextLine[ 3 ] );
                    String[] varray={nextLine[ 1],nextLine[ 2],String.valueOf(ifreq),"false"};
                    if(freq_table.containsKey( key ) )
                    {
                        // avg
                        String[] currentVal = freq_table.get( key );
                        currentVal[2] = String.valueOf((ifreq + (Integer.parseInt(currentVal[2]))) / 2);

                        //currentVal[0] = myfunc_mintime(_varray[0],currentVal[0]);
                        currentVal[1] = varray[1]; //myfunc_maxtime(_varray[1],currentVal[1]);

                        freq_table.put( key ,currentVal );
                        if(key.equalsIgnoreCase("7051_4439_SAT,SUN&HOL_UP"))
                        {


                            System.out.println("tripid "+key);
                            System.out.println("start_time "+currentVal[0]);
                            System.out.println("end_time "+currentVal[1]);
                            System.out.println("freq "+currentVal[2]);
                        }
                    }
                    else
                    {
                        freq_table.put( key ,varray );

                        if(key.equalsIgnoreCase("7051_4439_SAT,SUN&HOL_UP"))
                        {
                            System.out.println("tripid "+key);
                            System.out.println("start_time "+varray[0]);
                            System.out.println("end_time "+varray[1]);
                            System.out.println("freq "+varray[2]);
                        }
                    }



            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e);
        }

        catch(IOException  e)
        {
            System.out.println(e);
        }
        return(freq_table);
    }

}









