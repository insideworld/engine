insert into security.roles values (1, 'system', null);
insert into security.users values (1, 1, 'system');
insert into security.token values (1, 'token');

CREATE SCHEMA test;

create table test.some_data
(
    "id"    bigint not null
        constraint some_data_pk primary key,
    "value" varchar,
    "date"  date
);

insert into test.some_data values (1, 'qwe', null);