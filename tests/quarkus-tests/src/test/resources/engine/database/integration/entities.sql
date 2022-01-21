CREATE SCHEMA entities;



create table entities.one
(
    id          serial  not null
        constraint one_pk
            primary key,
    name        varchar not null,
    description varchar
);

create table entities.two
(
    id          serial  not null
        constraint two_pk
            primary key,
    name        varchar not null,
    description varchar
);

create table entities.main
(
    id          serial  not null
        constraint main_pk
            primary key,
    name        varchar not null,
    description varchar,
    one         bigint
        constraint main_one_id_fk
            references entities.one,
    two         bigint
        constraint main_two_id_fk
            references entities.two
);

create table entities.arrays
(
    id      serial  not null
        constraint arrays_pk
            primary key,
    main_id bigint
        constraint array_main_id_fk
            references entities.main,
    message varchar not null
);

insert into entities.one
values (1, 'One', 'Test one');
insert into entities.two
values (1, 'Two', 'Test two');
insert into entities.main
values (1, 'Main', 'Test main', 1, 1);
insert into entities.arrays
values (1, 1, 'Test array');
insert into entities.arrays
values (2, 1, 'Test array1');
insert into entities.arrays
values (3, 1, 'Test array2');
insert into entities.arrays
values (4, 1, 'Test array3');
insert into entities.arrays
values (5, 1, 'Test array4');

CREATE SCHEMA generated;

create table generated.child
(
    id serial not null
        constraint child_pk
            primary key,
    some varchar
);

insert into generated.child values (1, 'One');
insert into generated.child values (2, 'Two');
insert into generated.child values (3, 'Three');

create table generated.top
(
    id serial not null
        constraint top_pk
            primary key,
    message varchar,
    child_id bigint
        constraint top_main_id_fk
            references generated.child
);

