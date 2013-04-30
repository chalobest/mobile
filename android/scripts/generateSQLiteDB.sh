#!/bin/bash
#
# Liscense: GPLv3
#
#

source "env.sh"

if [ ! -f ${ZIPGEN_JAR} ] ; then
	echo "Error: Please build the zip gen utility"
else
	if [ -f ${DATA_SQLITE_DB_CORE} ] ; then
		rm -vf ${DATA_SQLITE_DB_CORE}
	fi

	echo -n "Info: Generating sqlite database ... "	

	# Create the DB
	cat ${DATA_SQLITE_DB_CREATE} | sqlite3 ${DATA_SQLITE_DB_CORE}

	# Import the data
	echo .separator \"${SQLITE_DB_IMPORT_SEP}\" >${TMP_WORK_FILE}
	echo .import \"${FILE_OUT_STOPS_NAMES}\" stop_names >>${TMP_WORK_FILE}
	echo .import \"${FILE_OUT_STOPS_GEO}\" stop_geo >>${TMP_WORK_FILE}
	
	echo .import \"${FILE_OUT_ROUTES}\" routes >>${TMP_WORK_FILE}
	echo .import \"${FILE_OUT_TRIPS}\" trips >>${TMP_WORK_FILE}
	echo .import \"${FILE_OUT_CALENDAR}\" schedule_rules >>${TMP_WORK_FILE}
	echo .import \"${FILE_OUT_STOP_SEQ}\" stops_on_trip >>${TMP_WORK_FILE}

	cat ${TMP_WORK_FILE} | sqlite3 ${DATA_SQLITE_DB_CORE}
	rm -f ${TMP_WORK_FILE}

	_SDLITE_DB_=${DATA_SQLITE_DB_CORE}
	_ZIPGEN_JAR_=${ZIPGEN_JAR}
	
	if [ $(uname -a | grep -i -c cygwin) -ge 1 ] ; then
		_SDLITE_DB_=$(cygpath -w ${_SDLITE_DB_})
		_ZIPGEN_JAR_=$(cygpath -w ${_ZIPGEN_JAR_})
	fi
	
	echo "Info: Generating sqlite database zip:"	
	
	java -jar ${_ZIPGEN_JAR_} ${_SDLITE_DB_}
	
	if [ -f "${DATA_SQLITE_DB_CORE}.gz" ] ; then
		mv "${DATA_SQLITE_DB_CORE}.gz" "${DATA_SQLITE_ANDROID_RES}"
		echo && echo "done"
	else
		echo "Error: Sqlite DB zip was not created!"
	fi
fi


