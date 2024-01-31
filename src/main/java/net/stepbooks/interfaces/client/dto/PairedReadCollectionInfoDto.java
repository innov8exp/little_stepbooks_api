package net.stepbooks.interfaces.client.dto;

import lombok.Data;

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
}
