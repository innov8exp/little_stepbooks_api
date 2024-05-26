package net.stepbooks.domain.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("STEP_USER_CHECKIN_LOG")
public class UserCheckinLog extends BaseEntity {

    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkinDate;

    private Integer continuesDay;

}
