CREATE SCHEMA transactions;

create table transactions.some_nested_entity
(
    id          serial  not null
        constraint some_nested_entity_pk
            primary key,
    one        bigint,
    two        bigint
);

create table transactions.some_entity
(
    id          serial  not null
        constraint some_entity_pk
            primary key,
    some_nested_entity_id         bigint
        constraint some_entity_some_nested_entity_fk
            references transactions.some_nested_entity,
    "value"        varchar
);

insert into transactions.some_nested_entity
values (default, 1, 2);
insert into transactions.some_entity
values (default, 1, 'Hello!');
