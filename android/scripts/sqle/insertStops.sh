#!/bin/bash
#
# Liscense: GPLv3
#
#

if [ ${#} -eq 3 ] ; then
	FILE_IN_STOPS="${1}"
	FILE_OUT_STOPS_NAMES="${2}"
	FILE_OUT_STOPS_GEO="${3}"
	
	echo "delete from stop_names;" >"${FILE_OUT_STOPS_NAMES}"
	cat "${FILE_IN_STOPS}" | grep -v "stop_id" | gawk -v sq="'" -F ',' '{
		stop_id=$1; stop_name=$2; stop_dec=$5;
		
		if(length(stop_id)>0)
		{
			sub( sq, sq""sq, stop_name );
			if( split( stop_dec, stop_dec_parts, ":" ) == 4 )
			{
				stop_name_marathi = stop_dec_parts[2];
				sub( " Road", "", stop_name_marathi ); 
				stop_road = stop_dec_parts[3];
				sub( " Area", "", stop_road ); 
				stop_area = stop_dec_parts[4];
				print "insert into stop_names (_stopID, _stopName, _stopNameMarathi, _stopRoad, _stopArea) values (" sq stop_id sq "," sq stop_name sq "," sq stop_name_marathi sq "," sq stop_road sq "," sq stop_area sq");"
			}
		}
	}' | grep -v "^$" >>"${FILE_OUT_STOPS_NAMES}"
	
	echo "delete from stop_geo;" >"${FILE_OUT_STOPS_GEO}"
	cat "${FILE_IN_STOPS}" | grep -v "stop_id" | gawk -v sq="'" -F ',' '{
		stop_id=$1; stop_lat=$3; stop_long=$4;
		if( (length(stop_id)>0) && (length(stop_lat)>0) && (length(stop_long)>0) )
		{
			print "insert into stop_geo (_stopID, _stopLat, _stopLon) values (" sq stop_id sq "," stop_lat "," stop_long ");"
		}
	}' | grep -v "^$" >>"${FILE_OUT_STOPS_GEO}"
fi