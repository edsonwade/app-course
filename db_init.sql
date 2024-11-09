create table if not exists COURSES
(
    id     varchar primary key not null,
    name   varchar             not null,
    length int                 not null,
    url    varchar             not null,
    notes  varchar
);

-- INSERT INTO COURSES (id, name, length, url, notes) VALUES ('1', 'Introduction to Programming', 120, 'http://example.com/course1', 'This is a beginner course.');
-- INSERT INTO COURSES (id, name, length, url, notes) VALUES ('2', 'Advanced Java', 150, 'http://example.com/course2', 'This course covers advanced topics in Java programming.');
