package net.stepbooks.application.dto.client;

import lombok.Data;

import java.util.List;

@Data
public class UserTagRefDto {

    private String id;
    private String userId;
    private List<String> tagIds;
}
