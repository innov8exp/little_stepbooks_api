package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.pairedread.enums.CollectionStatus;

@Data
public class PairedReadCollectionDto {
    private String coverImgId;
    private String coverImgUrl;
    private String detailImgId;
    private String detailImgUrl;
    private String name;
    private String description;
    private CollectionStatus status;
}
