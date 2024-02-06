package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

@Data
public class PairedReadCollectionDto {
    private String coverImgId;
    private String coverImgUrl;
    private String detailImgId;
    private String detailImgUrl;
    private String name;
    private String description;
}
