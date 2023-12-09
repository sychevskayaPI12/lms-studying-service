alter table lms_study.group add column stage_code varchar not null default 'bac';
alter table lms_study.group add constraint stage_code_fk foreign key (stage_code)
    references lms_study.stage(code);