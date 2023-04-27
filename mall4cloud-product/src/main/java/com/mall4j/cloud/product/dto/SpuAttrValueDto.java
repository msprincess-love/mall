package com.mall4j.cloud.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 10:42
 * @description mall
 */
@Data
public class SpuAttrValueDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long spuAttrValueId;

    private Long attrId;

    private String attrName;

    private Long attrValueId;

    private String attrValueName;
}
