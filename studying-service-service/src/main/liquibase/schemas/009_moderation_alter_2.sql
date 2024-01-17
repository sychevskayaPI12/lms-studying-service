alter table lms_study.registration_request drop column if exists accepted;

alter table lms_study.registration_request add column if not exists
    state smallint not null default 0;