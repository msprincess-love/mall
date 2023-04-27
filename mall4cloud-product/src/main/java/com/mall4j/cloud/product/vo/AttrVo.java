package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.product.bo.AttrValueBo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 17:46
 * @description mall
 */
@Data
public class AttrVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long attrId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String name;

    private String desc;

    private Integer searchType;

    private Integer attrType;

    private List<AttrValueBo> attrValues;

}
