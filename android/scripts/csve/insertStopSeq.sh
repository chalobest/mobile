#!/bin/bash
#
# Liscense: GPLv3
#
#trip_id,arrival_time,departure_time,stop_id,stop_sequence -- FOR MUMBAI

if [ ${#} -eq 2 ] ; then
	FILE_IN_STOP_TIMES="${1}"
	FILE_OUT_STOP_SEQ="${2}"
	
	cat "${FILE_IN_STOP_TIMES}" | grep -v "stop_id" | gawk -v sq="${SQLITE_DB_IMPORT_SEP}" -F ',' '{ 
		pickup_type = $7; drop_off_type = $8;
		if( ( ( pickup_type == "" ) &&( drop_off_type == "" ) ) || ( ( pickup_type == 0 ) && ( drop_off_type == 0 ) ) ) {
			trip_id=$1; arri_time=$2; dept_time=$3; stop_id=$4; stop_seq=$5;
			
			if( arri_time == "" )
				arri_time = "00:00:00";
			if( dept_time == "" )
				dept_time = "00:00:00";
			
			stop_seq = stop_seq;
			
			split(dept_time, parts_dept_time, ":"); split(arri_time, parts_arri_time, ":");
			
			if( parts_dept_time[1] >= 24 )
				{ dept_time_next_day = "true"; dept_time = (parts_dept_time[1] - 24) ":" parts_dept_time[2] ":" parts_dept_time[3] }
			else { dept_time_next_day = "false"; }
			if( parts_arri_time[1] >= 24 )
				{ arri_time_next_day = "true"; arri_time = (parts_arri_time[1] - 24) ":" parts_arri_time[2] ":" parts_arri_time[3] }
			else { arri_time_next_day = "false"; }
			
			print trip_id sq stop_id sq stop_seq ;
		}
	}' | grep -v "^$" >"${FILE_OUT_STOP_SEQ}"
fi