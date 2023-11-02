package net.stepbooks.application.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookAuthResDto {
    private Data data;

    @lombok.Data
    public static class Data {
        @JsonProperty("app_id")
        private String appId;
        private String type;
        private String application;
        @JsonProperty("data_access_expires_at")
        private long dataAccessExpiresAt;
        @JsonProperty("expires_at")
        private long expiresAt;
        @JsonProperty("is_valid")
        private boolean isValid;
        @JsonProperty("issued_at")
        private long issuedAt;
        private Metadata metadata;
        private String[] scopes;
        @JsonProperty("user_id")
        private String userId;
    }

    @lombok.Data
    public static class Metadata {
        @JsonProperty("auth_type")
        private String authType;
        private String sso;
    }

}


