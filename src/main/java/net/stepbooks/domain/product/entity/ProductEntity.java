package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.enums.*;
import net.stepbooks.infrastructure.model.BaseEntity;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_PRODUCT")
public class ProductEntity extends BaseEntity {

    private String skuNo;
    private String skuName;
    private PackageType packageType;
    private ProductType productType;
    private ChargeType chargeType;
    private SalesPlatform salesPlatform;
    private String productDescription;
    private Double price;
    private String productImgLink;
    private ProductObjectType productObjectType;
    @TableField(jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class)
    private String[] resources;
}
