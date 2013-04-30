////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.data;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Date;
import java.util.Calendar;
import java.util.Hashtable;
import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;

import com.best.ui.Best;
import com.best.util.Funcs;

public class GTStore{

	public static int M_MAX_STOP_SEARCH_ROW_BY_TEXT = 30;
	public static int M_MAX_RANGE = 500;
	
	public static int M_STOP_NEAR_POINT_DISTANCE_INDEX = 0;
	public static int M_STOP_NEAR_POINT_LAT_INDEX = 1;
	public static int M_STOP_NEAR_POINT_LON_INDEX = 2;

	public static int M_STOP_INFO_ID_INDEX = 0; 
	public static int M_STOP_INFO_LAT_INDEX = 1;
	public static int M_STOP_INFO_LON_INDEX = 2;
	public static int M_STOP_INFO_SEQ_INDEX = 3;
	public static int M_STOP_INFO_ARR_TIME_INDEX = 4;
	public static int M_STOP_INFO_ARR_DISTANCE_INDEX = 5;

	public static double M_WALING_DISTANCE_KM = 0.3;
	
	public static double GEO_DELTA = 0.003;
	
	public static String[][] getAllStopsByText(String matchString, Double lat, Double lon)
	{
		try{
			//String query =  "select g._stopID, _stopName, Distance( _stopLoc ,ST_GeomFromText('POINT("+ lat +" "+ lon +" )'))*111 distance from stop_names n, stop_geo g where n._stopID = g._stopID and ( _stopName like '"+matchString+"%' or _stopName like '% "+matchString+"%' ) limit "+M_MAX_STOP_SEARCH_ROW_BY_TEXT+";";
			
			String query =  "select g._stopID, _stopNameMarathi, Distance( _stopLoc ,ST_GeomFromText('POINT("+ lat +" "+ lon +" )'))*111 distance from stop_names n, stop_geo g where n._stopID = g._stopID and ( _stopNameMarathi like '"+matchString+"%' or _stopNameMarathi like '% "+matchString+"%' ) UNION select g._stopID, _stopNameMarathi, Distance( _stopLoc ,ST_GeomFromText('POINT("+ lat +" "+ lon +" )'))*111 distance from stop_names n, stop_geo g where n._stopID = g._stopID and ( _stopDesc like '"+matchString+"%' or _stopDesc like '% "+matchString+"%' ) order by distance limit "+M_MAX_STOP_SEARCH_ROW_BY_TEXT+";";
			
			System.out.println("===query=="+query);
			String[][] resultSet=DBHandle.execQuery( query, M_MAX_STOP_SEARCH_ROW_BY_TEXT );
					
			return( resultSet );
		}catch(Exception e){ System.out.println("===EXEC ERR=="+e.getMessage() ); }
		return( null );
	}
	
	public static String[][] getNearByStops(Double lat, Double lon)
	{
		String nearByStopQuery = "select distinct g._stopID,_stopNameMarathi, Distance( _stopLoc, ST_GeomFromText('POINT(" + lat + " " + lon + ")') )*111 distance from stop_geo g, stop_names n where g._stopID = n._stopID and Distance( _stopLoc, ST_GeomFromText('POINT(" + lat + " " + lon + ")') )*111 <= 0.5 order by distance asc limit 30 ;";
		
		//"select g._stopID,_stopName, Distance( _stopLoc, ST_GeomFromText('POINT(" + lat + " " + lon + ")') )*111 distance from stop_geo g, stop_names n where g._stopID = n._stopID and PtDistWithin( _stopLoc, ST_GeomFromText('POINT(" + lat + " " + lon + ")', 4326),"+M_MAX_RANGE+") order by distance asc limit 30;";
		Best.log("nearByStopQuery=="+nearByStopQuery);
		
		String[][] resultSet = DBHandle.execQuery(nearByStopQuery,M_MAX_STOP_SEARCH_ROW_BY_TEXT);
		
		return(resultSet);
	}
	
	public static String[][] getLatitudeLongitude(String stopID)
	{
		String[][] resultSet=DBHandle.execQuery( "select _stopID,X(_stopLoc),Y(_stopLoc) from stop_geo where stop_geo._stopID="+stopID+";", 1 );
		
		return(resultSet);
	}
	public static void insertIntoPreviousSearch(String sourceID, String destID)
	{
		String currentDateTime = Funcs.getCurrentDateTime();
		String delete_query = "delete from previous_search where _sourceStopID = '"+sourceID+"' and _destStopID = '"+destID+"';";
		Best.log(delete_query);
		//DBHandle.execInsDelQuery( delete_query );
		
		String insert_query = "insert into previous_search (_sourceStopID,_destStopID,_searchDate) values ('"+sourceID+"','"+destID+"','"+currentDateTime+"');";
		Best.log(insert_query);
		DBHandle.execInsertQuery( insert_query );
		
		String[][] res = DBHandle.execQuery("select * from previous_search;");
		//System.out.println("RESULT SIZE = "+res.length+" NUMBER OF ENTERIES = "+res[0][0]);
		if(res != null && res.length > 0)
		{
			for(int i =0 ;i < res.length; i++)
			{
				for(int j = 0 ; j < res[i].length ;j++)
					{System.out.println("RESULT = "+res[i][j]);}
			}
		}
	}
	public static String[][] getPreviousSearches()
	{
		String query = "select _searchID, _sourceStopID,(select _stopNameMarathi from stop_names where _stopID = _sourceStopID), _destStopID,(select _stopNameMarathi from stop_names where _stopID = _destStopID), _searchDate from previous_search;";
		return null ;
	}
	
