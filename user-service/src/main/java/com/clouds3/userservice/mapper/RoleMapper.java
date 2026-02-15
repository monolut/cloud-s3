package com.clouds3.userservice.mapper;

import com.clouds3.userservice.dto.RoleDto;
import com.clouds3.userservice.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleEntity toEntity(RoleDto roleDto);

    RoleDto toDto(RoleEntity roleEntity);
}
