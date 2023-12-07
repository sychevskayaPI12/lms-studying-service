create table lms_study.specialty (
  code varchar(10) not null unique,
  title varchar not null,
  constraint specialty_code_pk primary key (code)
);

create table lms_study.study_form (
   code varchar(10) not null unique,
   title varchar not null,
   description varchar not null,
   constraint study_form_code_pk primary key (code)
);

create table lms_study.stage (
    code varchar not null unique,
    title varchar not null,
    constraint stage_code_pk primary key (code)
);

CREATE SEQUENCE lms_study.group_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.group (
    id integer not null unique default nextval('lms_study.group_id_seq'::regclass),
    code varchar not null unique,
    entry_year smallint not null,
    specialty_code varchar not null,
    study_form_code varchar not null,
    constraint group_id_pk primary key (id),
    constraint specialty_code_fk FOREIGN KEY(specialty_code) REFERENCES lms_study.specialty(code),
    constraint study_form_code_fk FOREIGN KEY(study_form_code) REFERENCES lms_study.study_form(code)
);

insert into lms_study.study_form values
('full_time', 'о', 'Очная'),
('distant', 'з', 'Заочная'),
('part_time', 'о/з', 'Очно-заочная');

insert into lms_study.stage values
('bac', 'Бакалавриат'),
('mag', 'Магистратура'),
('postgrad', 'Аспирантура'),
('spec', 'Специалитет');



CREATE SEQUENCE lms_study.discipline_descr_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.discipline_descriptor (
    id integer not null unique default nextval('lms_study.discipline_descr_id_seq'::regclass),
    title varchar not null,
    description varchar,
    constraint discipline_descr_id_pk primary key (id)
);

CREATE SEQUENCE lms_study.discipline_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;


create table lms_study.discipline (
    id integer not null unique default nextval('lms_study.discipline_id_seq'::regclass),
    discipline_descr_id integer not null,
    semester smallint not null,
    is_examination boolean not null default false,
    specialty_code varchar not null,
    stage_code varchar not null default 'bac',
    study_form varchar not null default 'full_time',

    constraint discipline_id_pk primary key (id),
    constraint discipline_descr_id_fk FOREIGN KEY(discipline_descr_id) REFERENCES lms_study.discipline_descriptor(id),
    constraint specialty_code_fk FOREIGN KEY(specialty_code) REFERENCES lms_study.specialty(code),
    constraint stage_code_fk FOREIGN KEY(stage_code) REFERENCES lms_study.stage(code),
    constraint study_form_code_fk FOREIGN KEY(study_form) REFERENCES lms_study.study_form(code)
);




