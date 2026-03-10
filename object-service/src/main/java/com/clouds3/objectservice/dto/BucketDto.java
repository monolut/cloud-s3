package com.clouds3.objectservice.dto;

import com.clouds3.objectservice.enums.BucketType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BucketDto {

    private Long id;
    private String name;
    private Long ownerId;
    private BucketType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long size;
    private Integer objectCount;
    private Boolean blocked;
}
