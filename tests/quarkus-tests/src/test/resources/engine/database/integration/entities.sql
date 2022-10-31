/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
    one_id         bigint
        constraint main_one_id_fk
            references entities.one,
    two_id         bigint
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

