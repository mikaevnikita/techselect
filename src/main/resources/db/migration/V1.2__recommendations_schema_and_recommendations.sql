SET SCHEMA 'techselect';

create table if not exists recommendation (
    id bigint not null,
    text text not null,
    primary key (id)
);

insert into recommendation (id, text)
values (1, 'Наличие фронтенд разработчиков в команде - ДА');

insert into recommendation (id, text)
values (2, 'Наличие фронтенд разработчиков в команде - НЕТ');

insert into recommendation (id, text)
values (3, 'Оцените опыт и способности работы вашей команды с реляционными СУБД  - Хороший или средний');

insert into recommendation (id, text)
values (4, 'Оцените опыт и способности работы вашей команды с реляционными СУБД  - Низкий или отсутствует');

insert into recommendation (id, text)
values (5, 'Микросервисы в облаке');

insert into recommendation (id, text)
values (6, 'Микросервисы не в облаке');

insert into recommendation (id, text)
values (7, 'Монолит не в облаке');

insert into recommendation (id, text)
values (8, 'Монолит в облаке');

insert into recommendation (id, text)
values (9, 'Должна ли система быть масштабируемой горизонтально - Да');

insert into recommendation (id, text)
values (10, 'Требуется ли функционал регистрации, авторизации и аутентификации - Да');

insert into recommendation (id, text)
values (11, 'Требуется ли аудит событий - Да');

insert into recommendation (id, text)
values (12, 'Синхронные интеграции');

insert into recommendation (id, text)
values (13, 'Множество интеграций - Camel');

insert into recommendation (id, text)
values (14, 'Асинхронные интеграции');

insert into recommendation (id, text)
values (15, 'Будет ли система являться корпоративным приложением с фиксированным числом пользователей - Нет');

insert into recommendation (id, text)
values (16, 'Должна ли система быть отказоустойчивой - Да');