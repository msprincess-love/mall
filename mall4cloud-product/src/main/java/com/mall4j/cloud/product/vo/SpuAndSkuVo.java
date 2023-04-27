package com.mall4j.cloud.product.vo;

import lombok.Data;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 18:01
 * @description mall
 */
@Data
public class SpuAndSkuVo {

    private Long skuId;

    private String imgUrl;

    private String skuName;

    private Long priceFee;

    private Long marketPriceFee;

    private Integer stock;
}
