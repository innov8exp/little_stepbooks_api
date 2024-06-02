package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.domain.product.enums.ProductStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MProductQueryDto {
    private String skuCode;
    private String skuName;
    private String tag;
    private ProductStatus status;
}
