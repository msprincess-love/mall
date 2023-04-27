package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 15:51
 * @description mall
 */
@Data
public class Spu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long spuId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Long brandId;

    private Long categoryId;

    private Long shopCategoryId;

    private Long shopId;

    private String name;

    private String sellingPoint;

    private String mainImgUrl;

    private String imgUrls;

    private String video;

    private Long priceFee;

    private Long marketPriceFee;

    private Integer status;

    private Integer hasSkuImg;

    private Integer seq;
}
