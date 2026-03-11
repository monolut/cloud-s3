package com.clouds3.objectservice.mapper;

import com.clouds3.objectservice.dto.ObjectResponseDto;
import com.clouds3.objectservice.entity.ObjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObjectMapper {

    ObjectEntity toEntity(ObjectResponseDto objectResponseDto);

    ObjectResponseDto toResponseDto(ObjectEntity objectEntity);
}
