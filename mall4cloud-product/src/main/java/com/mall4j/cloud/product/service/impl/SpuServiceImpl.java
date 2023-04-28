package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.*;
import com.mall4j.cloud.product.mapper.*;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.ISpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mall4j.cloud.product.mapper.SkuDtoToSkuMapper.SKU_INSTANCT;
import static com.mall4j.cloud.product.mapper.SpuDtoToSpuMapper.INSTANCT;


/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 15:55
 * @description mall
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements ISpuService {

    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuSkuAttrValueMapper spuSkuAttrValueMapper;
    @Autowired
    private SpuAttrValueMapper spuAttrValueMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    @Transactional
    @SuppressWarnings("all")
    public void addSpuAndSkuAndAttr(SpuDto spuDto) {
        Spu spu = new Spu();
        spu.setBrandId(spuDto.getBrandId());
        spu.setCategoryId(spuDto.getCategoryId());
        spu.setShopCategoryId(spuDto.getShopCategoryId());
        spu.setCategoryId(spuDto.getCategoryId());
        spu.setShopId(AuthUserContext.get().getTenantId());
        spu.setName(spuDto.getName());
        spu.setSellingPoint(spuDto.getSellingPoint());
        spu.setMainImgUrl(spuDto.getMainImgUrl());
        spu.setImgUrls(spuDto.getImgUrls());
        spu.setMarketPriceFee(spuDto.getMarketPriceFee());
        spu.setPriceFee(spuDto.getPriceFee());
        // 设置 spu 的默认状态，默认为下架状态（0为下架）
        spu.setStatus(0);
        // 设置 spu 的是否有sku图片
        if (spuDto.getMainImgUrl() != null)
            spu.setHasSkuImg(1);
        spu.setSeq(spuDto.getSeq());
        spuMapper.insert(spu);

        // 保存sku的信息
        List<SkuDto> skuList = spuDto.getSkuList();
        for (SkuDto skuDto : skuList) {
            Sku sku = new Sku();
            sku.setAttrs(skuDto.getAttrs());
            sku.setImgUrl(skuDto.getImgUrl());
            sku.setMarketPriceFee(skuDto.getMarketPriceFee());
            sku.setSkuName(skuDto.getSkuName());
            sku.setPriceFee(skuDto.getPriceFee());
            sku.setPartyCode(skuDto.getPartyCode());
            sku.setModelId(skuDto.getModelId());
            sku.setSpuId(spu.getSpuId());
            sku.setStatus(0);
            skuMapper.insert(sku);

            // 保存库存
            SkuStock skuStock = new SkuStock();
            skuStock.setStock(skuDto.getStock());
            skuStock.setSkuId(sku.getSkuId());
            skuStockMapper.insert(skuStock);
            // 每个sku中都存在 sku，spu和销售属性之间的关联
            List<SpuSkuAttrValueDto> spuSkuAttrValues = skuDto.getSpuSkuAttrValues();

            for (SpuSkuAttrValueDto spuSkuAttrValueDto : spuSkuAttrValues) {
                SpuSkuAttrValue spuSkuAttrValue = new SpuSkuAttrValue();
                spuSkuAttrValue.setAttrId(spuSkuAttrValueDto.getAttrId());
                spuSkuAttrValue.setAttrName(spuSkuAttrValueDto.getAttrName());
                spuSkuAttrValue.setAttrValueId(spuSkuAttrValueDto.getAttrValueId());
                spuSkuAttrValue.setAttrValueName(spuSkuAttrValueDto.getAttrValueName());
                spuSkuAttrValue.setSpuId(spu.getSpuId());
                spuSkuAttrValue.setSkuId(sku.getSkuId());
                spuSkuAttrValueMapper.insert(spuSkuAttrValue);
            }
        }

        // 保存 基本属性 和spu的关联
        List<SpuAttrValueDto> spuAttrValues = spuDto.getSpuAttrValues();
        for (SpuAttrValueDto spuAttrValueDto : spuAttrValues) {
            SpuAttrValue spuAttrValue = new SpuAttrValue();
            spuAttrValue.setAttrId(spuAttrValueDto.getAttrId());
            spuAttrValue.setAttrValueId(spuAttrValueDto.getAttrValueId());
            spuAttrValue.setAttrName(spuAttrValueDto.getAttrName());
            spuAttrValue.setAttrValueName(spuAttrValueDto.getAttrValueName());
            spuAttrValue.setAttrValueId(spuAttrValueDto.getAttrValueId());
            spuAttrValue.setSpuId(spu.getSpuId());
            spuAttrValueMapper.insert(spuAttrValue);
        }
        String detail = spuDto.getDetail();
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spu.getSpuId());
        spuDetail.setDetail(detail);
        spuDetailMapper.insert(spuDetail);
    }

    @Override
    @Transactional
    public void deleteSpuAndSkuAndAttrById(Long spuId) {
        // 首先删除 spu 表的相关信息
        LambdaQueryWrapper<Spu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Spu::getSpuId, spuId);
        removeById(spuId);

        // 删除 spu 关联的 sku 信息 , 一个 spu 对应 多个sku
        LambdaQueryWrapper<Sku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(Sku::getSpuId, spuId);
        List<Sku> skus = skuMapper.selectList(skuWrapper);
        List<Long> skuIds = skus.stream().map(Sku::getSkuId).collect(Collectors.toList());
        skuMapper.deleteBatchIds(skuIds);

        // 删除 spu 关联的 基本属性，一个 spu 对应多个 基本属性
        LambdaQueryWrapper<SpuAttrValue> spuAttrWrapper = new LambdaQueryWrapper<>();
        spuAttrWrapper.eq(SpuAttrValue::getSpuId, spuId);
        List<SpuAttrValue> spuAttrValues = spuAttrValueMapper.selectList(spuAttrWrapper);
        List<Long> spuAttrIds = spuAttrValues.stream().map(SpuAttrValue::getSpuAttrValueId).collect(Collectors.toList());
        spuAttrValueMapper.deleteBatchIds(spuAttrIds);

        // 删除 spu ， sku 关联的销售属性
        LambdaQueryWrapper<SpuSkuAttrValue> spuSkuAttrWrapper = new LambdaQueryWrapper<>();
        spuSkuAttrWrapper.eq(SpuSkuAttrValue::getSpuId, spuId);
        List<SpuSkuAttrValue> spuSkuAttrValues = spuSkuAttrValueMapper.selectList(spuSkuAttrWrapper);
        List<Integer> spuSkuAttrIds = spuSkuAttrValues.stream().map(SpuSkuAttrValue::getSpuSkuAttrId).collect(Collectors.toList());
        spuSkuAttrValueMapper.deleteBatchIds(spuSkuAttrIds);
    }

    @Override
    public SpuDto getSpuAndSkuAndAttrById(Long spuId) {
        // 获取 spu 方面的数据
        Spu spu = getById(spuId);
        SpuDto spuDto = INSTANCT.toDto(spu);

        // 获取 sku 方面的数据
        LambdaQueryWrapper<Sku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(Sku::getSpuId, spuId);
        List<Sku> skus = skuMapper.selectList(skuWrapper);

        for (Sku sku : skus) {
            SkuDto skuDto = SKU_INSTANCT.toDto(sku);
            LambdaQueryWrapper<SkuStock> skuStockWrapper = new LambdaQueryWrapper<>();
            skuStockWrapper.eq(SkuStock::getSkuId, sku.getSkuId());
            SkuStock skuStock = skuStockMapper.selectOne(skuStockWrapper);
            skuDto.setStock(skuStock.getStock());

            LambdaQueryWrapper<SpuSkuAttrValue> spuSkuAttrWrapper = new LambdaQueryWrapper<>();
            spuSkuAttrWrapper.eq(SpuSkuAttrValue::getSkuId, sku.getSkuId());
            List<SpuSkuAttrValue> spuSkuAttrValues = spuSkuAttrValueMapper.selectList(spuSkuAttrWrapper);
            List<SpuSkuAttrValueDto> spuSkuAttrValueDtoList = spuSkuAttrValues.stream().map(spuSkuAttrValue -> {
                SpuSkuAttrValueDto spuSkuAttrValueDto = new SpuSkuAttrValueDto();
                spuSkuAttrValueDto.setSpuSkuAttrId(spuSkuAttrValue.getSpuSkuAttrId());
                spuSkuAttrValueDto.setAttrName(spuSkuAttrValue.getAttrName());
                spuSkuAttrValueDto.setAttrValueName(spuSkuAttrValue.getAttrValueName());
                spuSkuAttrValueDto.setAttrValueId(spuSkuAttrValue.getAttrValueId());
                spuSkuAttrValueDto.setAttrId(spuSkuAttrValue.getAttrId());
                return spuSkuAttrValueDto;
            }).collect(Collectors.toList());

            skuDto.setSpuSkuAttrValues(spuSkuAttrValueDtoList);
        }

        LambdaQueryWrapper<SpuAttrValue> spuAttrValueWrapper = new LambdaQueryWrapper<>();
        spuAttrValueWrapper.eq(SpuAttrValue::getSpuId, spuId);
        List<SpuAttrValue> spuAttrValues = spuAttrValueMapper.selectList(spuAttrValueWrapper);

        List<SpuAttrValueDto> spuAttrValueDtoList = spuAttrValues.stream().map(spuAttrValue -> {
            SpuAttrValueDto spuAttrValueDto = new SpuAttrValueDto();
            spuAttrValueDto.setSpuAttrValueId(spuAttrValue.getSpuAttrValueId());
            spuAttrValueDto.setAttrName(spuAttrValue.getAttrName());
            spuAttrValueDto.setAttrValueName(spuAttrValue.getAttrValueName());
            spuAttrValueDto.setAttrId(spuAttrValue.getAttrId());
            spuAttrValueDto.setAttrValueId(spuAttrValue.getAttrValueId());
            return spuAttrValueDto;
        }).collect(Collectors.toList());
        spuDto.setSpuAttrValues(spuAttrValueDtoList);

        return spuDto;
    }

    @Override
    @Transactional
    public void updateSpuAndSkuAndAttr(SpuDto spuDto) {
        Spu spu = INSTANCT.toEntity(spuDto);
        updateById(spu);

        List<SkuDto> skuList = spuDto.getSkuList();
        for (SkuDto skuDto : skuList) {
            Sku sku = skuMapper.selectById(skuDto.getSkuId());
            sku = SKU_INSTANCT.toEntity(skuDto);
            skuMapper.updateById(sku);

            List<SpuSkuAttrValueDto> spuSkuAttrValues = skuDto.getSpuSkuAttrValues();
            for (SpuSkuAttrValueDto spuSkuAttrValueDto : spuSkuAttrValues) {
                SpuSkuAttrValue spuSkuAttrValue = spuSkuAttrValueMapper.selectById(spuSkuAttrValueDto.getSpuSkuAttrId());
                BeanUtils.copyProperties(spuSkuAttrValueDto, spuSkuAttrValue);
                spuSkuAttrValueMapper.updateById(spuSkuAttrValue);
            }

            Integer stock = skuDto.getStock();
            LambdaQueryWrapper<SkuStock> skuStockWrapper = new LambdaQueryWrapper<>();
            skuStockWrapper.eq(SkuStock::getSkuId, skuDto.getSkuId());
            SkuStock skuStock = skuStockMapper.selectOne(skuStockWrapper);
            skuStock.setStock(stock);
            skuStockMapper.updateById(skuStock);
        }
        List<SpuAttrValueDto> spuAttrValues = spuDto.getSpuAttrValues();
        for (SpuAttrValueDto spuAttrValueDto : spuAttrValues) {
            SpuAttrValue spuAttrValue = new SpuAttrValue();
            BeanUtils.copyProperties(spuAttrValueDto, spuAttrValue);
            spuAttrValueMapper.updateById(spuAttrValue);
        }
    }

