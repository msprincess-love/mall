package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.dto.SkuDto;
import com.mall4j.cloud.product.model.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/27 18:25
 * @description mall
 */
@Mapper
public interface SkuDtoToSkuMapper {

    SkuDtoToSkuMapper SKU_INSTANCT = Mappers.getMapper(SkuDtoToSkuMapper.class);

    SkuDto toDto(Sku user);

    Sku toEntity(SkuDto dto);
}
