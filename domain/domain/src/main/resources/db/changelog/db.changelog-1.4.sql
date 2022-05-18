-- liquibase formatted sql

-- changeset miaskor:129
alter table car
    add column year VARCHAR(4) not null,
    lock = shared;

-- changeset miaskor:130
alter table spare_part
    rename column photo to photo_path,lock = shared;

-- changeset miaskor:131
alter table spare_part
    modify photo_path varchar(100) not null,lock = shared;

-- changeset miaskor:132
alter table car
    add column store_house_id BIGINT not null, lock = shared;

-- changeset miaskor:133
alter table car
    add constraint store_house_id_fk
        foreign key (store_house_id) references store_house(id), lock = shared;


