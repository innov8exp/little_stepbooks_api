package net.stepbooks.domain.bookset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("STEP_BOOK_SET")
public class BookSet extends BaseEntity {

    private String code;
    private String name;
    private String description;
}
