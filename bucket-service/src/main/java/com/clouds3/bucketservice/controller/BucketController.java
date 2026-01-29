package com.clouds3.bucketservice.controller;

import com.clouds3.bucketservice.dto.BucketDto;
import com.clouds3.bucketservice.enums.BucketType;
import com.clouds3.bucketservice.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/buckets")
@CrossOrigin("*")
public class BucketController {

    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BucketDto> createBucket(
            @RequestParam Long ownerId,
            @RequestParam String name,
            @RequestParam BucketType type
    ) {
        log.info("HTTP POST /buckets - ownerId={}, name={}, type={}", ownerId, name, type);
        BucketDto created =  bucketService.createBucket(ownerId, name, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BucketDto> getBucket(@PathVariable Long id) {
        log.info("HTTP GET /buckets/{}", id);
        BucketDto bucket = bucketService.getBucket(id);
        return ResponseEntity.ok(bucket);
    }

    @GetMapping
    public ResponseEntity<List<BucketDto>> getBuckets(@RequestParam Long ownerId) {
        log.info("HTTP GET /buckets?ownerId={}", ownerId);
        List<BucketDto> buckets = bucketService.getBucketsByOwner(ownerId);
        return ResponseEntity.ok(buckets);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BucketDto> updateBucket(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BucketType type
    ) {
        log.info("HTTP PATCH /buckets/{} - newName={}, newType={}", id, name, type);

        BucketDto updated = bucketService.updateBucket(id, name, type);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBucket(@PathVariable Long id) {
        log.info("HTTP DELETE /buckets/{}", id);
        bucketService.deleteBucket(id);
        return ResponseEntity.noContent().build();
    }
}
