package com.mall4j.cloud.product.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 8:06
 * @description mall
 */
@Data
public class CategoryBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> pathNames;

    private String name;

    private Long categoryId;


}
