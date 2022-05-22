-- liquibase formatted sql

-- changeset miaskor:119
drop table telegram_client_store_house;

-- changeset miaskor:120
alter table store_house
    add column telegram_client_id BIGINT not null,
    lock = shared;

-- changeset miaskor:121
alter table store_house add constraint telegram_client_id_fk
    foreign key (telegram_client_id) references
    telegram_client(id),
    lock = shared;


-- changeset miaskor:122
create unique index store_house_name_telegram_client_id_idx
    on store_house(store_house_name, telegram_client_id);

-- changeset miaskor:123
drop index store_house_name_telegram_client_id_idx on store_house;

-- changeset miaskor:124
alter table store_house
    rename column telegram_client_id to telegram_chat_id, lock=shared;

-- changeset miaskor:125
alter table store_house drop constraint telegram_client_id_fk, lock=shared;

-- changeset miaskor:126
create unique index telegram_client_chat_id_idx
    on telegram_client(chat_id);

-- changeset miaskor:127
alter table store_house add constraint telegram_chat_id_fk
    foreign key (telegram_chat_id) references
        telegram_client(chat_id), lock = shared;

-- changeset miaskor:128
create unique index store_house_name_telegram_chat_id_idx
    on store_house(store_house_name, telegram_chat_id);
