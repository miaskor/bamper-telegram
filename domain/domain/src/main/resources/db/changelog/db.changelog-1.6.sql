-- liquibase formatted sql

-- changeset miaskor:144
create unique index unq_bamper_client_login
    on bamper_client (login);
