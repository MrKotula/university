insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce92e2', 'OR-41');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce1234', 'GM-87');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce5325', 'XI-12');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce1111', 'TT-12');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce2356', 'YT-16');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce8906', 'LG-55');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce5775', 'GQ-22');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce2344', 'TH-13');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce2337', 'GN-33');
insert into schedule.groups (group_id, group_name) values ('3c01e6f1-762e-43b8-a6e1-7cf493ce9988', 'IT-18');

insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee0f22', 'math', 'course of Mathematics');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee1234', 'biology', 'course of Biology');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee5324', 'chemistry', 'course of Chemistry');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee6589', 'physics', 'course of Physics');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee8999', 'philosophy', 'course of Philosophy');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee0096', 'drawing', 'course of Drawing');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee1222', 'literature', 'course of Literature');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee7658', 'English', 'course of English');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee3356', 'geography', 'course of Geography');
insert into schedule.courses (course_id, course_name, course_description) values ('1d95bc79-a549-4d2c-aeb5-3f929aee0887', 'physical training', 'course of Physical training');

insert into schedule.students (student_id, group_id, first_name, last_name) values ('33c99439-aaf0-4ebd-a07a-bd0c550db4e1', '3c01e6f1-762e-43b8-a6e1-7cf493ce92e2', 'John', 'Doe');
insert into schedule.students (student_id, group_id, first_name, last_name) values ('33c99439-aaf0-4ebd-a07a-bd0c550d2311', '3c01e6f1-762e-43b8-a6e1-7cf493ce5325',  'Jane', 'Does');

insert into schedule.students_courses (student_id, course_id) values ('33c99439-aaf0-4ebd-a07a-bd0c550db4e1', '1d95bc79-a549-4d2c-aeb5-3f929aee0f22');
insert into schedule.students_courses (student_id, course_id) values ('33c99439-aaf0-4ebd-a07a-bd0c550db4e1', '1d95bc79-a549-4d2c-aeb5-3f929aee1234');
insert into schedule.students_courses (student_id, course_id) values ('33c99439-aaf0-4ebd-a07a-bd0c550d2311', '1d95bc79-a549-4d2c-aeb5-3f929aee1234');
