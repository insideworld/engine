CREATE SCHEMA transactions;

create table transactions.some_nested_entity
(
    id  serial not null
        constraint some_nested_entity_pk
            primary key,
    one bigint,
    two bigint
);

create table transactions.some_entity
(
    id                    serial not null
        constraint some_entity_pk
            primary key,
    some_nested_entity_id bigint
        constraint some_entity_some_nested_entity_fk
            references transactions.some_nested_entity,
    "value"               varchar
);

insert into transactions.some_nested_entity
values (default, 1, 2);
insert into transactions.some_entity
values (default, 1, 'Hello!');

CREATE SCHEMA entities;

create table entities.many_to_one
(
    id      serial not null
        constraint many_to_one_pk
            primary key,
    "value" varchar
);

create table entities."primary"
(
    id             serial not null
        constraint primary_pk
            primary key,
    many_to_one_id bigint
        constraint primary_many_to_one_fk
            references entities.many_to_one (id),
    "value"        varchar
);

create table entities.one_to_many
(
    id         serial not null
        constraint one_to_many_pk
            primary key,
    primary_id bigint
        constraint many_to_one_
            references entities."primary" (id),
    "value"    varchar
);

create table entities.one_to_one
(
    id      serial not null
        primary key
        constraint one_to_one_primary_fk
            references entities."primary" (id),
    "value" varchar
);
