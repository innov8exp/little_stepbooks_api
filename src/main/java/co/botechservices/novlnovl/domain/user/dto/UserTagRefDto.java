package co.botechservices.novlnovl.domain.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserTagRefDto {

    private String id;
    private String userId;
    private List<String> tagIds;
}
