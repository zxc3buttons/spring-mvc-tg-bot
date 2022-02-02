create table if not exists wallet_changes
(
    id          bigint default nextval('tgusers_id_seq'::regclass) not null
        constraint tgusers_pkey
            primary key,
    money       bigint,
    chat_id     integer,
    date        date,
    category    varchar(30),
    change_type varchar(30),
    wallet_type varchar(30)
);

create table if not exists balance
(
    id           integer not null
        constraint balance_pk
            primary key,
    chat_id      bigint,
    wallet_type  varchar(30),
    money_amount bigint
);