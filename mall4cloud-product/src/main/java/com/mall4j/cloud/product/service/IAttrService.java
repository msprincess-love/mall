package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.product.dto.AttrDto;
import com.mall4j.cloud.product.model.Attr;
import com.mall4j.cloud.product.vo.AttrVo;

import java.util.List;


/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/24 17:07
 * @description mall
 */
public interface IAttrService extends IService<Attr> {
    Page<AttrVo> pageAttrVos(Integer pageNum, Integer pageSize, String name, Integer attrType);

    void addBrand(AttrDto attrDto);

    AttrDto getAttrById(Long attrId);

    void deleteAttrById(Long attrId);

    void updateAttr(AttrDto attrDto);

    List<AttrVo> getAttrByCategoryId(Long categoryId, Integer attrType);

    List<AttrVo> getShopAttrs();


}