	/*public static String[][] getRoutes(String fromStopID, String toStopID, String day, String timeStart,String timeEnd)
	{
		try{
			// String _qryForAllRouteForFromStop = "select stops_on_trip._tripid , stops_on_trip._stopid, stops_on_trip._stopseq, stops_on_trip._stopdept,stops_on_trip._stopDeptOnNextDay from stops_on_trip, trips, schedule_rules where stops_on_trip._tripid = trips._tripid and trips._serviceid = schedule_rules._serviceid and stops_on_trip._stopid='"+fromStopID+"' and schedule_rules._serviceday='"+day+"' and schedule_rules._startperioddate<=date('now') and date('now')<=schedule_rules._endperioddate and stops_on_trip._stopDept > strftime('%H:%M:%S', '"+timeStart+"') and strftime('%H:%M:%S', '"+timeEnd+"') >(stops_on_trip._stopDept) and stops_on_trip._stopDeptOnNextDay=0";

			// String _qryForAllRouteForFromStop = "select stops_on_trip._tripid , stops_on_trip._stopid, stops_on_trip._stopseq, stops_on_trip._stopdept,stops_on_trip._stopDeptOnNextDay from stops_on_trip, trips, schedule_rules where stops_on_trip._tripid = trips._tripid and trips._serviceid = schedule_rules._serviceid and stops_on_trip._stopid='714' and schedule_rules._serviceday='Thu'and strftime('%Y%m%d',schedule_rules._startperioddate) <=strftime('%Y%m%d','20120117') and strftime('%Y%m%d','20120117') <= strftime('%Y%m%d',schedule_rules._endperioddate) and stops_on_trip._stopDept > strftime('%H:%M:%S', '09:00:00') and strftime('%H:%M:%S', '09:25:00') >(stops_on_trip._stopDept) and stops_on_trip._stopDeptOnNextDay=0;";
			
			String _qryForAllRouteForFromStop = "select stops_on_trip_frequency._uniqtripID,stops_on_trip_frequency._stopid, stops_on_trip_frequency._stopseq, trips_sum._tripid, trips_sum._start, trips_sum._start_next_day, trips_sum._freq, st_distance( stop_geo._stopLoc , st_pointFromText( 'POINT( lat lon )' ) )*111 as distd from stop_geo, stops_on_trip_frequency, schedule_rules, trips_sum where trips_sum._uniqtripID = stops_on_trip_frequency._uniqtripID and trips_sum._serviceid = schedule_rules._serviceid and stops_on_trip_frequency._stopid=stop_geo._stopid and schedule_rules._serviceday='Thu' and schedule_rules._startperioddate <='2012-01-17' and '2012-01-17' <= schedule_rules._endperioddateand stops_on_trip_frequency._stopid in ('714') order by distd;";
			
			String[][] resultSet = DBHandle.execQuery( _qryForAllRouteForFromStop, M_MAX_STOP_SEARCH_ROW_BY_TEXT );
			
			Hashtable _allEmergingRoutes = null;
			if( resultSet != null )
			{
				String _tripIdSet = "";
				_allEmergingRoutes = new Hashtable( resultSet.length );
				
				for( int cnt=0; cnt < resultSet.length; cnt++ )
				{
					Vector _tripInfo = new Vector( 2 );
					String _tripId = resultSet[ cnt ][ 0 ];
					_tripInfo.add( resultSet[ cnt ][ 2 ] ); //StopSequence
					_tripInfo.add( resultSet[ cnt ][ 3 ] ); //deprtrTime
					_allEmergingRoutes.put( _tripId, _tripInfo );//trip id is key
					
					_tripIdSet += ( ( cnt > 0 )?( "," ):( "" ) ) + "'"+_tripId+"'";
				}	
				
				if( _tripIdSet.length() > 0 )
				{
					// String _qryForAllRouteForToStop = "select stops_on_trip._tripid, trips._tripHeadSign, routes._routeName, stops_on_trip._stopid , stops_on_trip._stopseq, stops_on_trip._stopArri, stops_on_trip._stopArriOnNextDay from stops_on_trip, stop_geo, trips, routes, schedule_rules where routes._routeID=trips._routeID and stops_on_trip._stopid=stop_geo._stopid and stops_on_trip._tripid=trips._tripid and trips._serviceid=schedule_rules._serviceid and schedule_rules._serviceday='"+ day +"' and schedule_rules._startperioddate<=date('now') and date('now')<=schedule_rules._endperioddate and trips._tripid in ("+ _tripIdSet +") and stops_on_trip._Stopid='"+toStopID+"';";
					
					String _qryForAllRouteForToStop = "select stops_on_trip._tripid, trips._tripHeadSign, routes._routeName, stops_on_trip._stopid , stops_on_trip._stopseq, stops_on_trip._stopArri, stops_on_trip._stopArriOnNextDay from stops_on_trip, stop_geo, trips, routes, schedule_rules where routes._routeID=trips._routeID and stops_on_trip._stopid=stop_geo._stopid and stops_on_trip._tripid=trips._tripid and trips._serviceid=schedule_rules._serviceid and schedule_rules._serviceday='Thu' and strftime('%Y%m%d',schedule_rules._startperioddate)<=strftime('%Y%m%d','20120117') and strftime('%Y%m%d','20120117')<=strftime('%Y%m%d',schedule_rules._endperioddate ) and trips._tripid in ( 'LA0010052','LA0010053','LA0010081','LA0010082' ) and stops_on_trip._Stopid='1750';";
					
					resultSet = DBHandle.execQuery( _qryForAllRouteForToStop, M_MAX_STOP_SEARCH_ROW_BY_TEXT );
					
					if( resultSet != null && resultSet.length > 0 )
					{
						String[][] _returnArr = new String[ resultSet.length ][ 6 ];
						for( int cnt=0; cnt < resultSet.length; cnt++ )
						{
							String _tripId = resultSet[ cnt ][ 0 ];
							if( _allEmergingRoutes.containsKey( _tripId ) )
							{
								Vector v = ( Vector )_allEmergingRoutes.get( _tripId );
								int _fromStopSeq = Integer.parseInt( ( String ) v.elementAt( 0 ) );
								String _deptTime = ( String ) v.elementAt( 1 );
								
								String _tripHeadSign = resultSet[ cnt ][ 1 ];
								String _routeName = resultSet[ cnt ][ 2 ];
								int _toStopSeq = Integer.parseInt( resultSet[ cnt ][ 4 ] );
								String _arrivalTime = resultSet[ cnt ][ 5 ];
								
								Best.log( _routeName+" -- "+_tripHeadSign+" -- "+(_toStopSeq - _fromStopSeq)+" -- "+_deptTime+" -- "+_arrivalTime );
								
								if( _toStopSeq > _fromStopSeq )
								{
									_returnArr[ cnt ][ 0 ] = _tripId;
									_returnArr[ cnt ][ 1 ] = _routeName;
									_returnArr[ cnt ][ 2 ] = _tripHeadSign;
									_returnArr[ cnt ][ 3 ] = (_toStopSeq - _fromStopSeq)+"";
									_returnArr[ cnt ][ 4 ] = _deptTime;
									_returnArr[ cnt ][ 5 ] = _arrivalTime;
								}	
							}
							//else shud never happen
						}
						
						return( _returnArr );
					}
				}		
			}
		}catch(Exception e){}
		return( null );
	} */
	
