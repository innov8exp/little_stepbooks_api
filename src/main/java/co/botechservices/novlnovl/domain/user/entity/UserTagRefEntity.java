package co.botechservices.novlnovl.domain.user.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("NVOL_USER_TAG_REF")
public class UserTagRefEntity extends BaseEntity {
    private String userId;
    private String tagId;
}
