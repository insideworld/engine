CREATE SCHEMA users;

create table users.roles
(
	id bigint not null
		constraint roles_pk
			primary key,
	name varchar not null,
	append_id bigint
		constraint roles_roles_id_fk
			references users.roles
);

create unique index roles_name_uindex
	on users.roles (name);

create table users.users
(
    id bigint not null
        constraint users_pk
            primary key,
	token varchar not null,
	role bigint not null
		constraint users_roles_id_fk
			references users.roles,
	name varchar not null
);

create unique index authentication_name_uindex
	on users.users (name);

create index authentication_token_uindex
	on users.users (token);

