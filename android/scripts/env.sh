#!/bin/bash
#
# Liscense: GPLv3
#
# NOTE: Please set the correct working base
#

export M_PROCESS_FOR="Mumbai"
export M_GTFS_FEED='http://gtfs.chalobest.in/gtfs_0.9.5.zip'
export SQLITE_DB_IMPORT_SEP="~"

if [ $(uname -a | grep -i -c cygwin) -ge 1 ] ; then
	export M_BASE="/cygdrive/d/dev/transportation"
else
	export M_BASE="/var/working/transportation"
fi

M_DB_ID="gt${M_PROCESS_FOR}"

GTFS_INPUT="${M_BASE}/GTFS/${M_PROCESS_FOR}"
DATA_OUT="${M_BASE}/data/${M_PROCESS_FOR}"

TMP_WORK_FILE="${DATA_OUT}/__tmp"

DATA_SQLITE_DB_CORE="${DATA_OUT}/android-db.sqlite"
DATA_SQLITE_DB_CREATE="${M_BASE}/android/db/create-sqlite-android.sql"
DATA_SQLITE_ANDROID_RES="${M_BASE}/android/mobile/res/raw/chalobest.zip"

ZIPGEN_JAR="${M_BASE}/zipgen/ZipGen/bin/zipgen.jar"

FILE_DB_CREATE="${M_BASE}/web/db/create-template.txt"

FILE_IN_FREQ="${GTFS_INPUT}/frequencies.txt"
FILE_OUT_FREQ="${DATA_OUT}/frequencies.txt"

FILE_IN_STOPS="${GTFS_INPUT}/stops.txt"
FILE_IN_STOP_TIMES="${GTFS_INPUT}/stop_times.txt"
FILE_OUT_STOPS_NAMES="${DATA_OUT}/stop_names.txt"
FILE_OUT_STOPS_GEO="${DATA_OUT}/stop_geo.txt"

FILE_IN_STOP_TIMES="${GTFS_INPUT}/stop_times.txt"
FILE_OUT_STOP_SEQ="${DATA_OUT}/stop_seq.txt"

FILE_IN_ROUTES="${GTFS_INPUT}/routes.txt"
FILE_OUT_ROUTES="${DATA_OUT}/routes.txt"

FILE_IN_TRIPS="${GTFS_INPUT}/trips.txt"
FILE_OUT_TRIPS="${DATA_OUT}/trips.txt"

FILE_IN_CALENDAR="${GTFS_INPUT}/calendar.txt"
FILE_OUT_CALENDAR="${DATA_OUT}/trip_schedules.txt"
