package net.stepbooks.interfaces.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentNotifyDto {

    private String id;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("resource_type")
    private String resourceType;
    @JsonProperty("event_type")
    private String eventType;
    private String summary;
    private Resource resource;

    public static class Resource {
        @JsonProperty("original_type")
        private String originalType;
        private String algorithm;
        private String ciphertext;
        @JsonProperty("associated_data")
        private String associatedData;
        private String nonce;
    }
}
