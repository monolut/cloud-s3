package com.clouds3.bucketservice.client;

import com.clouds3.bucketservice.dto.BucketStatsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "object-service", url = "http://localhost:8082")
public interface ObjectClient {

    @GetMapping("/api/v1/buckets/{bucketId}/stats")
    BucketStatsDto getBucketStats(
            @PathVariable Long bucketId
    );
}
