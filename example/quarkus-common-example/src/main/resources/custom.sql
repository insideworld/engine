CREATE SCHEMA engine;
create table engine.properties
(
    "key"   varchar not null
        constraint properties_pk
            primary key,
    "value" varchar
);
create unique index properties_key_uindex
    on engine.properties ("key");
create table engine.sql_queries
(
    id    varchar not null
        constraint sql_queries_pk
            primary key,
    query varchar not null,
    input varchar
);
create unique index sql_queries_id_uindex
    on engine.sql_queries (id);


CREATE SCHEMA test;

create table test.some_data
(
    "id"    bigint not null
        constraint some_data_pk primary key,
    "value" varchar,
    "date"  date
);

CREATE SCHEMA security;

create table security.roles
(
    id bigint not null
        constraint roles_pk
            primary key,
    name varchar not null,
    parent_id bigint
        constraint roles_roles_id_fk
            references security.roles
);


create unique index roles_name_uindex
    on security.roles (name);

create table security.users
(
    id bigint not null
        constraint users_pk
            primary key,
    role bigint not null
        constraint users_roles_id_fk
            references security.roles,
    name varchar not null
);

create unique index authentication_name_uindex
    on security.users (name);

create table security.token
(
    user_id bigint not null
        primary key
        constraint token_user_id_fk
            references security.users,
    token  varchar
);
create index token_token_index
    on security.token (token);

insert into security.roles values (1, 'system', null);
insert into security.users values (1, 1, 'system');
insert into security.token values (1, 'token')