package net.stepbooks.domain.pairedread.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.pairedread.enums.PairedReadType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PAIRED_READ")
public class PairedRead extends BaseEntity {

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
