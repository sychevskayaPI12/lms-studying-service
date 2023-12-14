create table lms_study.class_type (
    code varchar not null unique
);

insert into lms_study.class_type VALUES ('lect'), ('pract'), ('lab'), ('cons'), ('exam');


CREATE SEQUENCE lms_study.class_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.class (
    id integer not null unique default nextval('lms_study.class_id_seq'::regclass),
    discipline_id integer not null,
    group_code varchar,
    class_type_code varchar not null,
    constraint class_id primary key (id),
    constraint discipline_id_fk foreign key (discipline_id) references lms_study.discipline(id),
    constraint group_code_fk foreign key (group_code) references lms_study.group(code),
    constraint class_type_fk foreign key (class_type_code) references lms_study.class_type(code)
);

CREATE SEQUENCE lms_study.static_schedule_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.static_schedule (
    id integer not null unique default nextval('lms_study.static_schedule_id_seq'::regclass),
    class_id integer not null,
    day_of_week smallint not null,
    is_even_week boolean,
    lesson_number smallint not null,
    classroom varchar default 'не назначена',
    is_actual boolean not null default true,

    constraint static_schedule_id primary key (id),
    constraint class_id_fk foreign key (class_id) references lms_study.class(id)
);