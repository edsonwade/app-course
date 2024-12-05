create table if not exists COURSES
(
    id     varchar primary key not null,
    name   varchar             not null,
    length int                 not null,
    url    varchar             not null,
    notes  varchar
);