package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PHYSICAL_GOODS")
public class PhysicalGoodsEntity extends BaseEntity {

    private Integer sortIndex;
    private String name;
    private String description;
    private String coverId;
    private String coverUrl;
    private PublishStatus status;

    public String wdtSpuId() {
        return getId();
    }

    public String wdtSpuNo() {
        return AppConstants.WDT_SPU_NO_PREFIX + getId();
    }

    public String wdtSkuId() {
        return getId();
    }

    public String wdtSkuNo() {
        return AppConstants.WDT_SKU_NO_PREFIX + getId();
    }

}