	public static String[][] getTripInfo(String tripID)
	{
		String q = "select distinct _stopNameMarathi, stop_names._stopID _stopID, _stopSeq ,X(_stopLoc),Y(_stopLoc) from trips, stops_on_trip, stop_names, stop_geo where trips._tripID = '"+tripID+"' and trips._tripID = stops_on_trip._tripID and stops_on_trip._stopID = stop_names._stopID and stop_geo._stopID = stop_names._stopID order by _stopSeq asc;";
		
		System.out.println("==Query:getTripInfo=="+q);
		return (  DBHandle.execQuery( q ) );
	}
	
	/* public static Vector searchRoutes(int rec_level,String fromStopId,String lat_from,String lon_from,String lat_to,String lon_to,Calendar dept_date,Calendar dept_time)
	{
	
		double waling_distance_km = 0.3;
		rec_level++;
		
		Hashtable singleUniqueTrips = new Hashtable();
		Hashtable tripTimes = new Hashtable();
		Hashtable doneTrips = new Hashtable();
		
		Vector workingTrips = null;
		
		String dept_day = Funcs.getCurrentDay();
		
		String q = "select stops_on_trip_frequency._uniqtripID,stops_on_trip_frequency._stopid, stops_on_trip_frequency._stopseq, trips_sum._tripid, trips_sum._start, trips_sum._start_next_day, trips_sum._freq, st_distance( stop_geo._stopLoc , st_pointFromText( 'POINT( "+lat_from+" "+lon_from+")' ) )*111 as distd from stop_geo, stops_on_trip_frequency, schedule_rules, trips_sum where trips_sum._uniqtripID = stops_on_trip_frequency._uniqtripID and trips_sum._serviceid = schedule_rules._serviceid and stops_on_trip_frequency._stopid=stop_geo._stopid and schedule_rules._serviceday='"+dept_day+"' and schedule_rules._startperioddate<=date( 'now' ) and date( 'now' )<=schedule_rules._endperioddate and stops_on_trip_frequency._stopid = '"+fromStopId+"' order by distd ;";
		
		String[][] resultSet = DBHandle.execQuery( q );
		
		if( resultSet != null )
		{
			for( int cnt = 0; cnt <= resultSet.length; cnt++ )
			{
				String utripID = resultSet[ cnt ][ 0 ];  
				String tripID = resultSet[ cnt ][ 3 ];
				
				if( ! singleUniqueTrips.containsKey( utripID ) )
				{
					String stopID = resultSet[ cnt ][ 1 ]; 
					int stopSeq = Integer.parseInt( resultSet[ cnt ][ 2 ] );
					boolean start_time_tom = ( resultSet[ cnt ][5].equals( "t" ) );
					String _dept_time = resultSet[ cnt ][ 4 ];
					//TODO: needs to reworked
					Calendar start_time = Funcs.addTime( dept_date, _dept_time );
					
					if( !start_time_tom )
						start_time = Funcs.getNextDay( start_time ); 
						
					int stop_freq = Integer.parseInt( resultSet[ cnt ][ 6 ] );
					long stop_time = ( start_time.getTimeInMillis() + ( stopSeq * stop_freq) );
					/////////////////
					
					if( ( ( stop_time - 1500 ) < dept_time.getTimeInMillis() ) && ( ( stop_time + 1500 ) > dept_time.getTimeInMillis() ) )
					{
						Vector v = new Vector();
						v.add( start_time );
						v.add( stop_freq );
						tripTimes.put( tripID, v );
						
						Vector vTripInfo = init_dec( stopID, lat_from, lon_from, stopSeq+"", stop_time+"", "" );
						singleUniqueTrips.put( utripID, vTripInfo );
					}
				}	
			}
			
			allKeys = tripTimes.keys();
	
			String trip_list = "";
			while( allKeys.hasMoreElements() )
				trip_list = ( ( trip_list.length() > 0 )?( "," ):( "" ) )+"'"+( String )allKeys.nextElement()+"'";
			
			if( singleUniqueTrips.size() > 0 )
			{
				q = "select stops_on_trip_frequency._uniqtripID, trips_sum._tripID, trips_sum._tripHeadSign, routes._routeName, stops_on_trip_frequency._stopid , stops_on_trip_frequency._stopseq, st_distance( stop_geo._stopLoc , st_pointFromText( 'POINT( "+lat_to+" "+lon_to+")' ) )*111 as distd, ST_X( stop_geo._stopLoc ) as xlat, ST_Y( stop_geo._stopLoc ) as ylon from stops_on_trip_frequency, stop_geo, trips_sum, routes where stops_on_trip_frequency._uniqtripID=trips_sum._uniqtripID and routes._routeID=trips_sum._routeID and stops_on_trip_frequency._stopid=stop_geo._stopid and trips_sum._tripID in ("+trip_list+") order by trips_sum._tripID, distd ;";
				
				resultSet = DBHandle.execQuery( q );
				if( resultSet != null )
				{
					int max_check = 0;
					for( int cnt = 0; cnt <= resultSet.length; cnt++ )
					{
						String utripID = resultSet[ cnt ][ 0 ];
						String tripID = resultSet[ cnt ][ 1 ]; 
						String tripSign = resultSet[ cnt ][ 2 ]; 
						String routeName = resultSet[ cnt ][ 3 ];
						String stopID = resultSet[ cnt ][ 4 ]; 
						int stopSeq = Integer.parseInt( resultSet[ cnt ][ 5 ] );
						double stopDistanceFromDestination = Double.parseDouble( resultSet[ cnt ][ 6 ] );
						String stopLat = resultSet[ cnt ][ 7 ];
						String stopLon = resultSet[ cnt ][ 8 ];
						
						if( !doneTrips.containsKey( utripID ) )
						{
							Vector start_stop_dec = ( Vector ) singleUniqueTrips.get( utripID );
							if( start_stop_dec.size() == 6 )
							{
								int stored_stopSeq = Integer.parseInt( (String)start_stop_dec.elementAt( 3 ) );
								if( stored_stopSeq < stopSeq )
								{
									if( stopDistanceFromDestination <= waling_distance_km )
									{
										Vector tripVector = ( Vector )tripTimes.get( tripID );
										long stop_arr_time = Long.parseLong( ( String )tripVector.elementAt( 0 ) ) + ( Long.parseLong(( ( String )tripVector.elementAt( 1 ) ) ) * stopSeq );
										
										doneTrips.put( utripID, true );
										
										workingTrips = insertWorkingTrips( workingTrips, calc_createWorkingRoute( tripID, utripID, tripSign, routeName, start_stop_dec, init_dec( stopID, stopLat, stopLon, stopSeq+"", stop_arr_time+"", stopDistanceFromDestination+"" ), new Vector() ) );
									}
								}
							}
						}
					}
				}
			}
		}
		
		return( workingTrips );
	} */
	
