package com.anast.lms.repository;

import com.anast.lms.generated.jooq.Tables;
import com.anast.lms.generated.jooq.tables.records.GroupRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.anast.lms.generated.jooq.Tables.*;
import static com.anast.lms.generated.jooq.tables.Group.GROUP;

@Repository
public class StudyRepository {

    private final DSLContext context;

    public StudyRepository(DSLContext context) {
        this.context = context;
    }

    public GroupRecord getGroup(String groupCode) {
        return context.selectFrom(Tables.GROUP).where(GROUP.CODE.eq(groupCode)).fetchAny();
    }

    public void getStudentCourses(GroupRecord group, int semester, Boolean searchActive) {

        Condition condition = DSL.trueCondition();
        condition = condition.and(COURSE.IS_TEMPLATE.eq(false));
        condition = condition.and(DISCIPLINE.SPECIALTY_CODE.eq(group.getSpecialtyCode()));
        condition = condition.and(DISCIPLINE.STAGE_CODE.eq(group.getStageCode()));
        condition = condition.and(DISCIPLINE.STUDY_FORM.eq(group.getStudyFormCode()));

        //если null, ищем все
        if(searchActive != null) {
            if(searchActive) {
                condition = condition.and(COURSE.START_DATE.le(LocalDate.now()));
                condition = condition.and(COURSE.END_DATE.ge(LocalDate.now()));
                condition = condition.and(DISCIPLINE.SEMESTER.eq((short) semester));
            } else {
                condition = condition.and(COURSE.END_DATE.lessThan(LocalDate.now()));
            }
        }

        Result result = context.selectFrom(COURSE
                    .leftJoin(DISCIPLINE).on(COURSE.DISCIPLINE_ID.eq(DISCIPLINE.ID))
                    .leftJoin(DISCIPLINE_DESCRIPTOR).on(DISCIPLINE.DISCIPLINE_DESCR_ID.eq(DISCIPLINE_DESCRIPTOR.ID)))
                .where(condition)
                .fetch();
    }
}
