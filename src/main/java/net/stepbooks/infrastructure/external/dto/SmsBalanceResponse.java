package net.stepbooks.infrastructure.external.dto;

import lombok.Data;

@Data
public class SmsBalanceResponse {
    private int respCode;
    private String respDesc;
    private Data data;

    @lombok.Data
    static class Data {
        private int balance;
    }

}

