package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.StoreType;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {

    private StoreType storeType;

    private Integer sortIndex;

    private String skuCode;
    private String skuName;
    private int salesPlatforms;
    private int materials;
    private ProductNature productNature;
    private String description;
    private BigDecimal price;
    private String coverImgId;
    private String coverImgUrl;
    private String videoId;
    private String videoUrl;
    private List<ProductMediaDto> medias;
    private ProductStatus status;
    private String[] classificationIds;

    //private String[] classificationNames;

    @Deprecated
    //使用physicalGoodsIds和virtualGoodsIds替代
    private List<String> bookIds;

    private String detailImgId;
    private String tags;
}
