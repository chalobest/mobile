#!/bin/bash
#
# Liscense: GPLv3
#
#

if [ ${#} -eq 3 ] ; then
	FILE_IN_STOPS="${1}"
	FILE_OUT_STOPS_NAMES="${2}"
	FILE_OUT_STOPS_GEO="${3}"
	
	cat "${FILE_IN_STOPS}" | grep -v "stop_id" | gawk -v sq="${SQLITE_DB_IMPORT_SEP}" -F ',' '{
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
				# _stopID, _stopName, _stopNameMarathi, _stopRoad, _stopArea
				print stop_id sq stop_name sq stop_name_marathi sq stop_road sq stop_area;
			}
		}
	}' | grep -v "^$" >"${FILE_OUT_STOPS_NAMES}"
	
	cat "${FILE_IN_STOPS}" | grep -v "stop_id" | gawk -v sq="${SQLITE_DB_IMPORT_SEP}" -F ',' '{
		stop_id=$1; stop_lat=$3; stop_long=$4;
		if( (length(stop_id)>0) && (length(stop_lat)>0) && (length(stop_long)>0) )
		{
			print stop_id sq stop_lat sq stop_long;
		}
	}' | grep -v "^$" >"${FILE_OUT_STOPS_GEO}"
fi
