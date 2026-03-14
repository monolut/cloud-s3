package com.clouds3.bucketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketStatsDto {
    Long count;
    Long size;
}