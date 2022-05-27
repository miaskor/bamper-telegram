-- liquibase formatted sql

-- changeset miaskor:141
alter table worker_store_house
    drop constraint worker_store_house_ibfk_1,
    lock = shared;

-- changeset miaskor:142
alter table worker_store_house
    add constraint worker_store_house_telegram_chat_id_fk
        foreign key (worker_telegram_client_id) references telegram_client (chat_id),
    lock = shared;

-- changeset miaskor:143
create unique index worker_store_house_id_chat_id_unq_idx
    on worker_store_house(worker_telegram_client_id, store_house_id);
