package com.stepbook.interfaces.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserTagRefDto {

    private String id;
    private String userId;
    private List<String> tagIds;
}
