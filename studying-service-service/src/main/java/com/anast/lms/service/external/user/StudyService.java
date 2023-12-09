package com.anast.lms.service.external.user;

import com.anast.lms.generated.jooq.tables.records.GroupRecord;
import com.anast.lms.repository.StudyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

@Service
public class StudyService {

    private final StudyRepository repository;

    public StudyService(StudyRepository repository) {
        this.repository = repository;
    }

    public int calcStudentCourse(String groupCode) {
        GroupRecord groupRecord = repository.getGroup(groupCode);
        short year = groupRecord.getEntryYear();
        int currentYear = LocalDate.now().getYear();
        return currentYear - year + getAdditionalSemCoef();
    }

    private int getAdditionalSemCoef() {
        int currSemNum = getCurrentSemester();
        return currSemNum == 1 ? 1 : 0;
    }

    private int getCurrentSemester() {
        LocalDate currDate = LocalDate.now();
        if(currDate.isAfter(LocalDate.of(currDate.getYear(), Month.AUGUST, 1))
                && currDate.isBefore(LocalDate.of(currDate.getYear(), Month.DECEMBER, 31)))  {
            return 1;
        } else {
            return 2;
        }
    }
}
