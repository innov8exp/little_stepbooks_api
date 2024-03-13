package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
