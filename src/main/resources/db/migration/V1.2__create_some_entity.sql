-- Step #2: Creating table techselect.some_entity

create table if not exists techselect.some_entity
(
    id bigint not null,
    data varchar(128) not null,
    primary key (id)
);