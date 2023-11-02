package net.stepbooks.infrastructure.external.dto;

import lombok.Data;

@Data
public class SmsFailedResponse {
    private Integer respCode;
    private String respDesc;
}
