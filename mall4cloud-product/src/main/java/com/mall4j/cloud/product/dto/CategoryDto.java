package com.mall4j.cloud.product.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/22 13:32
 * @description mall
 */
@Data
public class CategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long categoryId;

    private String desc;

    @NotNull(message = "当前状态不能为空")
    private Integer status;

    @NotNull(message = "节点层级不能为空")
    private Integer level;

    @NotNull(message = "父节点不能为空")
    private Long parentId;

    private String name;

    @JsonSerialize(using = ImgJsonSerializer.class)
    private String icon;

    @JsonSerialize(using = ImgJsonSerializer.class)
    private String imgUrl;

    private Integer seq;
}
