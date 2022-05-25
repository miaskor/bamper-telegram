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

-- changeset miaskor:134
rename table spare_part to auto_part

-- changeset miaskor:135
alter table auto_part
 modify column photo_path varchar(100) null

-- changeset miaskor:136
alter table auto_part
 add column auto_part_key varchar(50) not null, lock = shared;

-- changeset miaskor:137
alter table advertisement
     drop foreign key advertisement_ibfk_1,
     drop foreign key advertisement_ibfk_2,
     lock = shared;

-- changeset miaskor:138
alter table advertisement
     drop column description,
     drop column photo,
     drop column price,
     drop column quality,
     drop column currency,
     drop column part_number,
     drop column car_id,
     drop column car_part_id,
     lock = shared;

-- changeset miaskor:139
alter table advertisement
     add column auto_part_id bigint not null, lock=shared;

-- changeset miaskor:140
alter table advertisement
     add constraint auto_part_id_fk
         foreign key (auto_part_id) references auto_part(id) , lock=shared;

-- changeset miaskor:141
create unique index auto_part_key_unq_idx
    on auto_part(auto_part_key)


