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