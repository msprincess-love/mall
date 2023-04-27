package com.mall4j.cloud.product.dto;

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
public class BrandDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long brandId;

    @NotNull
    private List<Long> categoryIds;

    private String name;

    private String desc;

    @NotBlank(message = "图片logo不能为空")
    private String imgUrl;

    @Pattern(regexp = "/^[a-zA-Z]$/")
    private Character firstLetter;

    private Integer seq;

    private Integer status;
}
