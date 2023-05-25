CREATE SCHEMA IF NOT EXISTS schedule;

CREATE TABLE schedule.groups
(
    group_id character(36) NOT NULL,
    group_name character varying(10) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
);

CREATE TABLE IF NOT EXISTS schedule.students
(
    student_id character(36) NOT NULL,
    group_id character(36),
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (student_id),
    CONSTRAINT group_id FOREIGN KEY (group_id)
        REFERENCES schedule.groups (group_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
    
CREATE TABLE IF NOT EXISTS schedule.courses
(
    course_id character(36) NOT NULL,
    course_name character varying(50) NOT NULL,
    course_description character varying,
    CONSTRAINT courses_pkey PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS schedule.students_courses
(
    student_id character(36) NOT NULL,
    course_id character(36) NOT NULL,
    CONSTRAINT students_courses_pkey PRIMARY KEY (student_id, course_id),
    CONSTRAINT course_id FOREIGN KEY (course_id) REFERENCES schedule.courses (course_id)
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT student_id FOREIGN KEY (student_id) REFERENCES schedule.students (student_id)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);
