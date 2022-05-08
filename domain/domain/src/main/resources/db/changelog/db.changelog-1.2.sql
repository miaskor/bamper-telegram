-- liquibase formatted sql

-- changeset miaskor:113
alter table telegram_client
    modify chat_id bigint not null, lock = shared;

-- changeset miaskor:114
alter table worker_telegram
    drop foreign key worker_telegram_ibfk_1, lock = shared;
alter table worker_telegram
    drop foreign key worker_telegram_ibfk_2, lock = shared;

-- changeset miaskor:115
alter table worker_telegram
    rename index employer_telegram_client_id to employer_telegram_chat_id, lock = shared;
alter table worker_telegram
    rename index worker_telegram_client_id to worker_telegram_chat_id, lock = shared;

-- changeset miaskor:116
alter table worker_telegram
    rename column worker_telegram_client_id to worker_telegram_chat_id, lock = shared;
alter table worker_telegram
    rename column employer_telegram_client_id to employer_telegram_chat_id, lock = shared;

-- changeset miaskor:117
alter table telegram_client
    rename column nick_name to user_name, lock = shared;

-- changeset miaskor:118
create unique index employee_chat_id_employer_chat_id
    on worker_telegram( worker_telegram_chat_id, employer_telegram_chat_id);


