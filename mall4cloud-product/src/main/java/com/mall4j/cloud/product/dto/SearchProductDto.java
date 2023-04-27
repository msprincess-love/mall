package com.mall4j.cloud.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 16:56
 * @description mall
 */
@Data
public class SearchProductDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer partyCode;

    private Integer keyWord;

    private Long shopPrimaryCategoryId;

    private Long shopSecondaryCategoryId;

    private Integer minSaleNum;

    private Integer maxSaleNum;

    private Integer minPrice;

    private Integer maxPrice;

    private Integer dataType;
}
