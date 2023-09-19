package co.botechservices.novlnovl.domain.library.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeEntity extends BaseEntity {
    private Long duration;
    private String userId;
}