	public static Vector findRoutes(double lat_from,double lon_from,double lat_to,double lon_to,String srcDay)
	{
		Vector workingTrips = new Vector();
		
		Hashtable allNearByStops = getAllNearByBustStops( lat_from, lon_from );
		
		String[][] _allPassingRoutes = getAllPassingRoutesFromStops( Funcs.getHashKeys( allNearByStops ), lat_from, lon_from, srcDay );
		if( _allPassingRoutes != null )
		{
			Hashtable tripTimes = new Hashtable();
			Hashtable _uniqPassingRoutes = new Hashtable();
			Hashtable singleUniqueTrips = new Hashtable();
			
			Best.log( " _allPassingRoutes - size : " + _allPassingRoutes.length );
			
			for( int cnt = 0; cnt < _allPassingRoutes.length; cnt++ )
			{
				String _uniqRouteId = _allPassingRoutes[ cnt ][ 0 ];
				Best.log( " _allPassingRoutes - cnt : " + cnt + " -- " + _uniqRouteId );
				
				if( !_uniqPassingRoutes.containsKey( _uniqRouteId ) )
				{
					Best.log( " _allPassingRoutes - new" );
					String stopID = _allPassingRoutes[ cnt ][ 1 ];
					Best.log( " _allPassingRoutes - stopID : " + stopID );
					String _currTime = Funcs.getCurrentTime( true );
					String _busStartTime = _allPassingRoutes[ cnt ][ 4 ];
					
					int stopSeq = Integer.parseInt( _allPassingRoutes[ cnt ][ 2 ] );
					int stop_freq = Integer.parseInt( _allPassingRoutes[ cnt ][ 6 ] );
					double stop_time = ( Funcs.timeToSeconds( _busStartTime ) + ( stopSeq *  stop_freq ) );
					
					long dept_time = Funcs.timeToSeconds( _currTime );
					Best.log( " stop_time: " + stop_time + " -- dept_time "+ dept_time );	
					if( ( ( stop_time - 1500 ) < dept_time ) && ( ( stop_time + 1500 ) > dept_time ) )
					{
						String tripID = _allPassingRoutes[ cnt ][ 3 ];
						
						Vector v = new Vector( 2 );
						v.add( Long.toString( dept_time ) );
						v.add( Integer.toString( stop_freq ) );
						tripTimes.put( tripID, v );
						
						Vector _t = ( Vector ) allNearByStops.get( stopID );
						
						Vector vTripInfo = init_dec( stopID, ( String ) _t.elementAt( M_STOP_NEAR_POINT_LAT_INDEX ), ( String ) _t.elementAt( M_STOP_NEAR_POINT_LON_INDEX ) , Integer.toString( stopSeq ), Double.toString( stop_time ), "0" );
						singleUniqueTrips.put( _uniqRouteId, vTripInfo );
					}
				}
			}

			Best.log( " singleUniqueTrips - size : " + singleUniqueTrips.size() );	
			if( singleUniqueTrips.size() > 0 )
			{
				String trip_list = "'" + android.text.TextUtils.join( "','", Funcs.getHashKeys( tripTimes ) ) + "'";
				String q = "select stops_on_trip_frequency._uniqtripID, trips_sum._tripID, trips_sum._tripHeadSign, routes._routeName, stops_on_trip_frequency._stopid , stops_on_trip_frequency._stopseq, st_distance( stop_geo._stopLoc , st_pointFromText( 'POINT( "+lat_to+" "+lon_to+")' ) )*111 as distd, ST_X( stop_geo._stopLoc ) as xlat, ST_Y( stop_geo._stopLoc ) as ylon from stops_on_trip_frequency, stop_geo, trips_sum, routes where stops_on_trip_frequency._uniqtripID=trips_sum._uniqtripID and routes._routeID=trips_sum._routeID and stops_on_trip_frequency._stopid=stop_geo._stopid and trips_sum._tripID in ("+trip_list+") order by trips_sum._tripID, distd;";
				
				Best.log( " q2 : " + q );	
				
				String[][] allStopsFromTrips = DBHandle.execQuery( q );
				if( allStopsFromTrips != null )
				{ 
					Best.log( " allStopsFromTrips : size : " + allStopsFromTrips.length );	
					int max_check = 0;	
					
					for( int cnt = 0; cnt < allStopsFromTrips.length; cnt++ )
					{
						Best.log( " allStopsFromTrips : cnt : " + cnt );	
						String utripID = allStopsFromTrips[ cnt ][ 0 ];
						String tripID = allStopsFromTrips[ cnt ][ 1 ];
						String tripSign = allStopsFromTrips[ cnt ][ 2 ];
						String routeName = allStopsFromTrips[ cnt ][ 3 ];
						String stopID = allStopsFromTrips[ cnt ][ 4 ];
						int stopSeq = Integer.parseInt( allStopsFromTrips[ cnt ][ 5 ] );
						double stopDistanceFromDestination = Double.parseDouble( allStopsFromTrips[ cnt ][ 6 ] );
						String stopLat = allStopsFromTrips[ cnt ][ 7 ];
						String stopLon = allStopsFromTrips[ cnt ][ 8 ];
						
						Best.log( " allStopsFromTrips : " + utripID + " -- " + tripID );	
						Hashtable doneTrips = new Hashtable();
						
						if( !doneTrips.containsKey( utripID ) )
						{
							if( singleUniqueTrips.containsKey( utripID ) )
							{
								Best.log( " -- " );	
								Vector vTripInfo = ( Vector ) singleUniqueTrips.get( utripID ); 
								int _storedStopSeq = Integer.parseInt( ( String ) vTripInfo.elementAt( M_STOP_INFO_SEQ_INDEX ) );
								if( _storedStopSeq < stopSeq )
								{ 
									if( stopDistanceFromDestination <= M_WALING_DISTANCE_KM )
									{ 
										Vector _tmp = ( Vector )tripTimes.get( tripID );
										long stored_dept_time = Long.parseLong( (String) _tmp.elementAt( 0 ) );
										int stored_freq = Integer.parseInt( (String) _tmp.elementAt( 1 ) );
										
										double stop_arr_time = stored_dept_time + ( stored_freq * stopSeq );
										
										//echo("calc time: ${tripID} start: ".$tripTimes[ $tripID ][ 'start_time' ]." - freq: ".$tripTimes[ $tripID ][ 'stop_freq' ]." - endseq: $stopSeq - startseq: ".$start_stop_dec[$this->_M_I_STOP_SEQ]." = ".$start_stop_dec['stoptime']." | $stop_arr_time \n");
										
										//echo("Adding Trip: ".$singleUniqueTrips[ $utripID ][ 'stop_start' ][$this->_M_I_STOP_ID]." -> ${tripID} -> ${stopID} ( ${stopDistanceFromDestination})\n");								
										doneTrips.put( utripID, true );
										
										workingTrips = insertWorkingTrips( workingTrips, calc_createWorkingRoute( tripID, utripID, tripSign, routeName, vTripInfo, init_dec( stopID, stopLat, stopLon, Integer.toString( stopSeq ), Double.toString( stop_arr_time ), Double.toString( stopDistanceFromDestination ) ), new Vector() ) );
									}
								}
							}	
						}
					}
				}
			}		
		}
		
		return( workingTrips );
	}
	
