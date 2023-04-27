package com.mall4j.cloud.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 10:38
 * @description mall
 */
@Data
public class SpuSkuAttrValueDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer spuSkuAttrId;

    private Integer attrId;

    private String attrName;

    private Integer attrValueId;

    private String attrValueName;
}
