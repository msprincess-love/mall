package com.mall4j.cloud.product.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 15:56
 * @description mall
 */
@Data
public class SpuDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long spuId;

    private Long brandId;

    @NotNull(message = "关联的平台分类id不能为空")
    private Long categoryId;

    @NotNull(message = "关联的店铺分类id不能为空")
    private Long shopCategoryId;

    private Integer hasSKuImg;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotBlank(message = "商品介绍主图不能为空")
    private String mainImgUrl;

    private Integer isCompose;

    @NotBlank(message = "商品轮播图不能为空")
    private String imgUrls;

    @NotNull(message = "市场价不能为空")
    @Min(value = 0, message = "市场价最小为0")
    private Long marketPriceFee;

    @NotNull(message = "售价不能为空")
    @Min(value = 0, message = "售价最小为0")
    private Long priceFee;

    private Integer scoreFee;

    @NotNull(message = "商品排序字段不能为空")
    private Integer seq;

    private Integer status;

    @NotBlank(message = "商品卖点不能为空")
    private String sellingPoint;

    private String detail;

    private List<SkuDto> skuList;

    private List<SpuAttrValueDto> spuAttrValues;

}