	public static Hashtable getAllNearByBustStops(double latFrom,double lonFrom)
	{
		double swlat = ( latFrom - GEO_DELTA );
		double swlon = ( lonFrom - GEO_DELTA );
		double nelat = ( latFrom + GEO_DELTA );
		double nelon = ( lonFrom + GEO_DELTA );
		
		String q = "select _stopID,st_distance( stop_geo._stopLoc , st_pointFromText( 'POINT( "+latFrom+" "+lonFrom+" )' ) )*111 as distd, ST_X( stop_geo._stopLoc ) as xlat, ST_Y( stop_geo._stopLoc ) as ylon from stop_geo where ST_Contains( GeomFromText( 'Polygon(("+swlat+" "+swlon+","+nelat+" "+swlon+","+nelat+" "+nelon+","+swlat+" "+nelon+","+swlat+" "+swlon+"))' ), _stopLoc ) order by distd;";
		
		Best.log( " getAllNearByBustStops : query" + q );
		
		String[][] allStops = DBHandle.execQuery( q );
		
		if( allStops != null )
		{
			Best.log( " getAllNearByBustStops : query_res_length " + allStops.length );
			int len = allStops.length;
			Hashtable _allStopNearPointHash = new Hashtable( len );
			for( int i = 0; i < len; i++ )
			{
				String _stopID = allStops[ i ][ 0 ];
				double _distanceFromPoint = Double.parseDouble( allStops[ i ][ 1 ] );
				double _stopLat = Double.parseDouble( allStops[ i ][ 2 ] );
				double _stopLon = Double.parseDouble( allStops[ i ][ 3 ] );
				Best.log( " getAllNearByBustStops : stop at "+ i +" :: " + _stopID + " -- " + " -- " + _distanceFromPoint + " -- " + _stopLat + " -- " + _stopLon );
				Vector _stopNearPointInfo = new Vector( 3 );
				_stopNearPointInfo.insertElementAt( Double.toString( _distanceFromPoint ), M_STOP_NEAR_POINT_DISTANCE_INDEX );
				_stopNearPointInfo.insertElementAt( Double.toString( _stopLat ), M_STOP_NEAR_POINT_LAT_INDEX );
				_stopNearPointInfo.insertElementAt( Double.toString( _stopLon ), M_STOP_NEAR_POINT_LON_INDEX );
				
				_allStopNearPointHash.put( _stopID, _stopNearPointInfo );
			}
			return( _allStopNearPointHash );
		}
		return( null );
	}
	
	public static String[][] getAllPassingRoutesFromStops(String[] stops,double lat,double lon,String srcDay)
	{
		if( stops.length > 0 )
		{
			String stopsList = "'" + android.text.TextUtils.join( "','", stops  ) + "'";
			String q = "select stops_on_trip_frequency._uniqtripID,stops_on_trip_frequency._stopid, stops_on_trip_frequency._stopseq, trips_sum._tripid, trips_sum._start, trips_sum._start_next_day, trips_sum._freq, st_distance( stop_geo._stopLoc , st_pointFromText( 'POINT( "+lat+" "+lon+")' ) )*111 as distd from stop_geo, stops_on_trip_frequency, schedule_rules, trips_sum where trips_sum._uniqtripID = stops_on_trip_frequency._uniqtripID and trips_sum._serviceid = schedule_rules._serviceid and stops_on_trip_frequency._stopid=stop_geo._stopid and schedule_rules._serviceday='"+srcDay+"' and schedule_rules._startperioddate<=date( 'now' ) and date( 'now' )<=schedule_rules._endperioddate and stops_on_trip_frequency._stopid in ("+stopsList+") order by distd;";
			
			Best.log( " getAllPassingRoutesFromStops : query : " + q );
			
			return( DBHandle.execQuery( q ) );
		}	
		
		return( null );
	}
	
