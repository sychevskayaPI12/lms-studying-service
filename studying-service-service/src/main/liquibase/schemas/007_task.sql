create table if not exists lms_study.task_type(
  code varchar not null unique,
  constraint task_type_code_pk primary key (code)
);

CREATE SEQUENCE lms_study.task_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table if not exists lms_study.task (
    id integer not null unique default nextval('lms_study.task_id_seq'::regclass),
    type_code varchar not null,
    title varchar,
    description varchar,
    deadline date,
    constraint task_id_pk primary key (id),
    constraint task_type_code_fk foreign key (type_code) references lms_study.task_type(code)
);

CREATE SEQUENCE lms_study.module_task_link_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table if not exists lms_study.module_task_link (
   id integer not null unique default nextval('lms_study.module_task_link_id_seq'::regclass),
   module_id integer not null,
   task_id integer not null,
   constraint module_task_link_id primary key (id),
   constraint module_id_fk foreign key (module_id) references lms_study.module(id),
   constraint task_id_fk foreign key (task_id) references lms_study.task(id)
);

CREATE SEQUENCE lms_study.task_resource_link_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table if not exists lms_study.task_resource_link (
   id integer not null unique default nextval('lms_study.task_resource_link_id_seq'::regclass),
   task_id integer not null,
   resource_id integer not null,
   constraint task_resource_link_id primary key (id),
   constraint task_id_fk foreign key (task_id) references lms_study.task(id),
   constraint resource_id_fk foreign key (resource_id) references lms_study.module_resource(id)
)

