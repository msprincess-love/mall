package com.mall4j.cloud.product.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/24 21:42
 * @description mall
 */
@Data
public class AttrValueBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long attrValueId;

    private String value;
}
