package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.product.model.Category;
import lombok.Data;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 14:51
 * @description mall
 */
@Data
public class CategoryVo {

    private Long categoryId;

    private String name;

    private Integer level;

    private Long parentId;

    private List<CategoryVo> children;
}
