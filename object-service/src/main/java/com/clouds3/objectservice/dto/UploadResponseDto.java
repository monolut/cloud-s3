package com.clouds3.objectservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadResponseDto {

    private Long id;
    private String objectKey;
    private String message;
}