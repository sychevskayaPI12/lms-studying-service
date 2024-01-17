create table lms_study.faculty_position (
    code varchar not null unique,
    title varchar not null
);

insert into lms_study.faculty_position VALUES
('assistant', 'Ассистент'),
('teacher', 'Преподаватель'),
('senior_teacher', 'Старший преподаватель'),
('assistant_professor', 'Доцент'),
('professor', 'Профессор'),
('head_of_department', 'Заведующий кафедрой');

CREATE SEQUENCE lms_study.department_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;


create table lms_study.department (
  id integer not null default nextval('lms_study.department_id_seq'::regclass),
  abbreviation varchar not null,
  short_title varchar not null,
  full_title varchar not null,
  constraint department_id primary key (id)
);

CREATE SEQUENCE lms_study.teacher_department_position_link_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

CREATE table lms_study.teacher_department_position_link (
  id integer not null unique default nextval('lms_study.teacher_department_position_link_id_seq'::regclass),
  login varchar not null,
  department_id integer not null,
  position_code varchar not null,

  constraint teacher_department_position_link_id primary key (id),
  constraint teacher_department_position_link_login foreign key (login) references lms_study.teacher(login),
  constraint teacher_department_position_link_department_id foreign key (department_id) references lms_study.department(id),
  constraint teacher_department_position_link_pos_code foreign key (position_code) references lms_study.faculty_position(code)
);


