package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private String skuCode;
    private String skuName;
    private String salesPlatform;
    private String resources;
    private ProductNature productNature;
    private String description;
    private BigDecimal price;
    private String coverImgId;
    private String coverImgUrl;
    private String bookSetId;
    private String bookSetCode;
    private String bookSetName;
    private List<Media> medias;
    private ProductStatus status;
}
