package net.stepbooks.interfaces.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PairedReadCollectionInfoDto {

    private String collectionId;
    private String username;
    private String coverImgId;
    private String coverImgUrl;
    private String detailImgId;
    private String detailImgUrl;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
