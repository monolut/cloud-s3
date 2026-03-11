package com.clouds3.bucketservice.dto;

import com.clouds3.bucketservice.enums.BucketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketDto {

    private Long id;
    private String name;
    private Long ownerId;
    private BucketType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long size;
    private Integer objectCount;
    private boolean blocked;
}
