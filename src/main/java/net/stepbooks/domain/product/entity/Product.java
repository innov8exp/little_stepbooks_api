package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_PRODUCT")
public class Product extends BaseEntity {

    private String skuNo;
    private String skuName;
    @TableField(jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class)
    private String[] salesPlatform;
    private Boolean hasInventory;
    private String description;
    private BigDecimal price;
    private String coverImgId;
    private String coverImgUrl;
    @TableField(jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class)
    private String[] resources;
}