	public static Vector init_dec(String stopID,String stopLat,String stopLon,String stopSeq,String stop_arr_time,String stopDistanceFromDestination )
	{
		Vector v = new Vector();
		v.insertElementAt( stopID, M_STOP_INFO_ID_INDEX ); 
		v.insertElementAt( stopLat, M_STOP_INFO_LAT_INDEX ); 
		v.insertElementAt( stopLon, M_STOP_INFO_LON_INDEX ); 
		v.insertElementAt( stopSeq, M_STOP_INFO_SEQ_INDEX ); 
		v.insertElementAt( stop_arr_time, M_STOP_INFO_ARR_TIME_INDEX ); 
		v.insertElementAt( stopDistanceFromDestination, M_STOP_INFO_ARR_DISTANCE_INDEX );
		return( v );
	}
	
	public static Vector insertWorkingTrips(Vector wtrips,Vector wt)
	{
		int wlen = wtrips.size();
		Vector ret = new Vector(); 
			
		if( wlen > 0 )
		{
			boolean b_done_insert = false;
			Vector infoVector = ( Vector )wt.elementAt( 2 );
			double suppliedDistance = Double.parseDouble( ( String )infoVector.elementAt( 2 ) );
			for( int i=0; i < wlen; i++ )
			{
				Vector singleTrip = ( Vector )wtrips.elementAt( i );
				Vector innerVector = ( Vector )singleTrip.elementAt( 2 );
				if( suppliedDistance < Double.parseDouble( ( String )innerVector.elementAt( 2 ) ) )
					{ ret.add( wt ); b_done_insert = true; }
				
				ret.add( singleTrip );
			}
			
			if(!b_done_insert) { ret.add( wt ); }
		}
		else
			ret.add( wt );
		
		return( ret );
	}
	
	public static Vector calc_createWorkingRoute(String tripID,String uniqueTripID,String tripSign,String routeName,Vector start_stop_dec,Vector end_stop_dec,Vector next_trip_hops)
	{
		double s = Double.parseDouble( (String)start_stop_dec.elementAt( M_STOP_INFO_ARR_TIME_INDEX ) );
		double e = Double.parseDouble( (String)end_stop_dec.elementAt( M_STOP_INFO_ARR_TIME_INDEX ) );
			
		int strartBusStopNo = Integer.parseInt( (String)start_stop_dec.elementAt( M_STOP_INFO_SEQ_INDEX ) );
		int lastBusStopNo = Integer.parseInt( (String)end_stop_dec.elementAt( M_STOP_INFO_SEQ_INDEX ) );
		
		double trip_time = e - s;
		double trip_dist = getTripDistance( uniqueTripID, strartBusStopNo+"", lastBusStopNo+"" );
					
					
		Vector v = new Vector();
		v.add( tripID );	
		v.add( uniqueTripID );	
		v.add( init_trip_dec( trip_time+"", "0", trip_dist+"", tripSign, routeName ) );	
		v.add( start_stop_dec );	
		v.add( end_stop_dec );	
		v.add( next_trip_hops );	
		
		return( v );			
	}
	
	public static double getTripDistance(String tripID,String start_seq,String end_seq)
	{
		try{
			String q = "select asText(stop_geo._stoploc) from stops_on_trip_frequency inner join stop_geo on (stop_geo._stopid=stops_on_trip_frequency._stopid) where _uniqtripID='"+tripID+"' and _stopseq>="+start_seq+" and _stopseq<="+end_seq;
			String[][] resultSet = DBHandle.execQuery( q );
			if( resultSet != null )
			{
				String lastPoint = "";
				String distanceSelectClause = "";
				for( int cnt = 0; cnt < resultSet.length; cnt++ )
				{
					if( cnt > 0 )
					{
						distanceSelectClause = ( ( distanceSelectClause.length() > 0 )?( "+" ):( "" ) )+"distance( GeomFromText( '"+lastPoint+"' ), GeomFromText( '"+resultSet[ cnt ][ 0 ]+"' ) )";
					}	
					distanceSelectClause += "*111"; 
					lastPoint = resultSet[ cnt ][ 0 ];
				}
				
				q = "select "+distanceSelectClause;
				resultSet = DBHandle.execQuery( q );
				
				Best.log( " getTripDistance q: " + q );
				
				if( resultSet != null )
				{
					Best.log( " ANSWER : " + resultSet[ 0 ][ 0 ] );
					return( Double.parseDouble( resultSet[ 0 ][ 0 ] ) ); 
				}	
			}
		}catch(Exception e){}	
		return( 0 );
	}
	
	public static Vector init_trip_dec(String tripTime,String tripFare,String tripDist,String tripSign,String routeName)
	{
		Vector v = new Vector();
		v.add( tripTime );v.add( tripFare );v.add( tripDist );v.add( tripSign );v.add( routeName );
		return( v );
	}
	
