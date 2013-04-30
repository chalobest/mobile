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
	
	echo "delete from bus_max_freq;" >"${FILE_OUT_FREQ}"
	cat "${FILE_IN_FREQ}" | grep -v "trip_id" | gawk -v sq="'" -F ',' '{
		trip_id = $1; start_time = $2; end_time = $3; headways = $4;
		if( length(alltrips[trip_id]) > 0 )
			alltrips[trip_id] = ((alltrips[trip_id] + headways) / 2); # hack: ouch average out the headways!
		else
			alltrips[trip_id] = headways;
	} END {
		for( tid in alltrips)
			print "insert into bus_max_freq (_tripID, _freq) values (" sq tid sq "," int(alltrips[tid]) ");"
	}' >>"${FILE_OUT_FREQ}"
fi