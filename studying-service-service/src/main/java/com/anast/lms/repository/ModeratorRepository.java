package com.anast.lms.repository;

import com.anast.lms.generated.jooq.tables.records.RegistrationRequestRecord;
import com.anast.lms.model.RequestState;
import com.anast.lms.model.profile.RegistrationRequest;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.anast.lms.generated.jooq.Tables.REGISTRATION_REQUEST;

@Repository
public class ModeratorRepository {

    private final DSLContext context;

    public ModeratorRepository(DSLContext context) {
        this.context = context;
    }

    public List<RegistrationRequest> getRegistrationRequests(Short state) {
        return context.selectFrom(REGISTRATION_REQUEST)
                .where(REGISTRATION_REQUEST.STATE.eq(state))
                .orderBy(REGISTRATION_REQUEST.CREATE_DATE)
                .fetch(this::mapRegistrationRequest);
    }

    public RegistrationRequest getRegistrationRequest(Integer requestId) {
        return context.selectFrom(REGISTRATION_REQUEST)
                .where(REGISTRATION_REQUEST.ID.eq(requestId))
                .fetchAny(this::mapRegistrationRequest);
    }

    public void updateRegistrationRequest(Integer requestId, RequestState state) {
        context.update(REGISTRATION_REQUEST)
                .set(REGISTRATION_REQUEST.STATE, state.getValue())
                .set(REGISTRATION_REQUEST.CHANGE_DATE, LocalDateTime.now())
                .where(REGISTRATION_REQUEST.ID.eq(requestId))
                .execute();
    }

    public void createRegistrationRequest(String login, String fullName) {
        context.insertInto(REGISTRATION_REQUEST)
                .set(REGISTRATION_REQUEST.USER_LOGIN, login)
                .set(REGISTRATION_REQUEST.DISPLAY_NAME, fullName)
                .set(REGISTRATION_REQUEST.CREATE_DATE, LocalDateTime.now())
                .execute();
    }

    private RegistrationRequest mapRegistrationRequest(RegistrationRequestRecord r) {
        return new RegistrationRequest(
                r.getId(),
                r.getUserLogin(),
                r.getDisplayName(),
                r.getState(),
                r.getCreateDate(),
                r.getChangeDate()
        );
    }
}
