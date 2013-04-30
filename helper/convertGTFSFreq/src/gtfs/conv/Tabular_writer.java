package gtfs.conv;
import java.io.BufferedWriter;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.FileWriter;
import java.io.IOException;

public class Tabular_writer
{
    String key;
    String[] value;
    String frequency_tablen ;
    String sql_qeryn;
    Hashtable freq_tablen;
    Tabular_writer(String frequency_table ,String sql_qery ,Hashtable freq_table)
    {
        frequency_tablen= frequency_table;
        sql_qeryn= sql_qery;
        freq_tablen=  freq_table;
    }

    void write()
        {
            Enumeration keys = freq_tablen.keys();
            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter(frequency_tablen));
				
                while(keys.hasMoreElements())
                {
                    key = ((keys.nextElement()).toString());
                    value = (String[])((freq_tablen.get(key)));
                    out.write(  key  );
					
                    for(int i=0;i<4;i++)
                    {
                        out.write("~" );
                        out.write((  value[i]  ) );
						out.write( "'"+value[i]+"'");
						if(i<3)
							out.write("," );
                    }
                    out.write("\n");
					
                }
            }
            catch(IOException  e)
            {
                System.out.println(e);
            }

        }

}




