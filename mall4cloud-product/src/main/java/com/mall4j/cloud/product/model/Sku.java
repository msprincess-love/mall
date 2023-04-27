package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 16:09
 * @description mall
 */
@Data
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long skuId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Long spuId;

    private String skuName;

    private String attrs;

    private String imgUrl;

    private Long priceFee;

    private Long marketPriceFee;

    private String partyCode;

    private String modelId;

    private BigDecimal weight;

    private BigDecimal volume;

    private Integer status;
}
