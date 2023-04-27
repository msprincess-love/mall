package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.product.bo.AttrValueBo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/24 17:03
 * @description mall
 */
@Data
public class AttrDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long attrId;

    @NotBlank(message = "属性名称不能为空")
    private String name;

    private String desc;

    @NotNull(message = "搜索类型不能为空")
    private Integer searchType;

    @NotNull(message = "销售属性不能为空")
    private Integer attrType;

    private Long attrValueId;

    private List<AttrValueBo> attrValues;

    private List<Long> categoryIds;
}
