#!/bin/bash
#
# Liscense: GPLv3
#
# routes = route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color
# route_id,route_short_name,route_long_name,route_type --for MUMBAI routes
# route_id,service_id,trip_id --for MUMBAI trips

if [ ${#} -eq 4 ] ; then
	FILE_IN_ROUTES="${1}"
	FILE_OUT_ROUTES="${2}"
	
	FILE_IN_TRIPS="${3}"
	FILE_OUT_TRIPS="${4}"
	
	echo "delete from routes;" >"${FILE_OUT_ROUTES}"
	cat "${FILE_IN_ROUTES}" | grep -v "route_id" | gawk -v sq="'" -F ',' '{ 
		route_id = $1; agency_id = $2; route_short_name = $3; route_long_name = $4; route_type = $5;
		
		sub( sq, sq""sq, route_short_name );
		sub( sq, sq""sq, route_long_name );
		sub( sq, sq""sq, route_type );
		
		if(route_type==0) { route_type="Tram"; } else if(route_type==1) { route_type="Subway"; } else if(route_type==2) { route_type="Rail"; } else if(route_type==3) { route_type="Bus"; } else if(route_type==4) { route_type="Ferry"; } else if(route_type==5) { route_type="Cable car"; } else if(route_type==6) { route_type="Gondola"; } else if(route_type==7) { route_type="Funicular"; } else { route_type=""; }
		
		if(route_type=="") { route_type="Bus"; }
		
		print "insert into routes (_routeID, _agencyID, _routeName, _routeLongName, _routeType) values (" sq route_id sq "," sq agency_id sq "," sq route_short_name sq "," sq route_long_name sq "," sq route_type sq ");"
	}' >>"${FILE_OUT_ROUTES}"
	
	echo "delete from trips;" >"${FILE_OUT_TRIPS}"
	cat "${FILE_IN_TRIPS}" | grep -v "route_id" | gawk -v sq="'" -F ',' '{ 
		route_id = $1; service_id = $2; trip_id = $3; trip_sign = $4;
		
		sub( sq, sq""sq, trip_sign );
		
		print "insert into trips (_tripID, _routeID, _serviceID, _tripHeadSign) values (" sq trip_id sq "," sq route_id sq "," sq service_id sq "," sq trip_sign sq ");"
	}' >>"${FILE_OUT_TRIPS}"
fi