package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.StoreType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MProductQueryDto {
    private StoreType storeType;
    private String skuCode;
    private String skuName;
    private String tag;
    private ProductStatus status;
}
