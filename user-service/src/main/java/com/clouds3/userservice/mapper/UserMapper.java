package com.clouds3.userservice.mapper;

import com.clouds3.userservice.dto.UserDto;
import com.clouds3.userservice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    UserEntity toEntity(UserDto userDto);
    UserDto toDto(UserEntity userEntity);
}
