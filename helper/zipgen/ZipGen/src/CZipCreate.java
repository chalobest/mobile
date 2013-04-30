/**
 * License: GPLv3
 */

import java.io.*;
import java.util.zip.*;

public class CZipCreate
{
    public static void writeGZ(String inFileName)
    {
        try
        {
            File file = new File(inFileName);
            FileOutputStream fos = new FileOutputStream(file + ".gz");
            GZIPOutputStream gzos = new GZIPOutputStream(fos);
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);

            System.out.println("Creating FAT filesystem (MS-DOS, OS/2, NT) GZip: " + inFileName + " to "+ file + ".gz");

            byte[] buffer = new byte[1024];
            int i = -1;

            while ((i = in.read(buffer)) >= 0)
                gzos.write(buffer,0,i);

            in.close();
            gzos.close();
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        if(args.length == 1)
            writeGZ(args[0]);
        else
            System.out.println("Raw Input Missing.");
    }
}
