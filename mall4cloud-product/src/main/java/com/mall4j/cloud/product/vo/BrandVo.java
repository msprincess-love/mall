package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.product.bo.CategoryBo;
import com.mall4j.cloud.product.model.Category;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 17:46
 * @description mall
 */
@Data
public class BrandVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long brandId;

    private List<CategoryBo> categories;

    private String name;

    private String desc;

    private String imgUrl;

    private Character firstLetter;

    private Integer seq;

    private Integer status;
}
