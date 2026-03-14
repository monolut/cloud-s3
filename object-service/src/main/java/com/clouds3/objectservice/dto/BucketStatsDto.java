package com.clouds3.objectservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BucketStatsDto {
    Long count;
    Long size;
}