	//------------DIRECT ROUTING ---------
	public static String[][] getDirectRoutes(String fromStopID, String toStopID)
	{
		String _currDay = Funcs.getCurrentDay();
	
		System.out.println("-----CurrentDAy--*--"+_currDay);
		String q = "select distinct stops_on_trip._tripID, stops_on_trip._stopSeq, trips_freq._start, trips_freq._end, trips_freq._end_next_day, trips_freq._freq, routes._routeLongName , routes._routeName from stops_on_trip, trips_freq, trips, routes, schedule_rules where stops_on_trip._stopID='"+fromStopID +"' and stops_on_trip._tripID = trips_freq._tripID and stops_on_trip._tripID = trips._tripID and schedule_rules._serviceID = trips._serviceID and routes._routeID = trips._routeID and schedule_rules._serviceDay='"+ _currDay +"' order by stops_on_trip._tripID asc, _start asc;" ;
		
		Best.log( q );
		String[][] resultSet = DBHandle.execQuery( q );
		Best.log("===resultSet.Length=="+resultSet.length);
		
		
		if(resultSet != null )
		{
			Hashtable _passingFromStopsTrips = new Hashtable();
			String _allTripId = "";
			int _passedTrips = 0;
			for( int cnt = 0; cnt < resultSet.length; cnt++ )
			{
				Best.log("====resultSet Count==="+ cnt);
				String _tripId = resultSet[ cnt ][ 0 ];
				int _seq = Integer.parseInt( resultSet[ cnt ][ 1 ] );
				String _startTime = resultSet[ cnt ][ 2 ];
				String _endTime = resultSet[ cnt ][ 3 ];
				int _freq = Integer.parseInt( resultSet[ cnt ][ 5 ] );
				String _routeLongName = resultSet[ cnt ][ 6 ];
				String _routeName = resultSet[ cnt ][ 7 ];
				
				String _currTime = Funcs.getCurrentTime( true );
				Best.log("===_tripId=="+_tripId+"==_seq=="+_seq+"==_startTime=="+_startTime+"==_freq=="+_freq+"==_routeLongName=="+_routeLongName+"==_routeName=="+_routeName);
				
				long _startTimeInSecs = Funcs.timeToSeconds( _startTime );
				
				long _endTimeInSecs = Funcs.timeToSeconds( _endTime );	

				long _currTimeInSecs = Funcs.timeToSeconds( _currTime );
				Best.log("===_currTime=="+_currTime);
				Best.log("===_currTimeInSecs=="+_currTimeInSecs);
				
				int _trip_travel_time = (_freq * _seq);
				long _trip_start_esti_beg = (_currTimeInSecs - _trip_travel_time) - 900;
				long _trip_start_esti_end = (_currTimeInSecs - _trip_travel_time) + 900;
				
				Best.log("===_startTimeInSecs=="+_startTimeInSecs);
				Best.log("_trip_start_esti_beg="+_trip_start_esti_beg);
				
				Best.log("===_endTimeInSecs=="+_endTimeInSecs);
				Best.log("_trip_start_esti_end="+_trip_start_esti_end);
						
				 if( ( (_startTimeInSecs <= _trip_start_esti_beg) && (_endTimeInSecs <= _trip_start_esti_beg) ) || ( (_startTimeInSecs <= _trip_start_esti_end) && (_endTimeInSecs <= _trip_start_esti_end) ) )
				{	
					double _stopTime = ( _startTimeInSecs + ( _seq *  _freq ) );
					Best.log("===_stopTime=="+_stopTime);	
					
					//if( ( ( _stopTime - 900 ) < 57600 ) && ( ( _stopTime + 900 ) > 57600 ) )	
					//{
					Vector _trip_info = new Vector( 6 );
					_trip_info.add( _tripId );
					_trip_info.add( Integer.toString( _seq ) );
					_trip_info.add( Integer.toString( _freq ) );
					_trip_info.add( _startTime );
					_trip_info.add( _routeLongName );
					_trip_info.add( _routeName );
					
					Best.log( "STORRING : " + _tripId + " -> " + _seq + " -- " + _freq + " -- " + _startTime + " -- " + _tripId );
					if(_passingFromStopsTrips.containsKey( _tripId ) )
					{
						System.out.println("KEY EXIST");
						Vector _tripVec = (Vector)_passingFromStopsTrips.get(_tripId);
						_tripVec.add(_trip_info);
					}
					else
					{
						Vector _tripVec  = new Vector();
						System.out.println("KEY DOESNT EXIST");
						_tripVec.add(_trip_info);
						_passingFromStopsTrips.put( _tripId, _tripVec );
					}
				
					if(((Vector)(_passingFromStopsTrips.get(_tripId))).size() == 1)
						_allTripId += ( ( _allTripId.length() > 0 )?( "," ):( "" ) ) + "'"+_tripId+"'";
						
				}
			}
			Best.log("=======_passingFromStopsTrips Size===="+_passingFromStopsTrips.size());
			Best.log("=======_allTripId===="+_allTripId);
			if( _passingFromStopsTrips.size() > 0 && _allTripId.length() > 0 )
			{
				
				String q2 = "select distinct _tripID, _stopSeq from stops_on_trip where _tripID in ("+_allTripId+") and _stopID = '"+toStopID+"';";
				Best.log( "2nd query======="+q2 );
				
				resultSet = DBHandle.execQuery( q2 );
				
				if( resultSet != null )
				{
					String _TripIds = "";
					Hashtable _toStopSeqStore = new Hashtable();
					
					for( int c = 0; c < resultSet.length; c++ )
					{
						String tripId = resultSet[ c ][ 0 ];
						int _toStopSeq = Integer.parseInt( resultSet[ c ][ 1 ] );
						Best.log( "Q2 : "+tripId+" :: "+_toStopSeq );
						if( _passingFromStopsTrips.containsKey( tripId ) )
						{
							Vector _tripPassingFrom = ( Vector )_passingFromStopsTrips.get( tripId );
							int _fromStopSeq = Integer.parseInt((String)( ( Vector )_tripPassingFrom.elementAt( 0 )).elementAt(1) );
							
							Best.log( "SQS : "+ _fromStopSeq + " :: " + _toStopSeq );
							
							if( _fromStopSeq < _toStopSeq )
							{
								_passedTrips++;
								_TripIds += ( ( _TripIds.length() > 0 )?( "," ):( "" ) )+ tripId;
								_toStopSeqStore.put( tripId, Integer.toString( _toStopSeq ) );
							}	
						}
						else
							Best.log( "NOT STORED : "+tripId );
					}
					
					Best.log( "_TripIds : "+ _TripIds );
					if( _TripIds.length() > 0 )
					{
						String[] _trips = _TripIds.split( "," );
						String _tripIds = "";
						int k = 0;
						for( k = 0 ;k < _trips.length -1; k++)
						{
							_tripIds += "'"+_trips[k]+"'"+",";
						}
						_tripIds += "'"+_trips[k]+"'";
						
						String[][] _ret = null;
						if( _passedTrips > 0 )
							_ret = new String[ _passedTrips ][ 6 ];
						
						String query = "select ST_X(_stopLoc) , ST_Y(_stopLoc) , _tripID , _stopSeq ,stops_on_trip._stopID from stops_on_trip ,stop_geo where stops_on_trip._stopID = stop_geo._stopID and stops_on_trip._tripID in ("+_tripIds+") order by _tripID , _stopSeq ;";
						Best.log("query="+query);
						
						String[][] tripInfoResult = DBHandle.execQuery( query );

						ArrayList<String> distanceList = new ArrayList<String>();
						for(int i =0 ; i < tripInfoResult.length; i++)
						{
							ArrayList<String> stopGeoX = new ArrayList<String>();
							ArrayList<String> stopGeoY = new ArrayList<String>();
							if(tripInfoResult[i][4].equals(fromStopID))
							{
								for(int j = 0; j< tripInfoResult.length; j++,i++)
								{
									stopGeoX.add( tripInfoResult[i][0]);
									stopGeoY.add( tripInfoResult[i][1]);
									if(tripInfoResult[i][4].equals(toStopID))
										break ;
								}
								int distanceBetweenStops = 0;
								for(int index = 0; index <stopGeoX.size() - 1; index++ )
								{
									GeoPoint gp1 = new GeoPoint(Double.parseDouble(stopGeoX.get(index)) , Double.parseDouble(stopGeoY.get(index)) );
									GeoPoint gp2 = new GeoPoint(Double.parseDouble(stopGeoX.get(index + 1)) , Double.parseDouble(stopGeoY.get(index + 1)) );
									distanceBetweenStops += gp1.distanceTo( gp2 );
									Best.log("distanceBetweenStops ="+ distanceBetweenStops);
								}
								distanceList.add(Integer.toString(distanceBetweenStops));
							}
						}
						
						if( _ret != null )
						{
							for( int c = 0,_passeIndex=0; c < _trips.length; c++ )
							{
								String _trip_id = _trips[ c ];
								Best.log( "_trip_id: "+_trip_id);
								if( _passingFromStopsTrips.containsKey( _trip_id ) )
								{
									Vector _trip_vec = ( Vector ) _passingFromStopsTrips.get( _trip_id );
									for(int i =0; i < _trip_vec.size() ; i++)
									{
										System.out.println("TRIP_VEC_SIZE="+_trip_vec.size());
										if( _passeIndex < _passedTrips )
										{
											Vector _t = (Vector)(_trip_vec.elementAt(i));
											_ret[ _passeIndex ][ 0 ] = ( String )_t.elementAt( 0 ); //trip ID
											_ret[ _passeIndex ][ 1 ] = ( String )_t.elementAt( 5 ); //routeName
											_ret[ _passeIndex ][ 2 ] = ( String )_t.elementAt( 4 ); //head sign
											_ret[ _passeIndex ][ 3 ] = Integer.toString( Integer.parseInt( ( String ) _toStopSeqStore.get( _trip_id ) ) - Integer.parseInt( ( String ) _t.elementAt( 1 ) ) ); // number of stops
											//_ret[ c ][ 4 ] = ( String )_t.elementAt( 2 ); //departure time
											_ret[ _passeIndex ][ 4 ] = distanceList.get(c);
											_ret[ _passeIndex ][ 5 ] = ( String )_t.elementAt( 2 ); //freq
											
											//String[][] tripInfoResultSet = getTripInfo( ( String )_t.elementAt( 0 ));

											Best.log( "_x RET INFO : " + android.text.TextUtils.join( "','", _ret[ _passeIndex ] ) );
											_passeIndex++;
										}
									}

								}
								else
									Best.log( "_x else : " );
							}
						}
						return( _ret );
					}
				}
			}
		}
		else
			Best.log("getDirectRoutes:=====Resultset null=====");
		return( null );
	}
	
