package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class PairedReadCollectionInfoDto {

    private String collectionId;
    private String userId;
    private String coverImgId;
    private String coverImgUrl;
    private String name;
    private String description;
}
