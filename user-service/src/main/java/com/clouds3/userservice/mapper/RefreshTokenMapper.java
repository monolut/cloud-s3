package com.clouds3.userservice.mapper;

import com.clouds3.userservice.dto.auth.RefreshTokenDto;
import com.clouds3.userservice.entity.RefreshTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper{

    @Mappings({
                    @Mapping(source = "userId", target = "user.id")
    })
    RefreshTokenEntity toEntity(RefreshTokenDto refreshTokenDto);

    @Mappings({
            @Mapping(source = "user.id", target = "userId")
    })
    RefreshTokenDto toDto(RefreshTokenEntity refreshTokenEntity);
}