	public static String[][] getAllStops()
	{
		String _currDay = Funcs.getCurrentDay();
		String allStopsQuery = "select _stopID, _stopNameMarathi from stop_names group by _stopNameMarathi order by _stopNameMarathi asc limit 50;";
		
		System.out.println("allStops QUERY==:"+allStopsQuery);
		String[][] allStops = DBHandle.execQuery( allStopsQuery );
		return (allStops);
	}
	
	public static String[][] getAllTrips()
	{
		String _currDay = Funcs.getCurrentDay();
		String allTripsQuery = "select distinct routes._routeID, trips._tripID, _routeLongName ,_routeName  from routes, trips, schedule_rules where trips._routeID = routes._routeID and trips._serviceID = schedule_rules._serviceID and _serviceDay = '"+_currDay+"' group by routes._routeID order by routes._routeID asc limit 30;";
		
		System.out.println("allTrips QUERY==:"+allTripsQuery);
		String[][] allTrips = DBHandle.execQuery( allTripsQuery );
		return (allTrips);
	}
	//------------------------------------
	
	public static String[][] getNearByTripInfo(Double lat, Double lon)
	{
		String _currDay = Funcs.getCurrentDay();
		String nearByStopQuery = "select g._stopID from stop_geo g, stop_names n where g._stopID = n._stopID and Distance( _stopLoc, ST_GeomFromText('POINT(" + lat + " " + lon + ")') )*111 <= 0.5  limit 30 ";
		
		//String nearByPassingTrips = "select distinct _routeLongName from routes, trips tr where tr._routeID = routes._routeID and tr._tripID in (select _tripID from stops_on_trip where _stopID in ("+nearByStopQuery+"));";
		
		String nearByPassingTrips = "select distinct routes._routeID, trips._tripID, _routeLongName ,_routeName ,trips_freq._freq from routes, trips, schedule_rules, trips_freq where trips._routeID = routes._routeID and trips._tripID in (select _tripID from stops_on_trip where _stopID in ( select stop_geo._stopID from stop_geo where Distance( _stopLoc, ST_GeomFromText('POINT("+lat+" "+lon+")') )*111 <= 0.5 limit 30)) and trips_freq._tripID =  trips._tripID and trips._serviceID = schedule_rules._serviceID and _serviceDay = '"+_currDay+"' group by routes._routeID order by routes._routeID asc ;";
		
		System.out.println("QUERY==:"+nearByPassingTrips);
		//String[][] nearByTrips = DBHandle.execQuery( nearByPassingTrips );
		//String _tripIDS = "";
		// for(int i = 0; i<nearByTrips.length; i++)
		// {
			// _tripIDS += ( ( _tripIDS.length() > 0 )?( "," ):( "" ) ) + "'"+nearByTrips[i][1]+"'";
		// }
		// System.out.println("===_tripIDS=="+_tripIDS);
		// String tripWithStopNames = "select stop_names._stopID, stop_names._stopName  from stop_names ,stop_geo, stops_on_trip where stop_geo._stopID = stop_names._stopID and Distance( _stopLoc, ST_GeomFromText('POINT(" + lat + " " + lon + ")') )*111 <= 0.5 and stop_names._stopID = stops_on_trip._stopID and stops_on_trip._tripID in ("+_tripIDS+");";
		
		String[][] nearbyTrips_Name = DBHandle.execQuery( nearByPassingTrips );
		
		return (nearbyTrips_Name);
		
		
	}
}