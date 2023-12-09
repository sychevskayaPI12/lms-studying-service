CREATE SEQUENCE lms_study.course_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;


create table lms_study.course (
    id integer not null default nextval('lms_study.course_id_seq'::regclass),
    discipline_id integer not null,
    start_date date not null,
    end_date date not null,
    is_template boolean not null default false,

    constraint id_pk primary key (id),
    constraint discipline_id_fk foreign key (discipline_id) references lms_study.discipline(id)
)