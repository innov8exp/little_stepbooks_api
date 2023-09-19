package co.botechservices.novlnovl.domain.history.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import lombok.*;

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
