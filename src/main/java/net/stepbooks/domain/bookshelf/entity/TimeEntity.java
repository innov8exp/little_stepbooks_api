package net.stepbooks.domain.bookshelf.entity;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeEntity extends BaseEntity {
    private Long duration;
    private String userId;
}
