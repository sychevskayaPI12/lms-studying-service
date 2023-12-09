package com.anast.lms.repository;

import com.anast.lms.generated.jooq.Tables;
import com.anast.lms.generated.jooq.tables.records.StudentRecord;
import com.anast.lms.generated.jooq.tables.records.TeacherRecord;
import com.anast.lms.model.StudentProfileInfo;
import com.anast.lms.model.TeacherProfileInfo;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.anast.lms.generated.jooq.Tables.STUDENT;
import static com.anast.lms.generated.jooq.Tables.TEACHER;

@Repository
public class ProfileRepository {

    private final DSLContext context;

    public ProfileRepository(DSLContext context) {
        this.context = context;
    }

    public StudentProfileInfo getStudentInfo(String login) {
        return context.selectFrom(STUDENT).where(STUDENT.LOGIN.eq(login))
                .fetchAny(this::mapStudentRecord);
    }

    public TeacherProfileInfo getTeacherInfo(String login) {
        return context.selectFrom(TEACHER).where(TEACHER.LOGIN.eq(login))
                .fetchAny(this::mapTeacherInfo);
    }

    private StudentProfileInfo mapStudentRecord(StudentRecord record) {
        return new StudentProfileInfo(record.getGroupCode());
    }
    private TeacherProfileInfo mapTeacherInfo(TeacherRecord record) {
        return new TeacherProfileInfo(record.getDegree());
    }
}
