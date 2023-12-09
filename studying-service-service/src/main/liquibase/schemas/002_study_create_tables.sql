CREATE SEQUENCE lms_study.student_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.student (
    id integer not null default nextval('lms_study.student_id_seq'::regclass),
    login varchar not null unique,
    group_code varchar not null,
    constraint student_id_pk primary key (id),
    constraint group_code_fk foreign key (group_code) REFERENCES lms_study.group(code)
);

CREATE SEQUENCE lms_study.teacher_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.teacher (
    id integer not null default nextval('lms_study.teacher_id_seq'::regclass),
    login varchar not null unique,
    degree varchar,
    constraint teacher_id_pk primary key (id)
);

CREATE SEQUENCE lms_study.teacher_disc_link_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.teacher_discipline_link (
    id integer not null default nextval('lms_study.teacher_disc_link_id_seq'::regclass),
    teacher_id integer not null,
    discipline_id integer not null,
    constraint teacher_disc_link_id_pk primary key (id),
    constraint teacher_id_fk foreign key (teacher_id) references lms_study.teacher(id),
    constraint discipline_id_fk foreign key (discipline_id) references lms_study.discipline(id)
)