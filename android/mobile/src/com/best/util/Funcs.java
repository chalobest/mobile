////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.util;

import java.util.Calendar;
import java.util.Formatter;

public class Funcs{

	public static String addTime(String time, String addPeriod)
	{
		//expects both parameter in format "hh:mm:ss"
		
		String[] timeSplit = time.split( ":" );
		String[] periodSplit = addPeriod.split( ":" );
		
		// "01:50:59" + 
		// "19:32:20" 
		// ---------
		// "21:23:19"
		
		if( timeSplit.length == 3 && periodSplit.length == 3 )
		{
			String newTime = "";
			boolean useOver = false;
			for( int cnt = 2; cnt >= 0; cnt-- )
			{
				int part1 = Integer.parseInt( timeSplit[ cnt ] );
				int part2 = Integer.parseInt( periodSplit[ cnt ] );
				
				int sum = part1 + part2 + ( ( useOver )?( 1 ):( 0 ) );
				
				useOver = false;
				if( cnt > 0 )
				{
					if( sum >= 60 )
					{
						sum = sum - 60;
						useOver = true;
					}
				}
				else
				{
					if( sum >= 24 )
					{
						sum = 0;
						useOver = true;
					}	
				}
				
				newTime = ( ( (""+sum).length() == 2 )?( sum ):( "0"+sum ) ) + ( ( cnt < 2 )?( ":" ):( "" ) ) + newTime;
			}
			
			return( newTime );
		}	
		
		return( null );
	}
	
	public static long timeToSeconds(String t)
	{
		t = t.trim();
		
		if( t.indexOf( " " ) > 0 )
		{
			String[] _tmp = t.split( " " );
			t = _tmp[ 0 ];
		}
		
		if( t.indexOf( "+" ) > 0 )
		{
			String[] _tmp = t.split( "\\+" );
			t = _tmp[ 0 ];
		}
		
		String[] t_split = t.split( ":" );
		int hr = 0; int min = 0; long sec = 0;
		
		int len = t_split.length;
		if( len >= 3 )
		{
			try{ hr = Integer.parseInt( t_split[ 0 ] ); }catch(Exception e){} 
			try{ min = Integer.parseInt( t_split[ 1 ] ); }catch(Exception e){}  
			try{ sec = Integer.parseInt( t_split[ 2 ] ); }catch(Exception e){} 
			
			sec += ( min * 60 ) + ( hr * 3600 );
		}
		else if( len == 2 )
		{
			try{min = Integer.parseInt( t_split[ 0 ] ); }catch(Exception e){} 
			try{sec = Integer.parseInt( t_split[ 1 ] ); }catch(Exception e){} 
			
			sec += ( min * 60 );
		}
		else
			try{ sec = Integer.parseInt( t_split[ 0 ] ); }catch(Exception e){} 
		
		if(t.contains("PM") && hr != 12)
			sec += 12 * 3600;
		return( sec );
		
	}
	
	public static String secondsToTimeFormat( int seconds)
	{
		int hr = seconds/3600;
		seconds = seconds%3600;
		int min = seconds/60;
		seconds = seconds%60;
		
		return(hr+":"+min+":"+seconds);
				
	}
	public static String secondsToMinHr( int seconds)
	{
		int hr = seconds/3600;
		seconds = seconds%3600;
		int min = seconds/60;
		seconds = seconds%60;
		
		if(hr > 0)
		{
			if(min > 0)
			{
				if(seconds > 0)
					return (hr+"hr "+min+"min "+seconds+"sec");
				else
					return (hr+"hr "+min+"min ");
			}
			else
				return (hr+"hr ");
		}
		else if(min > 0)
		{
			if(seconds > 0)
				return (min+"min "+seconds+"sec");
			else
				return (min+"min ");
		}
		else
			return (seconds+"sec");
				
	}
	public static String subtractTime(String timeStart, String timeEnd)
	{
		//expects both parameter in format "hh:mm:ss"
	    String[] timeStartSplit = timeStart.split( ":" );
		String[] timeEndSplit = timeEnd.split( ":" );
		
		if( timeStartSplit.length == 3 && timeEndSplit.length == 3 )
		{
			String newTime = "";
			boolean useOver = false;
			for( int cnt = 2; cnt >= 0; cnt-- )
			{
				int part1 = Integer.parseInt( timeStartSplit[ cnt ] );
				int part2 = Integer.parseInt( timeEndSplit[ cnt ] );
				
				boolean resetUO = true;
				if( cnt > 0 && part2 == 0 )
				{	part2 = 60; resetUO = false; }
				
				int diff = part2 - part1 - ( ( useOver )?( 1 ):( 0 ) );
				
				if( resetUO )
					useOver = false;
				else
					useOver = true;
				if( cnt > 0 )
				{
					if( diff < 0 )
					{
						diff = diff * -1;
						useOver = true;
					}
				}
				else
				{
					if( diff < 0 )
					{
						diff = 0;
						useOver = true;
					}	
				}
				
				newTime = ( ( (""+diff).length() == 2 )?( diff ):( "0"+diff ) ) + ( ( cnt < 2 )?( ":" ):( "" ) ) + newTime;
			}
			
			return( newTime );
		}	
		
		return( null );
	}
	
	public static String formatTimeSpan(String timeSpan)
	{
        String timeSpanFormatd = "";
		String[] timeSpanSplit = timeSpan.split(":");
		int timeSpanHr = 0;
		int timeSpanMin = 0;
		int timeSpanSec = 0;
		
		try{ timeSpanHr = Integer.parseInt( timeSpanSplit[ 0 ] ); }catch( Exception e ){}
		try{ timeSpanMin = Integer.parseInt( timeSpanSplit[ 1 ] ); }catch( Exception e ){}
		try{ timeSpanSec = Integer.parseInt( timeSpanSplit[ 2 ] ); }catch( Exception e ){}
		
		timeSpanFormatd = timeSpanHr+":";
		if( timeSpanSec > 30 )
			timeSpanMin++;
		timeSpanFormatd += timeSpanMin;
		return( timeSpanFormatd );
		
	}
	
	public static double roundDouble(double value, int places) {
	    if (places > 0) 
		{
            long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
		return value;
	}
	
	public static String getCurrentDay(){
		return( ( new Formatter() ).format( "%ta", Calendar.getInstance() ).toString() );
	}
	
	public static String getCurrentTime(boolean _24HrBased){
		if( _24HrBased )
			return( ( new Formatter() ).format( "%tT", Calendar.getInstance() ).toString() );
		else
			return( ( new Formatter() ).format( "%tr", Calendar.getInstance() ).toString() );	
	}
	public static String getCurrentDateTime()
	{
		return( ( new Formatter() ).format( "%tF %tT", Calendar.getInstance(), Calendar.getInstance() ).toString());
	
	}
	public static Calendar getNextDay(Calendar calendar){
		calendar.add( Calendar.DAY_OF_MONTH, +1 );
		return( calendar );
	}

	public static Calendar addTime(Calendar calendar, String time){
		try{
			String s[] = time.split( ":" );
			
			calendar.add( Calendar.HOUR, Integer.parseInt( s[ 0 ] ) );
			calendar.add( Calendar.MINUTE, Integer.parseInt( s[ 1 ] ) );
			
			return( calendar );
		}catch( Exception e ){}
		return( null );	
	}	
	
	public static String[] getHashKeys(	java.util.Hashtable h)
	{
		String[] starr = new String[ h.size() ];
		java.util.Enumeration e = h.keys();
		int c = 0;
		while( e.hasMoreElements() )
		{
			starr[ c++ ] = (String)e.nextElement();
		}
		
		return( starr );
	}
}