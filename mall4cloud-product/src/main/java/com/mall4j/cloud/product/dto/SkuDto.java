package com.mall4j.cloud.product.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 10:34
 * @description mall
 */
@Data
public class SkuDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long skuId;

    private String attrs;

    private String imgUrl;

    @NotNull(message = "市场价不能为空")
    @Min(value = 0, message = "市场价最小为0")
    private Long marketPriceFee;

    @NotNull(message = "售价不能为空")
    @Min(value = 0, message = "售价最小为0")
    private Long priceFee;

    private String partyCode;

    private String modelId;

    private String skuName;

    private List<SpuSkuAttrValueDto> spuSkuAttrValues;

    private Integer stock;


}
