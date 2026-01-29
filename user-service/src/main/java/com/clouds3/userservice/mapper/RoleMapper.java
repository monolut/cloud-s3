package com.clouds3.userservice.mapper;

import com.clouds3.userservice.dto.RoleDto;
import com.clouds3.userservice.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(
        source = "role", target = "roleName"
    )
    RoleEntity toEntity(RoleDto roleDto);

    @Mapping(
            source = "roleName", target = "role"
    )
    RoleDto toDto(RoleEntity roleEntity);
}
