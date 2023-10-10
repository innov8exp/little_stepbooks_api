package net.stepbooks.domain.bookshelf.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
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
