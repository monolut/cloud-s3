package com.clouds3.objectservice.client;

import com.clouds3.objectservice.dto.BucketDto;
import com.clouds3.objectservice.enums.BucketType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "bucket-service", url = "${bucket.service.url}")
public interface BucketClient {

    @PostMapping("/api/v1/buckets")
    BucketDto createBucket(
            @RequestParam Long ownerId,
            @RequestParam String name,
            @RequestParam BucketType type
    );

    @GetMapping("/api/v1/buckets/{id}")
    BucketDto getBucket(@PathVariable Long id);

    @GetMapping("/api/v1/buckets")
    List<BucketDto> getBucketsByOwner(
            @RequestParam Long ownerId
    );

    @PatchMapping("/api/v1/buckets/{id}")
    BucketDto updateBucket(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BucketType type
    );

    @DeleteMapping("/api/v1/buckets/{id}")
    void deleteBucket(@PathVariable Long id);
}
