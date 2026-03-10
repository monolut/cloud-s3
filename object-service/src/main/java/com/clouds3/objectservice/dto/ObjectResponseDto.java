package com.clouds3.objectservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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