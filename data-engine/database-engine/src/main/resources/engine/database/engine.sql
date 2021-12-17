CREATE SCHEMA engine;

create table engine.properties
(
	key varchar not null
		constraint properties_pk
			primary key,
	value varchar
);

create unique index properties_key_uindex
	on engine.properties (key);

create table engine.sql_queries
(
	id varchar not null
		constraint sql_queries_pk
			primary key,
	query varchar not null,
	input varchar
);

create unique index sql_queries_id_uindex
	on engine.sql_queries (id);

