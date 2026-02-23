package com.clouds3.objectservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ObjectResponseDto {

    private Long id;
    private String objectKey;
    private Long bucketId;
    private Long ownerId;
    private String originalFileName;
    private String contentType;
    private Long size;
    private LocalDateTime createdAt;
}