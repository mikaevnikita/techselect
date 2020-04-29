-- Author: Nikita Mikaev
-- Date: 2020-04-27

-- Step #1: Creating schema

create schema if not exists techselect;

SET SCHEMA 'techselect';

create table if not exists question_meta (
    id bigint not null,
    text varchar(512) not null,
    primary key (id)
);

create table if not exists answer_meta (
    id bigint not null,
    text varchar(512) not null ,
    question_meta_id bigint not null,
    primary key (id),
    foreign key (question_meta_id) references question_meta (id)
);

create table if not exists question (
    id bigint not null,
    session_id bigint not null,
    meta_id bigint not null,
    text varchar(512) not null,
    primary key (id),
    foreign key (meta_id) references question_meta (id),
    unique (session_id, meta_id)
);

create table if not exists answer (
    id bigint not null,
    session_id bigint not null,
    question_id bigint not null,
    question_meta_id bigint not null,
    answer_meta_id bigint not null,
    text varchar(512) not null,
    primary key (id),
    foreign key (answer_meta_id) references answer_meta (id),
    foreign key (question_meta_id) references question_meta (id),
    foreign key (question_id) references question (id),
    unique (session_id, question_meta_id, answer_meta_id, question_id)
);

