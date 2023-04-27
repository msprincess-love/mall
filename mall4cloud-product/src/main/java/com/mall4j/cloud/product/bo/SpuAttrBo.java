package com.mall4j.cloud.product.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 16:03
 * @description mall
 */
@Data
public class SpuAttrBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String attrName;

    private Long attrValueId;

    private String attrValueName;

    private List<SpuAttrBo> spuAttrValues;

    private Integer seq;
}
