#!/bin/bash
#
# Liscense: GPLv3
#
#

source "env.sh"

if [ ${#} -eq 1 ] ; then
	M_FORCE_GTFS_REFRESH=${1}
else
	M_FORCE_GTFS_REFRESH=0
fi

if [ ! -d "${GTFS_INPUT}" ] ; then
	mkdir -p "${GTFS_INPUT}"
fi

if [ ! -d "${DATA_OUT}" ] ; then
	mkdir -p "${DATA_OUT}"
fi

pushd "${GTFS_INPUT}"

if [ ! -f gtfs.zip ] || [ ${M_FORCE_GTFS_REFRESH} -eq 1 ] ; then
	rm -f gtfs.zip
	wget -O gtfs.zip "${M_GTFS_FEED}"
	unzip gtfs.zip
	
	dos2unix "${FILE_IN_STOPS}"
	
	dos2unix "${FILE_IN_ROUTES}"
	dos2unix "${FILE_IN_TRIPS}"
	dos2unix "${FILE_IN_CALENDAR}"
	dos2unix "${FILE_IN_STOP_TIMES}"
fi

popd

echo -n "Info: Generating Stop Name Geo Entries ... "	
./sqle/insertStops.sh "${FILE_IN_STOPS}" "${FILE_OUT_STOPS_NAMES}" "${FILE_OUT_STOPS_GEO}"
echo "done"

echo -n "Info: Generating Trip Bus Freq Entries ... "	
./sqle/insertTripBusFreq.sh "${FILE_IN_FREQ}" "${FILE_OUT_FREQ}"
echo "done"

echo -n "Info: Generating Trip Route Entries ... "
./sqle/insertTripRoutes.sh "${FILE_IN_ROUTES}" "${FILE_OUT_ROUTES}" "${FILE_IN_TRIPS}" "${FILE_OUT_TRIPS}"
echo "done"

echo -n "Info: Generating Schedule Entries ... "	
./sqle/insertTripSchedules.sh "${FILE_IN_CALENDAR}" "${FILE_OUT_CALENDAR}"
echo "done"

echo -n "Info: Generating Stop Trip Sequence Entries ... "
./sqle/insertStopSeq.sh "${FILE_IN_STOP_TIMES}" "${FILE_OUT_STOP_SEQ}"
echo "done"

echo "---- -------- ----- ------ ----------- ------ -----"
