-- liquibase formatted sql

-- changeset miaskor:111
alter table telegram_client
    modify bamper_client_id bigint null, lock = none;

-- changeset miaskor:112
create unique index uindex_telegram_client_nick_name
    on telegram_client (nick_name);

