package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/21 19:59
 * @description mall
 */
@Data
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long categoryId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Long shopId;

    private Long parentId;

    private String name;

    @TableField("`desc`")
    private String desc;

    private String path;

    @ApiModelProperty(value = "分类的状态", notes = "默认是1也就是启用的状态")
    private Integer status;

    @JsonSerialize(using = ImgJsonSerializer.class)
    private String icon;

    @JsonSerialize(using = ImgJsonSerializer.class)
    private String imgUrl;

    private Integer level;

    private Integer seq;
}
