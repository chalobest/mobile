/**
 * License: GPLv3
 */

package gtfs.conv;
import java.util.Hashtable;
import java.io.*;
import java.util.zip.*;

public class CMain
{
    public static void main(String[] args)
    {
       /* if(args.length !=2)
        {
            System.out.println("wrong no of parameters passed");
            System.exit(1);
        }  */

       /* String ptype = args[1];
        String fileGTFS = args[2];

        if (ptype.equals("F") )
        {
            GTFSReadIn rgtfs = new GTFSReadIn( );
            rgtfs.read_gtfs(fileGTFS);

        }          */
        String freq_gtfs= "D:\\dev\\transportation\\GTFS\\Mumbai\\frequencies.txt";
        GTFSReadIn rgtfs = new GTFSReadIn( freq_gtfs );
        Hashtable freq_table=rgtfs.read_gtfs();

        String freq_tabletxt= "D:\\dev\\transportation\\data\\Mumbai\\frequencies.txt";
        String sql_txt= "D:\\dev\\transportation\\convertHelper\\sqlquery2.txt";
        Tabular_writer writer = new  Tabular_writer(freq_tabletxt ,sql_txt ,freq_table );
        writer.write();
    }
}
