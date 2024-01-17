CREATE SEQUENCE lms_study.registration_request_id_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483
    START WITH 1;

create table lms_study.registration_request (
    id integer not null unique default nextval('lms_study.registration_request_id_seq'::regclass),
    user_login varchar not null,
    accepted boolean,
    create_date timestamp not null,
    change_date timestamp,
    constraint registration_request_id_pk primary key (id)
);