//    @Override
//    public Page<SpuAndSkuVo> pageAllProducts(Integer pageSize, Integer pageNum, SearchProductDto searchProductDto) {
//        Page<Sku> skuPage = new Page<>(pageNum, pageSize);
//        LambdaQueryWrapper<Sku> wrapper = new LambdaQueryWrapper<>();
//        if (searchProductDto.getPartyCode() != null)
//            wrapper.eq(Sku::getPartyCode, searchProductDto.getPartyCode());
//        if (searchProductDto.getKeyWord() != null)
//            wrapper.like(Sku::getSkuName, searchProductDto.getKeyWord());
//        if (searchProductDto.getMinPrice() != null)
//            wrapper.gt(Sku::getPriceFee, searchProductDto.getMinPrice());
//        if (searchProductDto.getMaxPrice() != null)
//            wrapper.lt(Sku::getPriceFee, searchProductDto.getMaxPrice());
//        Integer dataType = searchProductDto.getDataType();
//        if (dataType != null) {
//            if (dataType == 1) {
//                wrapper.eq(Sku::getStatus, IN_SHOPPING);
//            }
//            if (dataType == 2) {
//                wrapper.eq(Sku::getStatus, NO_STOCK);
//            }
//            if (dataType == 3) {
//                wrapper.eq(Sku::getStatus, NO_SHOPPING);
//            }
//        }
//        Page<Sku> page = skuMapper.selectPage(skuPage, wrapper);
//        List<Sku> skus = page.getRecords();
//        if (searchProductDto.getShopPrimaryCategoryId() != null) {
//            skus = skus.stream().filter(sku -> {
//                LambdaQueryWrapper<Spu> spuWrapper = new LambdaQueryWrapper<>();
//                spuWrapper.eq(Spu::getSpuId, sku.getSpuId());
//                Spu spu = getOne(spuWrapper);
//                Long shopCategoryId = spu.getShopCategoryId();
//
//                LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
//                categoryWrapper.eq(Category::getCategoryId, shopCategoryId);
//                Category category = categoryMapper.selectOne(categoryWrapper);
//
//                return Objects.equals(category.getParentId(), searchProductDto.getShopSecondaryCategoryId());
//            }).collect(Collectors.toList());
//        }
//        if (searchProductDto.getShopSecondaryCategoryId() != null) {
//            skus = skus.stream().filter(sku -> {
//                LambdaQueryWrapper<Spu> spuWrapper = new LambdaQueryWrapper<>();
//                spuWrapper.eq(Spu::getSpuId, sku.getSpuId());
//                Spu spu = getOne(spuWrapper);
//                Long shopCategoryId = spu.getShopCategoryId();
//                return Objects.equals(shopCategoryId, searchProductDto.getShopSecondaryCategoryId());
//            }).collect(Collectors.toList());
//        }
//
//        List<SpuAndSkuVo> spuAndSkuVos = skus.stream().map(sku -> {
//            SpuAndSkuVo spuAndSkuVo = new SpuAndSkuVo();
//            BeanUtils.copyProperties(sku, spuAndSkuVo);
//            LambdaQueryWrapper<SkuStock> skuStockWrapper = new LambdaQueryWrapper<>();
//            skuStockWrapper.eq(SkuStock::getSkuId, sku.getSkuId());
//            SkuStock skuStock = skuStockMapper.selectOne(skuStockWrapper);
//
//            spuAndSkuVo.setStock(skuStock.getStock());
//            return spuAndSkuVo;
//        }).collect(Collectors.toList());
//
//        Page<SpuAndSkuVo> spuAndSkuVoPage = new Page<>();
//        BeanUtils.copyProperties(page, spuAndSkuVoPage);
//        spuAndSkuVoPage.setRecords(spuAndSkuVos);
//
//        return spuAndSkuVoPage;
//    }
}
