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
	
	cat "${FILE_IN_ROUTES}" | grep -v "route_id" | gawk -v sq="${SQLITE_DB_IMPORT_SEP}"  'BEGIN { FPAT = "([^,]*)|(\"[^\"]+\")" }{ 
		route_id = $1; agency_id = $2; route_short_name = $3; route_long_name = $4; route_type = $5;
	
		if(route_type==0) { route_type="Tram"; } else if(route_type==1) { route_type="Subway"; } else if(route_type==2) { route_type="Rail"; } else if(route_type==3) { route_type="Bus"; } else if(route_type==4) { route_type="Ferry"; } else if(route_type==5) { route_type="Cable car"; } else if(route_type==6) { route_type="Gondola"; } else if(route_type==7) { route_type="Funicular"; } else { route_type=""; }
		
		if(route_type=="") { route_type="Bus"; }
		
		print route_id sq  route_short_name sq route_long_name ;
	}' | grep -v "^$" | tr -d '"' >"${FILE_OUT_ROUTES}"
	
	# FPAT regex used to parse CSV data where a comma is in the field; further strip quotes from result as that is not part of the  field data.
	cat "${FILE_IN_TRIPS}" | grep -v "route_id" | gawk -v sq="${SQLITE_DB_IMPORT_SEP}" 'BEGIN { FPAT = "([^,]*)|(\"[^\"]+\")" } { 
		route_id = $1; service_id = $2; trip_id = $3; trip_sign = $4;
		
		print trip_id sq route_id sq service_id sq trip_sign;
	}' | grep -v "^$" | tr -d '"' >"${FILE_OUT_TRIPS}"
fi