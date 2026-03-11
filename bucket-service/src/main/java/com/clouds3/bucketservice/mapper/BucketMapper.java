package com.clouds3.bucketservice.mapper;

import com.clouds3.bucketservice.dto.BucketDto;
import com.clouds3.bucketservice.dto.BucketMetadataDto;
import com.clouds3.bucketservice.entity.BucketEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BucketMapper {

    BucketEntity toEntity(BucketDto dto);

    @Named("full")
    BucketDto toDto(BucketEntity entity);

    @Named("metadata")
    BucketMetadataDto toMetadata(BucketEntity entity);

    @IterableMapping(qualifiedByName = "full")
    List<BucketDto> toDtoList(List<BucketEntity> entities);

    @IterableMapping(qualifiedByName = "metadata")
    List<BucketMetadataDto> toMetadataList(List<BucketEntity> entities);
}
