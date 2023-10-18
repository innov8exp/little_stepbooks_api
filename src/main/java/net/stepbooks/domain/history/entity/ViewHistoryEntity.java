package net.stepbooks.domain.history.entity;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewHistoryEntity extends BaseEntity {
    private String bookId;
    // normal user and guest
    private String userId;
}
