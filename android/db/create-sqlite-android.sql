CREATE TABLE stops_on_trip (
	_tripID varchar(30) not null,
	_stopID varchar(7) not null,
	_stopSeq integer NOT NULL,
	PRIMARY KEY(_tripID, _stopID, _stopSeq)
);

CREATE TABLE trips (
	_tripID varchar(30) NOT NULL,
	_routeID varchar(4) not null,
	_serviceID varchar(15) NOT NULL,
	_tripHeadSign varchar(17) not null,
	PRIMARY KEY(_tripID)
);



CREATE TABLE routes (
	_routeID varchar(4) not null,
	_routeName varchar(5) NOT NULL,
	_routeLongName varchar(80) NOT NULL,
	PRIMARY KEY(_routeID)
);

CREATE TABLE schedule_rules (
	_serviceID varchar(15) NOT NULL,
	_serviceDay varchar(3) not null,
	_startPeriodDate DATE not null,
	_endPeriodDate DATE not null,
	PRIMARY KEY(_serviceID, _serviceDay)
);

CREATE TABLE stop_names (
	_stopID varchar(7) PRIMARY KEY not null,
	_stopName varchar(50) not null,
	_stopNameMarathi varchar(80) ,
	_stopRoad varchar(50) ,
	_stopArea varchar(50) 
);

CREATE TABLE stop_geo (
	_stopID varchar(7) PRIMARY KEY not null,
	_stopLat number NOT NULL,
	_stopLon number NOT NULL
);



CREATE INDEX ix_stop_names_stopName ON stop_names ( _stopName );
CREATE INDEX ix_stops_on_trip_tripID ON stops_on_trip (_tripID);
CREATE INDEX ix_stops_on_trip_stopID ON stops_on_trip (_stopID);
CREATE INDEX ix_stops_on_trip_stopSeq ON stops_on_trip (_stopSeq);

CREATE INDEX ix_trips_routeID ON trips (_routeID);
CREATE INDEX ix_trips_serviceID ON trips (_serviceID);
CREATE INDEX ix_trips_tripHeadSign ON trips (_tripHeadSign);


CREATE INDEX ix_routes_routeName ON routes (_routeName);

CREATE INDEX ix_schedule_rules_serviceID ON schedule_rules (_serviceID);
CREATE INDEX ix_schedule_rules_serviceDay ON schedule_rules (_serviceDay);
CREATE INDEX ix_schedule_rules_startPeriodDate ON schedule_rules (_startPeriodDate);
CREATE INDEX ix_schedule_rules_endPeriodDate ON schedule_rules (_endPeriodDate);