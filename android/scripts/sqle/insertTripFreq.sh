#!/bin/bash
#
# Liscense: GPLv3
#
# routes = route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color
# route_id,route_short_name,route_long_name,route_type --for MUMBAI routes
# route_id,service_id,trip_id --for MUMBAI trips

if [ ${#} -eq 2 ] ; then
	FILE_IN_FREQ="${1}"
	FILE_OUT_FREQ="${2}"
	
	echo "delete from trips_freq;" >"${FILE_OUT_FREQ}"
	cat "${FILE_IN_FREQ}" | grep -v "trip_id" | gawk -v sq="'" -F ',' '{ 
		trip_id = $1; start_time = $2; end_time = $3; freq = $4;
				
		split(end_time, parts_end_time, ":");			
			
		if( parts_end_time[1] >= 24 )
			{ end_time_next_day = "true"; end_time = (parts_end_time[1] - 24) ":" parts_end_time[2] ":" parts_end_time[3]; }
		else { end_time_next_day = "false"; }
			
		print "insert into trips_freq (_tripID, _start, _end, _freq,_end_next_day) values (" sq trip_id sq "," sq start_time sq "," sq end_time sq "," sq freq sq "," end_time_next_day ",);"
	}' >>"${FILE_OUT_FREQ}"
fi