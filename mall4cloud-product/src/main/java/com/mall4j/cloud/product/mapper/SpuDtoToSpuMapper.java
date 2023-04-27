package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.dto.SpuDto;
import com.mall4j.cloud.product.model.Spu;
import org.mapstruct.Mapper;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/27 18:15
 * @description mall
 */
@Mapper
public interface SpuDtoToSpuMapper {

    SpuDto toDto(Spu user);

    Spu toEntity(SpuDto dto);

}
