package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.domain.pairedread.enums.PairedReadType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PairedReadDto {
    private String collectionId;
    private String name;
    private String audioId;
    private String audioUrl;
    private String duration;
    private Integer sortIndex;

    private PairedReadType type; //AUDIO,VIDEO
    private String videoId;
    private String videoUrl;
    private String coverImgId;
    private String coverImgUrl;
}
