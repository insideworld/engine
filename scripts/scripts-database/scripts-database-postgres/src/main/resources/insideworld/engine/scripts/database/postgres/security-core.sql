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