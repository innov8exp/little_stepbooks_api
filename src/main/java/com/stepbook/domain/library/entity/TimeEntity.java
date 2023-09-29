package com.stepbook.domain.library.entity;

import com.stepbook.infrastructure.model.BaseEntity;
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
