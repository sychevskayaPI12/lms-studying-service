CREATE SEQUENCE lms_study.resource_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table if not exists lms_study.module_resource (
    id integer not null unique default nextval('lms_study.resource_id_seq'::regclass),
    store_file_name varchar not null unique,
    display_file_name varchar not null,
    constraint module_resource_id primary key (id)
);

CREATE SEQUENCE lms_study.module_resource_link_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table if not exists lms_study.module_resource_link (
    id integer not null unique default nextval('lms_study.module_resource_link_id_seq'::regclass),
    module_id integer not null,
    resource_id integer not null,
    constraint module_resource_link_id primary key (id),
    constraint module_id_fk foreign key (module_id) references lms_study.module(id),
    constraint resource_id_fk foreign key (resource_id) references lms_study.module_resource(id)
)

