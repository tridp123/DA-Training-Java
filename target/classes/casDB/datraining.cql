create KEYSPACE IF NOT EXISTS datraining
with replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 1
};

use datraining;

CREATE TABLE IF NOT EXISTS location (
	location_id uuid,
	country varchar,
	city varchar,
	created_at timestamp,
	modified_at timestamp,
	PRIMARY KEY (location_id)
);

CREATE TABLE IF NOT EXISTS time (
	time_id uuid,
	month int,
	quarter int,
	year int,
	created_at timestamp,
	modified_at timestamp,
	PRIMARY KEY (time_id)
);

CREATE TABLE IF NOT EXISTS product (
	product_id uuid,
	item int,
	class varchar,
	inventory varchar,
	created_at timestamp,
	modified_at timestamp,
	PRIMARY KEY (product_id)
);

CREATE TABLE IF NOT EXISTS sales ( 
	product_id uuid,
	time_id uuid,
	location_id uuid,
	dollars int,
	created_at timestamp,
	modified_at timestamp,
	PRIMARY KEY (product_id,time_id,location_id)
);
//
SELECT *  FROM datraining.location;

