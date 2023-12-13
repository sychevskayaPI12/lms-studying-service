CREATE SEQUENCE lms_study.module_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.module (
    id integer not null unique default nextval('lms_study.module_id_seq'::regclass),
    course_id integer not null,
    title varchar not null,
    content varchar,
    module_order smallint not null,
    constraint module_id_pk primary key (id),
    constraint course_id_fk foreign key (course_id) references lms_study.course(id)
)
