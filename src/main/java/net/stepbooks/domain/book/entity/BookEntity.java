package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "STEP_BOOK")
public class BookEntity extends BaseEntity {
    private String bookName;
    private String author;
    private String bookImgLink;
    private String introduction;
    @TableField(jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class)
    private String[] keywords;
    private String status;
}
