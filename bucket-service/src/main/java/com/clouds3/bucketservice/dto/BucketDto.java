package com.clouds3.bucketservice.dto;

import com.clouds3.bucketservice.enums.BucketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
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
