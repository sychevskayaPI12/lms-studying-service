package com.anast.lms.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum RequestState {

    unprocessed("На рассмотрение", (short) 0),
    accepted("Приняты", (short) 1),
    declined("Отклонены", (short) 2)
    ;

    private String title;
    private Short value;

    RequestState(String title, Short value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public Short getValue() {
        return value;
    }

    private static final Map<Short, RequestState> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(RequestState::getValue, v->v));

    public static RequestState getEnum(Short val) {
        return lookup.get(val);
    }
}
