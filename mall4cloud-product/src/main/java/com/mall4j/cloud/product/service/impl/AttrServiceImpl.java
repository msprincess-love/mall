package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.bo.AttrValueBo;
import com.mall4j.cloud.product.dto.AttrDto;
import com.mall4j.cloud.product.mapper.AttrCategoryMapper;
import com.mall4j.cloud.product.mapper.AttrMapper;
import com.mall4j.cloud.product.mapper.AttrValueMapper;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.IAttrService;
import com.mall4j.cloud.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mall4j.cloud.product.constant.ProductConstant.SHOP_ATTR_VALUE;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/24 17:07
 * @description mall
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements IAttrService {

    @Autowired
    private AttrMapper attrMapper;
    @Autowired
    private AttrCategoryMapper  attrCategoryMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;

    @Override
    @SuppressWarnings("all")
    public Page<AttrVo> pageAttrVos(Integer pageNum, Integer pageSize, String name, Integer attrType) {
        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        if (name != null)
            wrapper.like(Attr::getName, name);
        if (attrType != null)
            wrapper.eq(Attr::getAttrType, attrType);
        if (AuthUserContext.get().getTenantId() != 0)
            wrapper.eq(Attr::getAttrType, SHOP_ATTR_VALUE);
        Page<Attr> pageInfo = new Page<>(pageNum, pageSize);
        Page<Attr> page = page(pageInfo, wrapper);

        List<Attr> attrs = page.getRecords();
        List<AttrVo> attrVos = attrs.stream().map(attr -> {
            AttrVo attrVo = new AttrVo();
            BeanUtils.copyProperties(attr, attrVo);
            LambdaQueryWrapper<AttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
            attrValueWrapper.eq(AttrValue::getAttrId, attr.getAttrId());

            List<AttrValue> attrValues = attrValueMapper.selectList(attrValueWrapper);
            List<String> values = attrValues.stream().map(AttrValue::getValue).collect(Collectors.toList());

            List<AttrValueBo> attrValueBos = values.stream().map(value -> {
                AttrValueBo attrValueBo = new AttrValueBo();
                attrValueBo.setValue(value);
                return attrValueBo;
            }).collect(Collectors.toList());

            attrVo.setAttrValues(attrValueBos);
            return attrVo;
        }).collect(Collectors.toList());
        Page<AttrVo> pageVo = new Page<>();
        BeanUtils.copyProperties(page, pageVo);
        pageVo.setRecords(attrVos);
        return pageVo;
    }

    @Override
    @Transactional
    public void addBrand(AttrDto attrDto) {
        Attr attr = new Attr();
        BeanUtils.copyProperties(attrDto, attr);
        attrMapper.insert(attr);

        AttrValue attrValue = new AttrValue();
        attrValue.setAttrId(attr.getAttrId());
        for (AttrValueBo attrValueBo : attrDto.getAttrValues()) {
            attrValue.setValue(attrValueBo.getValue());
            attrValueMapper.insert(attrValue);
        }

        AttrCategory attrCategory = new AttrCategory();
        attrCategory.setAttrId(attr.getAttrId());
        for (Long categoryId : attrDto.getCategoryIds()) {
            attrCategory.setCategoryId(categoryId);
            attrCategoryMapper.insert(attrCategory);
        }
    }

    @Override
    public AttrDto getAttrById(Long attrId) {
        Attr attr = getById(attrId);
        AttrDto attrDto = new AttrDto();
        BeanUtils.copyProperties(attr, attrDto);

        LambdaQueryWrapper<AttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
        attrValueWrapper.eq(AttrValue::getAttrId, attr.getAttrId());
        List<AttrValue> attrValues = attrValueMapper.selectList(attrValueWrapper);

        List<AttrValueBo> attrValueBos = attrValues.stream().map(attrValue -> {
            AttrValueBo attrValueBo = new AttrValueBo();
            attrValueBo.setValue(attrValue.getValue());
            return attrValueBo;
        }).collect(Collectors.toList());
        attrDto.setAttrValues(attrValueBos);

        LambdaQueryWrapper<AttrCategory> attrCategoryWrapper = new LambdaQueryWrapper<>();
        attrCategoryWrapper.eq(AttrCategory::getAttrId, attr.getAttrId());
        List<AttrCategory> attrCategories = attrCategoryMapper.selectList(attrCategoryWrapper);
        List<Long> ids = attrCategories.stream().map(AttrCategory::getCategoryId).collect(Collectors.toList());
        attrDto.setCategoryIds(ids);

        return attrDto;
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public void deleteAttrById(Long attrId) {
        if (getById(attrId) == null)
            throw new RuntimeException("不存在id为" + attrId + "的数据");
        LambdaQueryWrapper<AttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
        attrValueWrapper.eq(AttrValue::getAttrId, attrId);
        List<AttrValue> attrValues = attrValueMapper.selectList(attrValueWrapper);

        List<Long> attrValueIds = attrValues.stream().map(AttrValue::getAttrValueId).collect(Collectors.toList());
        attrValueMapper.deleteBatchIds(attrValueIds);

        LambdaQueryWrapper<AttrCategory> attrCategoryWrapper = new LambdaQueryWrapper<>();
        attrCategoryWrapper.eq(AttrCategory::getAttrId, attrId);
        List<AttrCategory> attrCategories = attrCategoryMapper.selectList(attrCategoryWrapper);

        List<Long> attrCategoryIds = attrCategories.stream().map(AttrCategory::getAttrCategoryId).collect(Collectors.toList());
        attrCategoryMapper.deleteBatchIds(attrCategoryIds);
        removeById(attrId);
    }

    @Override
    public void updateAttr(AttrDto attrDto) {
        // 更新主表
        Attr attr = new Attr();
        BeanUtils.copyProperties(attrDto, attr);
        updateById(attr);

        // 更新副表 attr_value
        LambdaQueryWrapper<AttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
        attrValueWrapper.eq(AttrValue::getAttrId, attr.getAttrId());
        // 当前数据库中关联的 attrValues
        List<AttrValue> attrValues = attrValueMapper.selectList(attrValueWrapper);
        // 前端修改的 attrValues
        List<AttrValueBo> attrValueBos = attrDto.getAttrValues();

        int a = attrValues.size();
        int b = attrValueBos.size();

        if (a > b) {
            for (int i = 0; i < b; i ++) {
                attrValues.get(i).setValue(attrValueBos.get(i).getValue());
                attrValueMapper.updateById(attrValues.get(i));
            }
            List<AttrValue> newAttrValues = attrValues.subList(b, a);
            List<Long> ids = newAttrValues.stream().map(AttrValue::getAttrValueId).collect(Collectors.toList());
            attrValueMapper.deleteBatchIds(ids);
        }
        else if (a < b) {
            for (int i = 0; i < a; i ++) {
                attrValues.get(i).setValue(attrValueBos.get(i).getValue());
                attrValueMapper.updateById(attrValues.get(i));
            }
            for (int i = a; i < b; i ++) {
                AttrValue attrValue = new AttrValue();
                attrValue.setValue(attrValueBos.get(i).getValue());
                attrValue.setAttrId(attr.getAttrId());
                attrValueMapper.insert(attrValue);
            }
        }
        else {
            for (int i = 0; i < a; i ++) {
                attrValues.get(i).setValue(attrValueBos.get(i).getValue());
                attrValueMapper.updateById(attrValues.get(i));
            }
        }

        LambdaQueryWrapper<AttrCategory> attrCategoryWrapper = new LambdaQueryWrapper<>();
        attrCategoryWrapper.eq(AttrCategory::getAttrId, attr.getAttrId());
        List<AttrCategory> attrCategories = attrCategoryMapper.selectList(attrCategoryWrapper);

        List<Long> categoryIds = attrDto.getCategoryIds();
        // 自己从数据库中查出来的数据
        int m = attrCategories.size();
        // 前端传过来的数据
        int n = categoryIds.size();

        if (m > n) {
            for (int i = 0; i < n; i ++) {
                attrCategories.get(i).setCategoryId(categoryIds.get(i));
                attrCategoryMapper.updateById(attrCategories.get(i));
            }
            List<AttrCategory> newAttrCategories = attrCategories.subList(n, m);
            List<Long> ids = newAttrCategories.stream().map(AttrCategory::getAttrCategoryId).collect(Collectors.toList());
            attrCategoryMapper.deleteBatchIds(ids);
        }
        else if (m < n) {
            for (int i = 0; i < m; i ++) {
                attrCategories.get(i).setCategoryId(categoryIds.get(i));
                attrCategoryMapper.updateById(attrCategories.get(i));
            }
            for (int i = m; i < n; i ++) {
                AttrCategory attrCategory = new AttrCategory();
                attrCategory.setCategoryId(categoryIds.get(i));
                attrCategory.setAttrId(attr.getAttrId());
                attrCategoryMapper.insert(attrCategory);
            }
        }
        else {
            for (int i = 0; i < m; i ++) {
                attrCategories.get(i).setCategoryId(categoryIds.get(i));
                attrCategoryMapper.updateById(attrCategories.get(i));
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public List<AttrVo> getAttrByCategoryId(Long categoryId, Integer attrType) {
        ArrayList<AttrVo> attrVos = new ArrayList<>();
        LambdaQueryWrapper<AttrCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrCategory::getCategoryId, categoryId);
        List<AttrCategory> attrCategories = attrCategoryMapper.selectList(wrapper);

        List<Long> ids = attrCategories.stream().map(AttrCategory::getAttrId).collect(Collectors.toList());
        LambdaQueryWrapper<Attr> attrWrapper = new LambdaQueryWrapper<>();
        attrWrapper.in(Attr::getAttrId, ids);
        attrWrapper.eq(Attr::getAttrType, attrType);
        // 查出关联此分类下的所有属性（基本属性）
        List<Attr> attrs = attrMapper.selectList(attrWrapper);

        for (Attr attr : attrs) {
            AttrVo attrVo = new AttrVo();
            attrVo.setName(attr.getName());

            // 获取每个属性下的attrValue
            LambdaQueryWrapper<AttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
            attrValueWrapper.eq(AttrValue::getAttrId, attr.getAttrId());
            List<AttrValue> attrValues = attrValueMapper.selectList(attrValueWrapper);

            List<AttrValueBo> attrValueBos = attrValues.stream().map(attrValue -> {
                AttrValueBo attrValueBo = new AttrValueBo();
                attrValueBo.setAttrValueId(attrValue.getAttrValueId());
                attrValueBo.setValue(attrValue.getValue());
                return attrValueBo;
            }).collect(Collectors.toList());

            attrVo.setAttrValues(attrValueBos);

            attrVos.add(attrVo);
        }
        return attrVos;
    }

    @Override
    @SuppressWarnings("all")
    public List<AttrVo> getShopAttrs() {
        LambdaQueryWrapper<Attr> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attr::getAttrType, SHOP_ATTR_VALUE);
        List<Attr> attrs = list(wrapper);

        List<AttrVo> attrVos = attrs.stream().map(attr -> {
            AttrVo attrVo = new AttrVo();
            BeanUtils.copyProperties(attr, attrVo);
            LambdaQueryWrapper<AttrValue> attrValueWrapper = new LambdaQueryWrapper<>();
            attrValueWrapper.eq(AttrValue::getAttrId, attr.getAttrId());
            List<AttrValue> attrValues = attrValueMapper.selectList(attrValueWrapper);

            List<AttrValueBo> attrValueBos = attrValues.stream().map(attrValue -> {
                AttrValueBo attrValueBo = new AttrValueBo();
                attrValueBo.setAttrValueId(attrValue.getAttrValueId());
                attrValueBo.setValue(attrValue.getValue());
                return attrValueBo;
            }).collect(Collectors.toList());

            attrVo.setAttrValues(attrValueBos);
            return attrVo;
        }).collect(Collectors.toList());

        return attrVos;
    }
}
