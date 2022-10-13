/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

