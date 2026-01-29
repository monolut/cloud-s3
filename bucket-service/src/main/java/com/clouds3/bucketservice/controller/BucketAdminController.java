package com.clouds3.bucketservice.controller;

import com.clouds3.bucketservice.dto.BucketDto;
import com.clouds3.bucketservice.dto.BucketMetadataDto;
import com.clouds3.bucketservice.service.BucketAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@CrossOrigin("*")
public class BucketAdminController {

    private final BucketAdminService bucketAdminService;

    @Autowired
    public BucketAdminController(BucketAdminService bucketAdminService) {
        this.bucketAdminService = bucketAdminService;
    }

    @GetMapping
    public ResponseEntity<List<BucketMetadataDto>> getAllBuckets() {
        log.info("ADMIN | Get all buckets");
        return ResponseEntity.ok(bucketAdminService.getAllBuckets());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BucketMetadataDto>> getBucketsByUser(
            @PathVariable Long userId
    ) {
        log.info("ADMIN | Get buckets for userId={}", userId);
        return ResponseEntity.ok(bucketAdminService.getBucketsByUser(userId));
    }

    @GetMapping("/{bucketId}")
    public ResponseEntity<BucketDto> getBucket(
            @PathVariable Long bucketId
    ) {
        log.info("ADMIN | Get bucket details bucketId={}", bucketId);
        return ResponseEntity.ok(bucketAdminService.getBucket(bucketId));
    }

    @PostMapping("/{bucketId}/recalculate")
    public ResponseEntity<Void> recalculateMetadata(
            @PathVariable Long bucketId
    ) {
        log.warn("ADMIN | Recalculate metadata for bucketId={}", bucketId);
        bucketAdminService.recalculateMetadata(bucketId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recalculate-all")
    public ResponseEntity<Void> recalculateAllMetadata() {
        log.warn("ADMIN | Recalculate metadata for ALL buckets");
        bucketAdminService.recalculateAll();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bucketId}")
    public ResponseEntity<Void> forceDelete(
            @PathVariable Long bucketId
    ) {
        log.warn("ADMIN | Force delete bucketId={}", bucketId);
        bucketAdminService.forceDelete(bucketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{bucketId}/block")
    public ResponseEntity<Void> blockBucket(
            @PathVariable Long bucketId
    ) {
        log.warn("ADMIN | Block bucketId={}", bucketId);
        bucketAdminService.blockBucket(bucketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{bucketId}/unblock")
    public ResponseEntity<Void> unblockBucket(
            @PathVariable Long bucketId
    ) {
        log.warn("ADMIN | Unblock bucketId={}", bucketId);
        bucketAdminService.unblockBucket(bucketId);
        return ResponseEntity.noContent().build();
    }
}
