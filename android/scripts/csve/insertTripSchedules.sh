#!/bin/bash
#
# Liscense: GPLv3
#
#service_id,monday,tuesday,wednesday,thursday,friday,saturday,sunday,start_date,end_date -- FOR MUMBAI

if [ ${#} -eq 2 ] ; then
	FILE_IN_CALENDAR="${1}"
	FILE_OUT_CALENDAR="${2}"
	
	cat "${FILE_IN_CALENDAR}" | grep -v "^service_id" | gawk -v sq="${SQLITE_DB_IMPORT_SEP}" -F ',' '{ 
		if(NF==10)
		{
			service_id=$1; service_date_start=$9; service_date_end=$10; service_day="";
			
			for( ix=2; ix<=8; ix++ )
			{
				if( (ix==2) && ($ix=="1") ) { service_day="Mon"; }
				else if( (ix==3) && ($ix=="1") ) { service_day="Tue"; }
				else if( (ix==4) && ($ix=="1") ) { service_day="Wed"; }
				else if( (ix==5) && ($ix=="1") ) { service_day="Thu"; }
				else if( (ix==6) && ($ix=="1") ) { service_day="Fri"; }
				else if( (ix==7) && ($ix=="1") ) { service_day="Sat"; }
				else if( (ix==8) && ($ix=="1") ) { service_day="Sun"; }
				else { service_day=""; }
				
				if(service_day != "")
				{
					print service_id sq service_day sq service_date_start sq service_date_end;			
				}
			}
		}
	}' | grep -v "^$" >"${FILE_OUT_CALENDAR}"
fi