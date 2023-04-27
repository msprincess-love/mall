package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 17:31
 * @description mall
 */
@Data
public class SpuAttrValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long spuAttrValueId;

    private Long spuId;

    private Long attrId;

    private String attrName;

    private Long attrValueId;

    private String attrValueName;

    private String attrDesc;
}
