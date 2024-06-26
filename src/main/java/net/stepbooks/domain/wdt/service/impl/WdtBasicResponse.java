package net.stepbooks.domain.wdt.service.impl;

import lombok.Data;

@Data
public class WdtBasicResponse {

    public static final int SUCCESS = 0;

    private int code;
    private String message;

    public boolean success() {
        return code == SUCCESS;
    }
}
